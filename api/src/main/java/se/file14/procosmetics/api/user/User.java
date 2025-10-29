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
package se.file14.procosmetics.api.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;

import java.util.Map;
import java.util.UUID;

/**
 * Represents a player in the ProCosmetics system.
 * <p>
 * Each online player has a corresponding {@code User} instance that is used for
 * both runtime cosmetic state and persistent database synchronization.
 *
 * @see UserManager
 * @see Cosmetic
 * @see CosmeticCategory
 * @see TreasureChest
 */
public interface User extends Translator {

    /**
     * Displays a title to the player.
     *
     * @param title    the main title component
     * @param subTitle the subtitle component
     * @param times    the display timings for the title
     */
    void showTitle(Component title, Component subTitle, Title.Times times);

    /**
     * Sends an action bar message to the player.
     *
     * @param component the message to display
     */
    void sendActionBar(Component component);

    /**
     * Sends a chat message to the player.
     *
     * @param component the message to send
     */
    void sendMessage(Component component);

    /**
     * Gets the database ID of this user..
     *
     * @return the database ID
     */
    int getDatabaseId();

    /**
     * Gets the unique Minecraft UUID of this player.
     *
     * @return the player’s UUID
     */
    UUID getUniqueId();

    /**
     * Gets the name of this player.
     *
     * @return the player’s name
     */
    String getName();

    /**
     * Gets the underlying Bukkit {@link Player} instance, if online.
     *
     * @return the {@link Player}, or {@code null} if the player is offline
     */
    @Nullable
    Player getPlayer();

    /**
     * Checks whether the player is currently moving.
     *
     * @return {@code true} if the player is moving
     */
    boolean isMoving();

    /**
     * Sets the player's movement state.
     *
     * @param moving {@code true} if the player is moving
     */
    @ApiStatus.Internal
    void setMoving(boolean moving);

    /**
     * Gets the player’s last recorded location.
     *
     * @return the last known {@link Location}
     */
    @ApiStatus.Internal
    Location getLastLocation();

    /**
     * Updates the player’s last recorded location.
     *
     * @param location the new last known {@link Location}
     */
    @ApiStatus.Internal
    void setLastLocation(Location location);

    /**
     * Gets the number of coins the player currently has.
     *
     * @return the player’s coin balance
     */
    @ApiStatus.Internal
    int getCoins();

    /**
     * Sets the player’s coin balance.
     *
     * @param coins the new balance
     */
    @ApiStatus.Internal
    void setCoins(int coins);

    /**
     * Checks whether the player can see their own morph.
     *
     * @return {@code true} if self-view for morphs is enabled
     */
    boolean hasSelfViewMorph();

    /**
     * Sets whether the player can see their own morph.
     *
     * @param visible {@code true} to show the morph
     * @param update  {@code true} to immediately update the visual state
     */
    void setSelfViewMorph(boolean visible, boolean update);

    /**
     * Checks whether the player can see their own status.
     *
     * @return {@code true} if self-view for statuses is enabled
     */
    boolean hasSelfViewStatus();

    /**
     * Sets whether the player can see their own status.
     *
     * @param visible {@code true} to show the status
     * @param update  {@code true} to immediately update the visual state
     */
    void setSelfViewStatus(boolean visible, boolean update);

    /**
     * Checks if the player currently has fall damage protection.
     *
     * @return {@code true} if fall damage is prevented
     */
    boolean hasFallDamageProtection();

    /**
     * Applies temporary fall damage protection to the player.
     *
     * @param seconds the duration of protection in seconds
     */
    void setFallDamageProtection(int seconds);

    /**
     * Checks whether the player has an equipped cosmetic in the given category.
     *
     * @param category the cosmetic category to check
     * @return {@code true} if the player has a cosmetic equipped in the category
     */
    boolean hasCosmetic(CosmeticCategory<?, ?, ?> category);

    /**
     * Gets the cosmetic currently equipped in the given category.
     *
     * @param category the cosmetic category
     * @return the equipped cosmetic, or {@code null} if none
     */
    @Nullable
    Cosmetic<?, ?> getCosmetic(CosmeticCategory<?, ?, ?> category);

    /**
     * Sets the equipped cosmetic for the given category.
     *
     * @param category the category of the cosmetic
     * @param cosmetic the cosmetic to equip
     */
    @ApiStatus.Internal
    void setCosmetic(CosmeticCategory<?, ?, ?> category, Cosmetic<?, ?> cosmetic);

    /**
     * Gets all currently equipped cosmetics across all categories.
     *
     * @return a map of categories to their equipped cosmetics
     */
    @ApiStatus.Internal
    Map<CosmeticCategory<?, ?, ?>, Cosmetic<?, ?>> getCosmetics();

    /**
     * Equips all cosmetics previously saved for this player.
     *
     * @param silent {@code true} to suppress messages or sounds
     */
    void equipSavedCosmetics(boolean silent);

    /**
     * Unequips a cosmetic in a specific category.
     *
     * @param category       the category to unequip
     * @param silent         {@code true} to suppress messages or sounds
     * @param saveToDatabase {@code true} to update the database
     */
    void unequipCosmetic(CosmeticCategory<?, ?, ?> category, boolean silent, boolean saveToDatabase);

    /**
     * Unequips all equipped cosmetics.
     *
     * @param silent         {@code true} to suppress messages or sounds
     * @param saveToDatabase {@code true} to update the database
     */
    void unequipCosmetics(boolean silent, boolean saveToDatabase);

    /**
     * Removes a cosmetic from the player’s data entirely.
     *
     * @param category       the category to remove
     * @param silent         {@code true} to suppress messages or sounds
     * @param saveToDatabase {@code true} to update the database
     */
    void removeCosmetic(CosmeticCategory<?, ?, ?> category, boolean silent, boolean saveToDatabase);

    /**
     * Clears all cosmetics from the player.
     *
     * @param silent         {@code true} to suppress messages or sounds
     * @param saveToDatabase {@code true} to update the database
     */
    void clearAllCosmetics(boolean silent, boolean saveToDatabase);

    /**
     * Checks whether the player is currently opening a treasure chest.
     *
     * @return {@code true} if the player is in a treasure-opening session
     */
    boolean isOpeningTreasure();

    /**
     * Gets the treasure platform currently assigned to this user.
     *
     * @return the current {@link TreasureChestPlatform}, or {@code null} if none
     */
    @Nullable
    TreasureChestPlatform getCurrentPlatform();

    /**
     * Sets the current treasure platform assigned to this user.
     *
     * @param platform the treasure platform to assign
     */
    @ApiStatus.Internal
    void setCurrentPlatform(TreasureChestPlatform platform);

    /**
     * Checks whether the player has at least the specified number of a given treasure chest.
     *
     * @param treasureChest the treasure chest type
     * @param amount        the required amount
     * @return {@code true} if the player has enough chests
     */
    boolean hasTreasureChests(TreasureChest treasureChest, int amount);

    /**
     * Gets how many of a specific treasure chest the player currently owns.
     *
     * @param treasureChest the treasure chest type
     * @return the number of chests owned
     */
    int getTreasureChests(TreasureChest treasureChest);

    /**
     * Updates the number of a specific treasure chest owned by the player.
     *
     * @param treasureChest the treasure chest type
     * @param amount        the new amount
     */
    @ApiStatus.Internal
    void setTreasureChests(TreasureChest treasureChest, int amount);

    /**
     * Gets a map of all treasure chest types owned by the player.
     *
     * @return a map of {@link TreasureChest} to quantity
     */
    @ApiStatus.Internal
    Map<TreasureChest, Integer> getTreasureChests();

    /**
     * Checks whether a gadget is currently on cooldown for this player.
     *
     * @param gadget the gadget type
     * @return {@code true} if the gadget is on cooldown
     */
    boolean hasCooldown(GadgetType gadget);

    /**
     * Gets the cooldown time for a gadget.
     *
     * @param gadget the gadget type
     * @return the remaining cooldown in seconds
     */
    double getCooldown(GadgetType gadget);

    /**
     * Sets a cooldown for a gadget.
     *
     * @param gadget the gadget type
     * @param time   the cooldown duration in seconds
     */
    void setCooldown(GadgetType gadget, double time);

    /**
     * Checks whether the player has at least a given amount of ammo for a gadget.
     *
     * @param gadget the gadget type
     * @param amount the required amount
     * @return {@code true} if the player has enough ammo
     */
    boolean hasAmmo(GadgetType gadget, int amount);

    /**
     * Gets the amount of ammo the player currently has for a gadget.
     *
     * @param gadget the gadget type
     * @return the current ammo count
     */
    int getAmmo(GadgetType gadget);

    /**
     * Updates the ammo count for a specific gadget.
     *
     * @param gadget the gadget type
     * @param amount the new ammo amount
     */
    @ApiStatus.Internal
    void setAmmo(GadgetType gadget, int amount);

    /**
     * Gets all stored gadget ammo amounts of the player.
     *
     * @return a map of {@link GadgetType} to ammo count
     */
    @ApiStatus.Internal
    Map<GadgetType, Integer> getAmmo();

    /**
     * Returns a formatted display string containing the player’s name, database ID, and UUID.
     *
     * @return a human-readable user identifier string
     */
    default String getDebugName() {
        return getName() + " (" + getDatabaseId() + ", " + getUniqueId().toString() + ")";
    }
}
