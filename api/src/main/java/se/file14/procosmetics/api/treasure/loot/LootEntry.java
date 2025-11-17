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
import se.file14.procosmetics.api.treasure.loot.number.IntProvider;
import se.file14.procosmetics.api.util.ResolvableName;

/**
 * Represents an individual loot entry within a treasure chest loot pool.
 * <p>
 * Each {@code LootEntry} defines a potential loot that players can get
 * when opening a treasure chest. A loot entry may represent a cosmetic item,
 * currency, or other custom loot.
 *
 * @see se.file14.procosmetics.api.treasure.TreasureChest
 * @see CosmeticRarity
 */
public interface LootEntry extends ResolvableName {

    /**
     * Generates the actual loot based on this entry's configuration.
     * <p>
     * This method creates a {@link GeneratedLoot} instance which contains
     * the specific quantity and details of the reward to be given to the player.
     *
     * @return the generated loot instance
     */
    GeneratedLoot generate();

    /**
     * Gets the {@link IntProvider} that determines the quantity of this loot.
     *
     * @return the {@link IntProvider} for quantity calculation
     */
    IntProvider getIntProvider();

    /**
     * Gets the category this loot entry belongs to.
     *
     * @return the loot category
     */
    LootCategory getCategory();

    /**
     * Gets the rarity associated with this loot entry.
     *
     * @return the {@link CosmeticRarity} of this loot entry
     */
    CosmeticRarity getRarity();

    /**
     * Gets the {@link ItemStack} associated with this loot entry.
     *
     * @return the {@link ItemStack} representing the loot
     */
    ItemStack getItemStack();
}
