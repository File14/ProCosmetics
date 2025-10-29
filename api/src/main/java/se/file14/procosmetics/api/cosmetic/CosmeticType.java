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
package se.file14.procosmetics.api.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;

import java.util.function.Supplier;

/**
 * Represents a type of cosmetic that users can equip.
 * This interface defines the core properties and behaviors of a cosmetic type.
 *
 * @param <T> the specific cosmetic type implementation
 * @param <B> the behavior associated with this cosmetic type
 */
public interface CosmeticType<T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>> extends LootEntry {

    /**
     * Equips this cosmetic type to the specified user.
     *
     * @param user           the user to equip the cosmetic to
     * @param silent         whether to suppress equip messages
     * @param saveToDatabase whether to persist the equip state to the database
     */
    void equip(User user, boolean silent, boolean saveToDatabase);

    /**
     * Gets the unique identifier key for this cosmetic type.
     *
     * @return the cosmetic's unique key
     */
    String getKey();

    /**
     * Gets the category this cosmetic type belongs to.
     *
     * @return the cosmetic category
     */
    CosmeticCategory<T, B, ?> getCategory();

    /**
     * Gets the permission node required to use this cosmetic.
     *
     * @return the permission string
     */
    String getPermission();

    /**
     * Checks if the specified player has permission to use this cosmetic.
     *
     * @param player the player to check permissions for
     * @return true if the player has permission, false otherwise
     */
    boolean hasPermission(Player player);

    /**
     * Checks if the specified player has permission to purchase this cosmetic.
     *
     * @param player the player to check permissions for
     * @return true if the player has permission, false otherwise
     */
    boolean hasPurchasePermission(Player player);

    /**
     * Checks if this cosmetic type is currently enabled.
     *
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Sets whether this cosmetic can be found in treasures.
     *
     * @param findable true to make the cosmetic findable, false otherwise
     */
    void setFindable(boolean findable);

    /**
     * Checks if this cosmetic can be found in loot sources.
     *
     * @return true if findable, false otherwise
     */
    boolean isFindable();

    /**
     * Sets whether this cosmetic can be purchased.
     *
     * @param purchasable true to make the cosmetic purchasable, false otherwise
     */
    void setPurchasable(boolean purchasable);

    /**
     * Checks if this cosmetic can be purchased.
     *
     * @return true if purchasable, false otherwise
     */
    boolean isPurchasable();

    /**
     * Sets the cost to purchase this cosmetic.
     *
     * @param cost the purchase cost
     */
    void setCost(int cost);

    /**
     * Gets the cost to purchase this cosmetic.
     *
     * @return the purchase cost
     */
    int getCost();

    /**
     * Gets the item stack representation of this cosmetic.
     *
     * @return the display item stack
     */
    ItemStack getItemStack();

    /**
     * Gets the rarity of this cosmetic type.
     *
     * @return the cosmetic rarity
     */
    CosmeticRarity getRarity();

    /**
     * Builder interface for constructing cosmetic type instances.
     *
     * @param <T> the specific cosmetic type implementation
     * @param <B> the behavior associated with this cosmetic type
     * @param <S> the builder implementation type for method chaining
     */
    interface Builder<T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>, S extends Builder<T, B, S>> {

        /**
         * Reads and applies configuration values from the config file.
         *
         * @return this builder for method chaining
         */
        S readFromConfig();

        /**
         * Sets the item stack representation for this cosmetic.
         *
         * @param itemStack the display item stack
         * @return this builder for method chaining
         */
        S itemStack(ItemStack itemStack);

        /**
         * Sets the rarity of this cosmetic.
         *
         * @param rarity the cosmetic rarity
         * @return this builder for method chaining
         */
        S rarity(CosmeticRarity rarity);

        /**
         * Sets whether this cosmetic is enabled.
         *
         * @param enabled true to enable, false to disable
         * @return this builder for method chaining
         */
        S enabled(boolean enabled);

        /**
         * Sets whether this cosmetic can be found in loot sources.
         *
         * @param findable true if findable, false otherwise
         * @return this builder for method chaining
         */
        S findable(boolean findable);

        /**
         * Sets whether this cosmetic can be purchased.
         *
         * @param purchasable true if purchasable, false otherwise
         * @return this builder for method chaining
         */
        S purchasable(boolean purchasable);

        /**
         * Sets the purchase cost for this cosmetic.
         *
         * @param cost the purchase cost
         * @return this builder for method chaining
         */
        S cost(int cost);

        /**
         * Sets the factory for creating behavior instances.
         *
         * @param factory the behavior factory supplier
         * @return this builder for method chaining
         */
        S factory(Supplier<B> factory);

        /**
         * Builds and returns the configured cosmetic type instance.
         *
         * @return the built cosmetic type
         */
        T build();
    }
}
