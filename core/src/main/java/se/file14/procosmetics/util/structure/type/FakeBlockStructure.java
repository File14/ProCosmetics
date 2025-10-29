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
package se.file14.procosmetics.util.structure.type;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.Structure;

import java.util.Map;

public class FakeBlockStructure extends Structure<Block> {

    private static final double RANGE = 64.0d * 64.0d;

    public FakeBlockStructure(StructureData data) {
        super(data, block -> block.getType().isAir());
    }

    @Override
    public double spawn(Location location) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            location.add(vector);
            Block block = location.getBlock();
            location.subtract(vector);

            for (Player player : block.getWorld().getPlayers()) {
                if (player.getLocation().distanceSquared(location) < RANGE) {
                    player.sendBlockChange(location, blockData);
                }
            }
            MetadataUtil.setCustomBlock(block);
            placedEntries.add(block);
        }
        return angle;
    }

    @Override
    public void remove() {
        for (Block block : placedEntries) {
            Location location = block.getLocation();
            BlockData blockData = block.getBlockData();

            for (Player player : block.getWorld().getPlayers()) {
                player.sendBlockChange(location, blockData);
            }
            MetadataUtil.removeCustomBlock(block);
        }
        placedEntries.clear();
    }
}
