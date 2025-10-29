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

import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;

import java.util.Random;

public abstract class LootTable<T extends LootEntry> {

    protected static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    protected static final Random RANDOM = new Random();

    protected final String key;
    protected final int weight;

    public LootTable(String key, int weight) {
        this.key = key;
        this.weight = weight;
    }

    public abstract T getRandomLoot();

    public abstract void give(Player player, User user, T lootEntry);

    public String getCategory(User user) {
        return user.translateRaw("treasure_chest.category." + key);
    }

    public String getKey() {
        return key;
    }

    public int getWeight() {
        return weight;
    }
}
