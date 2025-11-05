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
package se.file14.procosmetics.api.treasure.loot;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.user.User;

/**
 * Represents a loot table that can generate random loot entries.
 *
 * @param <T> the type of loot entry this table produces
 */
public interface LootTable<T extends LootEntry> {

    /**
     * Generates a random loot entry from this loot table.
     *
     * @return a randomly selected loot entry
     */
    T getRandomLoot();

    /**
     * Gives the specified loot entry to a player.
     *
     * @param player    the player to receive the loot
     * @param user      the user representation of the player
     * @param lootEntry the loot entry to give
     */
    void give(Player player, User user, T lootEntry);

    /**
     * Gets the translated category name for this loot table.
     *
     * @param user the user to get the translation for
     * @return the translated category name
     */
    String getCategory(User user);

    /**
     * Gets the unique key identifier for this loot table.
     *
     * @return the loot table key
     */
    String getKey();

    /**
     * Gets the weight of this loot table for weighted random selection.
     *
     * @return the weight value
     */
    int getWeight();
}
