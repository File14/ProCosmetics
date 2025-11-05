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

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;

import java.util.List;

/**
 * Represents a loot table that with custom rewards.
 */
public interface CustomLoot extends LootTable<CustomLoot>, LootEntry {

    /**
     * Gets the item stack representation of this custom loot.
     *
     * @return the item stack to display
     */
    ItemStack getItemStack();

    /**
     * Gets the rarity of this custom loot.
     *
     * @return the rarity level
     */
    CosmeticRarity getRarity();

    /**
     * Gets the list of commands to execute when this loot is rewarded.
     *
     * @return the list of commands to execute
     */
    List<String> getCommands();
}
