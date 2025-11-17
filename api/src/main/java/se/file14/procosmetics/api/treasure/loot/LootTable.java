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

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;

import java.util.List;
import java.util.Map;

/**
 * Represents a loot table containing all possible loot entries for a treasure chest.
 */
public interface LootTable {

    /**
     * Rolls for a random loot entry based on weighted random selection.
     *
     * @return a randomly selected loot entry, or null if no entries are available
     */
    @Nullable
    LootEntry rollLoot();

    /**
     * Calculates the percentage chance of obtaining a specific loot entry.
     *
     * @param entry the loot entry to calculate chance for
     * @return the percentage chance (0.0 - 100.0)
     */
    double getEntryChance(LootEntry entry);

    /**
     * Calculates the percentage chance of a rarity.
     *
     * @param rarity the rarity to calculate chance for
     * @return the percentage chance (0.0 - 100.0)
     */
    double getRarityChance(CosmeticRarity rarity);

    /**
     * Gets all loot entries in this table.
     *
     * @return an immutable list of all loot entries
     */
    List<LootEntry> getEntries();

    /**
     * Gets all loot entries organized by their categories.
     *
     * @return a map of categories to their loot entries
     */
    Map<LootCategory, List<LootEntry>> getEntriesByCategory();

    /**
     * Gets the total weight of all entries in this loot table.
     *
     * @return the total weight
     */
    int getTotalWeight();
}
