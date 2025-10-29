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

import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Map;

/**
 * Represents an API interface for accessing immutable structure data.
 */
public interface StructureData {

    /**
     * Retrieves an immutable map of vectors to block data representing the
     * structure's placement.
     *
     * @return an unmodifiable map of {@link Vector} to {@link BlockData}
     */
    Map<Vector, BlockData> getPlacement();

    /**
     * Gets the width of the structure.
     *
     * @return the width of the structure
     */
    double getWidth();

    /**
     * Gets the height of the structure.
     *
     * @return the height of the structure
     */
    double getHeight();

    /**
     * Gets the length of the structure.
     *
     * @return the length of the structure
     */
    double getLength();

    /**
     * Calculates and returns a squared size metric for the structure.
     *
     * @return the squared size of the structure
     */
    double getSizeSquared();

    /**
     * Calculates and returns half of a squared size metric for the structure.
     *
     * @return half of the squared size of the structure
     */
    double getHalfSizeSquared();
}
