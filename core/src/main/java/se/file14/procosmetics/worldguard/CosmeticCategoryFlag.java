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
package se.file14.procosmetics.worldguard;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;

public class CosmeticCategoryFlag extends Flag<CosmeticCategory<?, ?, ?>> {

    private final ProCosmetics plugin;

    public CosmeticCategoryFlag(String name, ProCosmetics plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public CosmeticCategory<?, ?, ?> parseInput(FlagContext context) throws InvalidFlagFormat {
        String input = context.getUserInput();
        CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(input);

        if (category == null) {
            throw new InvalidFlagFormat("Unknown cosmetic category: " + input);
        }
        return category;
    }

    @Override
    public CosmeticCategory<?, ?, ?> unmarshal(Object o) {
        if (o instanceof String str) {
            return plugin.getCategoryRegistries().getCategory(str);
        }
        return null;
    }

    @Override
    public Object marshal(CosmeticCategory o) {
        return o.getKey();
    }
}
