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
package se.file14.procosmetics.api.util.structure;

import org.bukkit.Location;

import java.util.List;

/**
 * Represents a structure that can be spawned and removed in the world.
 *
 * @param <T> the type of entries placed by this structure
 */
public interface Structure<T> {

    /**
     * Checks if there is enough space to spawn this structure at the given location.
     *
     * @param location the location to check
     * @return true if there is enough space, false otherwise
     */
    boolean isEnoughSpace(Location location);

    /**
     * Spawns the structure at the given location.
     *
     * @param location the location to spawn the structure
     * @return the angle used for rotation during spawning
     */
    double spawn(Location location);

    /**
     * Removes the structure from the world.
     */
    void remove();

    /**
     * Gets the structure data defining this structure.
     *
     * @return the structure data
     */
    StructureData getData();

    /**
     * Gets the list of entries that have been placed by this structure.
     *
     * @return the list of placed entries
     */
    List<T> getPlacedEntries();
}
