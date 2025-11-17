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
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.ResolvableName;

/**
 * Represents a generated instance of loot from a {@link LootEntry}.
 *
 * @see LootEntry
 * @see LootEntry#generate()
 */
public interface GeneratedLoot extends ResolvableName {

    /**
     * Gets the loot entry template that generated this loot.
     *
     * @return the source loot entry
     */
    LootEntry getEntry();

    /**
     * Gets the generated amount for this loot.
     *
     * @return the amount (e.g., number of coins, ammo count, or 1 for cosmetics)
     */
    int getAmount();

    /**
     * Gets the ItemStack representing this loot.
     *
     * @return the ItemStack
     */
    ItemStack getItemStack();

    /**
     * Gets the rarity of this loot.
     *
     * @return the rarity
     */
    CosmeticRarity getRarity();

    /**
     * Gets the category this loot belongs to.
     *
     * @return the loot category
     */
    LootCategory getCategory();

    /**
     * Gives this generated loot to the player.
     *
     * @param player the player to receive the loot
     * @param user   the user representation of the player
     */
    void give(Player player, User user);
}
