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
package se.file14.procosmetics.api.nms;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Manages NMS (Net Minecraft Server) entity creation and conversion.
 * This interface provides version-independent access to NMS entity functionality
 * and allows conversion between Bukkit and NMS entity representations.
 */
public interface NMSManager {

    /**
     * Creates an NMS entity in the specified world with the given entity type.
     * Uses a default EntityTracker implementation.
     *
     * @param world      The world where the entity should be created
     * @param entityType The type of entity to create
     * @return The created NMS entity implementation, or null if creation failed
     */
    NMSEntity createEntity(World world, EntityType entityType);

    /**
     * Creates an NMS entity in the specified world with the given entity type and tracker.
     *
     * @param world         The world where the entity should be created
     * @param entityType    The type of entity to create
     * @param entityTracker The entity tracker to use, or null to use default
     * @return The created NMS entity implementation, or null if creation failed
     */
    NMSEntity createEntity(World world, EntityType entityType, EntityTracker entityTracker);

    /**
     * Creates a falling block entity with the specified block data.
     *
     * @param world         The world where the falling block should be created
     * @param blockData     The block data for the falling block
     * @param entityTracker The entity tracker to use
     * @return The created falling block NMS entity, or null if creation failed
     */
    NMSEntity createFallingBlock(World world, BlockData blockData, EntityTracker entityTracker);

    /**
     * Converts a Bukkit entity to an NMS entity implementation.
     *
     * @param entity The Bukkit entity to convert
     * @return The NMS entity implementation, or null if conversion failed
     */
    NMSEntity entityToNMSEntity(Entity entity);

    /**
     * Gets the version-specific NMS utility implementation.
     *
     * @return the NMS utility instance for this server version
     */
    NMSUtil getNMSUtil();
}
