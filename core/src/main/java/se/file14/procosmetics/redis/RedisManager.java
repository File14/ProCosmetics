package se.file14.procosmetics.redis;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.redis.messages.CoinUpdateMessage;
import se.file14.procosmetics.redis.messages.GadgetAmmoUpdateMessage;
import se.file14.procosmetics.redis.messages.TreasureChestUpdateMessage;

import java.util.concurrent.CompletableFuture;

public class RedisManager {

    private final ProCosmeticsPlugin plugin;
    private final RedisProvider redisProvider;
    private final PacketMessenger messenger;

    private Channel<CoinUpdateMessage> coinChannel;
    private Channel<GadgetAmmoUpdateMessage> gadgetAmmoChannel;
    private Channel<TreasureChestUpdateMessage> treasureChestsChannel;

    public RedisManager(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        Config config = plugin.getConfigManager().getMainConfig();
        this.redisProvider = new RedisProvider(plugin,
                config.getString("redis.host"),
                config.getInt("redis.port"),
                config.getString("redis.username"),
                config.getString("redis.password"),
                config.getBoolean("redis.ssl")
        );
        this.messenger = new PacketMessenger(plugin);
    }

    public void registerChannels() {
        // Coins
        coinChannel = messenger.registerChannel(getChannelKey("coins"), CoinUpdateMessage.SERIALIZER);
        coinChannel.listen(message ->
                CompletableFuture.runAsync(() -> handleCoinUpdate(message), plugin.getSyncExecutor())
        );

        // Gadget ammo
        gadgetAmmoChannel = messenger.registerChannel(getChannelKey("gadget_ammo"), GadgetAmmoUpdateMessage.SERIALIZER);
        gadgetAmmoChannel.listen(message ->
                CompletableFuture.runAsync(() -> handleGadgetAmmoUpdate(message), plugin.getSyncExecutor())
        );

        // Treasure chests
        treasureChestsChannel = messenger.registerChannel(getChannelKey("treasure_chests"), TreasureChestUpdateMessage.SERIALIZER);
        treasureChestsChannel.listen(message ->
                CompletableFuture.runAsync(() -> handleTreasureKeyUpdate(message), plugin.getSyncExecutor())
        );
    }

    private String getChannelKey(String key) {
        return plugin.getName() + ":" + key;
    }

    private void handleCoinUpdate(CoinUpdateMessage message) {
        User user = plugin.getUserManager().getConnected(message.getUserId());

        if (user != null) {
            user.setCoins(message.getAmount());
        }
    }

    private void handleGadgetAmmoUpdate(GadgetAmmoUpdateMessage message) {
        User user = plugin.getUserManager().getConnected(message.getUserId());

        if (user != null) {
            GadgetType gadgetType = plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getType(message.getGadgetKey());

            if (gadgetType != null) {
                user.setAmmo(gadgetType, message.getAmount());
            }
        }
    }

    private void handleTreasureKeyUpdate(TreasureChestUpdateMessage message) {
        User user = plugin.getUserManager().getConnected(message.getUserId());

        if (user != null) {
            TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(message.getTreasureKey());

            if (treasureChest != null) {
                user.setTreasureChests(treasureChest, message.getAmount());
            }
        }
    }

    public void sendCoinUpdate(int databaseId, int amount) {
        coinChannel.sendAsync(new CoinUpdateMessage(databaseId, amount));
    }

    public void sendGadgetAmmoUpdate(int databaseId, String gadgetKey, int amount) {
        gadgetAmmoChannel.sendAsync(new GadgetAmmoUpdateMessage(databaseId, gadgetKey, amount));
    }

    public void sendTreasureKeyUpdate(int databaseId, String treasureKey, int amount) {
        treasureChestsChannel.sendAsync(new TreasureChestUpdateMessage(databaseId, treasureKey, amount));
    }

    public void shutdown() {
        messenger.unregisterChannels();
        redisProvider.close();
    }

    public RedisProvider getRedis() {
        return redisProvider;
    }

    public PacketMessenger getMessenger() {
        return messenger;
    }
}