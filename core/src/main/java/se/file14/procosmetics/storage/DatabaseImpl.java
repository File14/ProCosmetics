/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

    protected abstract CompletableFuture<BooleanIntPair> addTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount);

    protected abstract CompletableFuture<BooleanIntPair> removeTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount);

    protected abstract CompletableFuture<BooleanIntPair> setTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount);

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
    public CompletableFuture<BooleanIntPair> addTreasureChestsAsync(User user, TreasureChest treasureChest, int amount) {
        if (!redisEnabled) {
            return addTreasureChestsAsyncImpl(user, treasureChest, amount);
        }
        return addTreasureChestsAsyncImpl(user, treasureChest, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendTreasureKeyUpdate(user.getDatabaseId(), treasureChest.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeTreasureChestsAsync(User user, TreasureChest treasureChest, int amount) {
        if (!redisEnabled) {
            return removeTreasureChestsAsyncImpl(user, treasureChest, amount);
        }
        return removeTreasureChestsAsyncImpl(user, treasureChest, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendTreasureKeyUpdate(user.getDatabaseId(), treasureChest.getKey(), result.rightInt());
                    }
                    return result;
                });
    }

    @Override
    public CompletableFuture<BooleanIntPair> setTreasureChestsAsync(User user, TreasureChest treasureChest, int amount) {
        if (!redisEnabled) {
            return setTreasureChestsAsyncImpl(user, treasureChest, amount);
        }
        return setTreasureChestsAsyncImpl(user, treasureChest, amount)
                .thenApply(result -> {
                    if (result.leftBoolean()) {
                        plugin.getRedisManager().sendTreasureKeyUpdate(user.getDatabaseId(), treasureChest.getKey(), result.rightInt());
                    }
                    return result;
                });
    }
}
