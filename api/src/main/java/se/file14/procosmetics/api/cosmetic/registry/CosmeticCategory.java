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
package se.file14.procosmetics.api.cosmetic.registry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.menu.CosmeticMenu;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;

/**
 * Represents a category that groups related cosmetics together.
 * <p>
 * Each category manages a registry of cosmetics, configuration,
 * permissions, and menu presentation data.
 *
 * @param <T> the cosmetic type contained within this category
 * @param <B> the behavior type associated with the cosmetic
 * @param <U> the builder type used to construct cosmetic types
 * @see CosmeticRegistry
 * @see CosmeticType
 * @see CosmeticBehavior
 */
public interface CosmeticCategory<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>,
        U extends CosmeticType.Builder<T, B, U>> {

    /**
     * Gets the unique key identifying this category.
     *
     * @return the category key
     */
    String getKey();

    /**
     * Checks whether this category is enabled.
     *
     * @return {@code true} if the category is enabled, otherwise {@code false}
     */
    boolean isEnabled();

    /**
     * Gets the permission required to <b>equip</b> all cosmetics within this category.
     * <p>
     * Players with this permission can equip any cosmetic in the category,
     * regardless of individual cosmetic permissions.
     *
     * @return the equip permission string, or {@code null} if not required
     */
    String getPermission();

    /**
     * Gets the permission required to <b>purchase</b> all cosmetics within this category.
     * <p>
     * Players with this permission can purchase any cosmetic in the category,
     * even if individual purchase permissions are normally required.
     *
     * @return the purchase permission string, or {@code null} if not required
     */
    String getPurchasePermission();

    /**
     * Gets the cosmetic registry managing cosmetics within this category.
     *
     * @return the {@link CosmeticRegistry} instance
     */
    CosmeticRegistry<T, B, U> getCosmeticRegistry();

    /**
     * Gets the number of cosmetics unlocked by a specific player in this category.
     *
     * @param player the player to check
     * @return the number of unlocked cosmetics
     */
    int getUnlockedCosmetics(Player player);

    /**
     * Creates a new cosmetic menu instance for this category.
     *
     * @param plugin the main {@link ProCosmetics} plugin instance
     * @param user   the user for whom the menu is created
     * @return a new {@link CosmeticMenu} instance
     */
    CosmeticMenu<T> createMenu(ProCosmetics plugin, User user);

    /**
     * Gets the configuration associated with this category, if any.
     *
     * @return the category configuration, or {@code null} if not applicable
     */
    @Nullable
    Config getConfig();

    /**
     * Builder interface for constructing {@link CosmeticCategory} instances.
     */
    interface Builder {

        /**
         * Sets the unique key of the category.
         *
         * @param key the category key
         * @return this builder instance
         */
        Builder key(String key);

        /**
         * Sets the cosmetic registry associated with this category.
         *
         * @param registry the cosmetic registry
         * @return this builder instance
         */
        Builder registry(CosmeticRegistry<?, ?, ?> registry);

        /**
         * Sets whether the category should be enabled.
         *
         * @param enabled {@code true} to enable the category
         * @return this builder instance
         */
        Builder enabled(boolean enabled);

        /**
         * Sets the permission required to <b>equip</b> all cosmetics in this category.
         *
         * @param permission the equip permission
         * @return this builder instance
         */
        Builder permission(String permission);

        /**
         * Sets the permission required to <b>purchase</b> all cosmetics in this category.
         *
         * @param permission the purchase permission
         * @return this builder instance
         */
        Builder purchasePermission(String permission);

        /**
         * Sets the item used to represent this category in menus.
         *
         * @param item the display item
         * @return this builder instance
         */
        Builder menuItem(ItemStack item);

        /**
         * Sets the item used to represent this category in menus.
         *
         * @param itemBuilder the {@link ItemBuilder} for constructing the item
         * @return this builder instance
         */
        Builder menuItem(ItemBuilder itemBuilder);

        /**
         * Sets the slot position of this category’s item in menus.
         *
         * @param slot the menu slot index
         * @return this builder instance
         */
        Builder menuSlot(int slot);

        /**
         * Sets the display name of this category.
         *
         * @param displayName the display name
         * @return this builder instance
         */
        Builder displayName(String displayName);

        /**
         * Sets the description lines shown for this category in menus.
         *
         * @param description one or more description lines
         * @return this builder instance
         */
        Builder description(String... description);

        /**
         * Builds and returns the configured {@link CosmeticCategory} instance.
         *
         * @return the built cosmetic category
         */
        CosmeticCategory<?, ?, ?> build();

        /**
         * Creates a new {@link Builder} instance.
         *
         * @return a new builder instance
         * @throws UnsupportedOperationException always, unless implemented
         */
        static Builder create() {
            throw new UnsupportedOperationException("Must be implemented by plugin");
        }
    }
}
