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
package se.file14.procosmetics.util.structure;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class Structure<T> {

    protected final StructureData data;
    protected final List<T> placedEntries = new ArrayList<>();
    protected Predicate<Block> blockCheckPredicate;

    public Structure(StructureData data, Predicate<Block> blockCheckPredicate) {
        this.data = data;
        this.blockCheckPredicate = blockCheckPredicate;
    }

    public boolean isEnoughSpace(Location location) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);

            location.add(vector);
            Block block = location.getBlock();
            location.subtract(vector);

            if (!blockCheckPredicate.test(block)) {
                return false;
            }
        }
        return true;
    }

    protected double calculateAngle(Location location) {
        LocationUtil.center(location);

        float yaw = 90.0f * (Math.round(location.getYaw() / 90.0f) & 0x3);
        location.setYaw(yaw);
        location.setPitch(0.0f);

        return -FastMathUtil.toRadians(location.getYaw());
    }

    protected void rotate(BlockData blockData, double angle) {
        int numericAngle = (int) angle;

        if (blockData instanceof Directional directional) {
            BlockFace originalFace = directional.getFacing();
            BlockFace newFace = rotateBlockFace(originalFace, numericAngle);

            if (originalFace != newFace) {
                directional.setFacing(newFace);
            }
        }
        if (blockData instanceof MultipleFacing multipleFacing) {
            for (BlockFace originalFace : multipleFacing.getFaces()) {
                BlockFace newFace = rotateBlockFace(originalFace, numericAngle);

                if (originalFace != newFace) {
                    multipleFacing.setFace(originalFace, false);
                    multipleFacing.setFace(newFace, true);
                }
            }
        }
    }

    private BlockFace rotateBlockFace(BlockFace original, int degrees) {
        int numRotations = (degrees / 90 + 4) % 4; // Ensure non-negative result
        for (int i = 0; i < numRotations; i++) {
            original = switch (original) {
                case NORTH -> BlockFace.EAST;
                case EAST -> BlockFace.SOUTH;
                case SOUTH -> BlockFace.WEST;
                case WEST -> BlockFace.NORTH;
                default -> original; // Does not rotate if it's not one of the main directions
            };
        }
        return original;
    }

    public abstract double spawn(Location location);

    public abstract void remove();

    public StructureData getData() {
        return data;
    }

    public List<T> getPlacedEntries() {
        return placedEntries;
    }
}
