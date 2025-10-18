package se.file14.procosmetics.storage;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.storage.Database;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

public abstract class DatabaseImpl implements Database {

    protected final ProCosmeticsPlugin plugin;
    private final boolean redisEnabled;

    public DatabaseImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        this.redisEnabled = plugin.getRedisManager() != null;
    }

    protected abstract CompletableFuture<BooleanIntPair> addCoinsAsyncImpl(User user, int amount);

    protected abstract CompletableFuture<BooleanIntPair> removeCoinsAsyncImpl(User user, int amount);

    protected abstract CompletableFuture<BooleanIntPair> setCoinsAsyncImpl(User user, int amount);

    protected abstract CompletableFuture<BooleanIntPair> addGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount);

    protected abstract CompletableFuture<BooleanIntPair> removeGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount);

    protected abstract CompletableFuture<BooleanIntPair> setGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount);

    protected abstract CompletableFuture<BooleanIntPair> addTreasureKeysAsyncImpl(User user, TreasureChest treasureChest, int amount);

    protected abstract CompletableFuture<BooleanIntPair> removeTreasureKeysAsyncImpl(User user, TreasureChest treasureChest, int amount);

    protected abstract CompletableFuture<BooleanIntPair> setTreasureKeysAsyncImpl(User user, TreasureChest treasureChest, int amount);

    // Wrapper methods for Redis messaging
    @Override
    public CompletableFuture<BooleanIntPair> addCoinsAsync(User user, int amount) {
        if (!redisEnabled) {
            return addCoinsAsyncImpl(user, amount);
        }
        return addCoinsAsyncImpl(user, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendCoinUpdate(user.getDatabaseId(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeCoinsAsync(User user, int amount) {
        if (!redisEnabled) {
            return removeCoinsAsyncImpl(user, amount);
        }
        return removeCoinsAsyncImpl(user, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendCoinUpdate(user.getDatabaseId(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> setCoinsAsync(User user, int amount) {
        if (!redisEnabled) {
            return setCoinsAsyncImpl(user, amount);
        }
        return setCoinsAsyncImpl(user, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendCoinUpdate(user.getDatabaseId(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> addGadgetAmmoAsync(User user, GadgetType gadgetType, int amount) {
        if (!redisEnabled) {
            return addGadgetAmmoAsyncImpl(user, gadgetType, amount);
        }
        return addGadgetAmmoAsyncImpl(user, gadgetType, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendGadgetAmmoUpdate(user.getDatabaseId(), gadgetType.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeGadgetAmmoAsync(User user, GadgetType gadgetType, int amount) {
        if (!redisEnabled) {
            return removeGadgetAmmoAsyncImpl(user, gadgetType, amount);
        }
        return removeGadgetAmmoAsyncImpl(user, gadgetType, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendGadgetAmmoUpdate(user.getDatabaseId(), gadgetType.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> setGadgetAmmoAsync(User user, GadgetType gadgetType, int amount) {
        if (!redisEnabled) {
            return setGadgetAmmoAsyncImpl(user, gadgetType, amount);
        }
        return setGadgetAmmoAsyncImpl(user, gadgetType, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendGadgetAmmoUpdate(user.getDatabaseId(), gadgetType.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> addTreasureKeysAsync(User user, TreasureChest treasureChest, int amount) {
        if (!redisEnabled) {
            return addTreasureKeysAsyncImpl(user, treasureChest, amount);
        }
        return addTreasureKeysAsyncImpl(user, treasureChest, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendTreasureKeyUpdate(user.getDatabaseId(), treasureChest.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeTreasureKeysAsync(User user, TreasureChest treasureChest, int amount) {
        if (!redisEnabled) {
            return removeTreasureKeysAsyncImpl(user, treasureChest, amount);
        }
        return removeTreasureKeysAsyncImpl(user, treasureChest, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendTreasureKeyUpdate(user.getDatabaseId(), treasureChest.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> setTreasureKeysAsync(User user, TreasureChest treasureChest, int amount) {
        if (!redisEnabled) {
            return setTreasureKeysAsyncImpl(user, treasureChest, amount);
        }
        return setTreasureKeysAsyncImpl(user, treasureChest, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendTreasureKeyUpdate(user.getDatabaseId(), treasureChest.getKey(), result.rightInt());
                    }
                    return result;
                });
    }
}