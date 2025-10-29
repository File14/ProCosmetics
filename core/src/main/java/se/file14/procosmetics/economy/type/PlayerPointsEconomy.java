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
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

public class PlayerPointsEconomy implements EconomyProvider {

    private PlayerPointsAPI api;

    @Override
    public String getPlugin() {
        return "PlayerPoints";
    }

    @Override
    public void hook(ProCosmetics plugin) throws IllegalStateException {
        api = PlayerPoints.getInstance().getAPI();

        if (api == null) {
            throw new IllegalStateException("PlayerPoints API was null! Is it installed?");
        }
    }

    @Override
    public int getCoins(User user) {
        return api.look(user.getUniqueId());
    }

    @Override
    public CompletableFuture<BooleanIntPair> getCoinsAsync(User user) {
        return CompletableFuture.completedFuture(BooleanIntPair.of(true, getCoins(user)));
    }

    @Override
    public CompletableFuture<Boolean> addCoinsAsync(User user, int amount) {
        return CompletableFuture.completedFuture(api.give(user.getUniqueId(), amount));
    }

    @Override
    public CompletableFuture<Boolean> setCoinsAsync(User user, int amount) {
        return CompletableFuture.completedFuture(api.set(user.getUniqueId(), amount));
    }

    @Override
    public CompletableFuture<Boolean> removeCoinsAsync(User user, int amount) {
        return CompletableFuture.completedFuture(api.take(user.getUniqueId(), amount));
    }
}
