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
package se.file14.procosmetics.economy;

import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.economy.type.BuiltInEconomy;
import se.file14.procosmetics.economy.type.PlayerPointsEconomy;
import se.file14.procosmetics.economy.type.VaultEconomy;

import java.util.function.Function;

public enum EconomyType {

    BUILT_IN("Built-In", BuiltInEconomy::new),
    VAULT("Vault", plugin -> new VaultEconomy()),
    PLAYER_POINTS("Player Points", plugin -> new PlayerPointsEconomy()),
    CUSTOM("Custom", null);

    private final String name;
    private final Function<ProCosmetics, EconomyProvider> factory;

    EconomyType(String name, Function<ProCosmetics, EconomyProvider> factory) {
        this.name = name;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public EconomyProvider create(ProCosmetics plugin) {
        return factory != null ? factory.apply(plugin) : null;
    }
}
