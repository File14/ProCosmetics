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
package se.file14.procosmetics.treasure.loot;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.treasure.loot.LootTable;
import se.file14.procosmetics.api.user.User;

import java.util.Random;

public abstract class LootTableImpl<T extends LootEntry> implements LootTable<T> {

    protected static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    protected static final Random RANDOM = new Random();

    protected final String key;
    protected final int weight;

    public LootTableImpl(String key, int weight) {
        this.key = key;
        this.weight = weight;
    }

    @Override
    public String getCategory(User user) {
        return user.translateRaw("treasure_chest.category." + key);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
