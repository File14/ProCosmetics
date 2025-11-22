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

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.api.util.structure.type.BlockStructure;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.StructureImpl;

import java.util.Map;

public class BlockStructureImpl extends StructureImpl<Block> implements BlockStructure {

    public BlockStructureImpl(StructureData data) {
        super(data, block -> block.getType().isAir());
    }

    @Override
    public double spawn(Location location) {
        return spawn(location, false);
    }

    public double spawn(Location location, boolean blockBreakEffect) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            location.add(vector);
            Block block = location.getBlock();
            block.setBlockData(blockData, false);

            if (blockBreakEffect) {
                location.getWorld().playEffect(location, Effect.STEP_SOUND, block.getType());
            }
            location.subtract(vector);
            MetadataUtil.setCustomBlock(block);
            placedEntries.add(block);
        }
        return angle;
    }

    @Override
    public void remove() {
        for (Block block : placedEntries) {
            block.setType(Material.AIR, false);
            MetadataUtil.removeCustomBlock(block);
        }
        placedEntries.clear();
    }
}
