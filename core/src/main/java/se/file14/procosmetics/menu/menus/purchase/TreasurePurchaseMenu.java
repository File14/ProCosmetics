package se.file14.procosmetics.menu.menus.purchase;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.event.PlayerPurchaseTreasureChestEventImpl;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class TreasurePurchaseMenu extends MenuImpl {

    private static final int COOLDOWN = 20;

    private final TreasureChest treasureChest;
    private final Config config;

    public TreasurePurchaseMenu(ProCosmetics plugin, User user, TreasureChest treasureChest) {
        super(plugin, user,
                user.translate("menu.purchase.treasure_chest.title"),
                plugin.getConfigManager().getMainConfig().getInt("menu.purchase.treasure_chest.rows")
        );
        this.treasureChest = treasureChest;
        this.config = plugin.getConfigManager().getMainConfig();
    }

    @Override
    protected void addItems() {
        // Treasure item
        ItemBuilder treasureChestItem = new ItemBuilderImpl(treasureChest.getItemStack());
        String name = treasureChest.getName(user);
        TagResolver tagResolver = treasureChest.getResolvers(user);

        // TODO: Add something like this to localization
        treasureChestItem.setDisplayName(user.translate(
                "menu." + treasureChest.getKey() + ".name",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        treasureChestItem.setLoreComponent(user.translateList(
                "menu." + treasureChest.getKey() + ".desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));

        setItem(13, treasureChestItem.getItemStack(), event -> {
        });
        ItemBuilder acceptPurchase = new ItemBuilderImpl(config, "menu.purchase.treasure_chest.items.accept");
        acceptPurchase.setDisplayName(user.translate(
                "menu.purchase.treasure_chest.accept.name",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        acceptPurchase.setLoreComponent(user.translateList(
                "menu.purchase.treasure_chest.accept.desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

        // Accept purchase button
        setItem(acceptPurchase.getSlot(), acceptPurchase.getItemStack(), event -> {
            if (player.hasCooldown(acceptPurchase.getItemStack())) {
                return;
            }
            close();
            player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

            EconomyProvider economy = plugin.getEconomyManager().getEconomyProvider();
            int cost = treasureChest.getCost();

            if (!economy.hasCoins(user, cost)) {
                economy.sendInsufficientCoinsMessage(user, cost);
                playDenySound();
                return;
            }
            economy.removeCoinsAsync(user, cost).thenAcceptAsync(result -> {
                if (result.booleanValue()) {
                    plugin.getDatabase().addTreasureKeysAsync(user, treasureChest, 1).thenAcceptAsync(result2 -> {
                        if (result2.leftBoolean()) {
                            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            plugin.getJavaPlugin().getServer().getPluginManager().callEvent(new PlayerPurchaseTreasureChestEventImpl(plugin, user, player, treasureChest));
                        } else {
                            // Failed, refund the coins
                            economy.addCoinsAsync(user, cost);
                            user.sendMessage(user.translate("generic.error.database"));
                        }
                    }, plugin.getSyncExecutor());
                } else {
                    economy.sendInsufficientCoinsMessage(user, cost);
                    playDenySound();
                }
            }, plugin.getSyncExecutor());
        });

        // Deny purchase button
        ItemBuilder denyPurchase = new ItemBuilderImpl(config, "menu.purchase.treasure_chest.items.deny");
        denyPurchase.setDisplayName(user.translate("menu.purchase.treasure_chest.deny.name"));
        denyPurchase.setLoreComponent(user.translateList(
                "menu.purchase.treasure_chest.deny.desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));

        setItem(denyPurchase.getSlot(), denyPurchase.getItemStack(), event -> {
            playClickSound();
            close();
        });
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getConfigManager().getMainConfig();

        if (!config.getBoolean("menu.purchase.treasure_chest.items.fill_empty_slots.enable")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.purchase.treasure_chest.items.fill_empty_slots").getItemStack();
    }
}