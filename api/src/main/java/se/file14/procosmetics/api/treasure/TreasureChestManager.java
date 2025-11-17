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
package se.file14.procosmetics.api.treasure;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.util.broadcaster.Broadcaster;
import se.file14.procosmetics.api.util.broadcaster.LootBroadcaster;

import java.util.List;

/**
 * Manages all registered treasure chests and active treasure platforms.
 * <p>
 * The {@code TreasureChestManager} provides access to loaded treasure chests,
 * their corresponding platforms, and utility methods for retrieving and managing
 * active treasure sessions.
 *
 * @see TreasureChest
 * @see TreasureChestPlatform
 */
public interface TreasureChestManager {

    /**
     * Gets the treasure platform located at the specified world location.
     *
     * @param location the world location to check
     * @return the {@link TreasureChestPlatform} at the location, or {@code null} if none exists
     */
    @Nullable
    TreasureChestPlatform getPlatform(@Nullable Location location);

    /**
     * Gets a treasure platform by its unique numeric identifier.
     *
     * @param id the platform ID
     * @return the {@link TreasureChestPlatform} associated with the ID, or {@code null} if not found
     */
    @Nullable
    TreasureChestPlatform getPlatform(int id);

    /**
     * Gets a treasure chest by its unique key.
     *
     * @param key the treasure chest key
     * @return the {@link TreasureChest} associated with the key, or {@code null} if not found
     */
    @Nullable
    TreasureChest getTreasureChest(@Nullable String key);

    /**
     * Gets the broadcaster responsible for announcing treasure chest openings.
     *
     * @return the {@link Broadcaster} for treasure chest opening announcements
     */
    Broadcaster getOpeningBroadcaster();

    /**
     * Gets the loot broadcaster responsible for announcing treasure chest rewards.
     *
     * @return the {@link LootBroadcaster} for treasure chest loot announcements
     */
    LootBroadcaster getLootBroadcaster();

    /**
     * Gets a list of all loaded treasure chests.
     *
     * @return a list of all {@link TreasureChest} instances
     */
    List<TreasureChest> getTreasureChests();
}
