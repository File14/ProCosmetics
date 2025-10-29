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
package se.file14.procosmetics.economy.type;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

public class BuiltInEconomy implements EconomyProvider {

    private final ProCosmetics plugin;

    public BuiltInEconomy(ProCosmetics plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPlugin() {
        return plugin.getJavaPlugin().getName();
    }

    @Override
    public void hook(ProCosmetics plugin) throws IllegalStateException {
    }

    @Override
    public int getCoins(User user) {
        return user.getCoins();
    }

    @Override
    public CompletableFuture<BooleanIntPair> getCoinsAsync(User user) {
        return CompletableFuture.completedFuture(BooleanIntPair.of(true, getCoins(user)));
    }

    @Override
    public CompletableFuture<Boolean> addCoinsAsync(User user, int amount) {
        return plugin.getDatabase().addCoinsAsync(user, amount).thenApply(BooleanIntPair::leftBoolean);
    }

    @Override
    public CompletableFuture<Boolean> setCoinsAsync(User user, int amount) {
        return plugin.getDatabase().setCoinsAsync(user, amount).thenApply(BooleanIntPair::leftBoolean);
    }

    @Override
    public CompletableFuture<Boolean> removeCoinsAsync(User user, int amount) {
        return plugin.getDatabase().removeCoinsAsync(user, amount).thenApply(BooleanIntPair::leftBoolean);
    }
}
