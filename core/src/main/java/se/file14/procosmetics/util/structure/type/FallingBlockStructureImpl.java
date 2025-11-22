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
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.api.util.structure.type.FallingBlockStructure;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.StructureImpl;

import java.util.Map;

public class FallingBlockStructureImpl extends StructureImpl<NMSEntity> implements FallingBlockStructure {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private final EntityTracker tracker = new EntityTrackerImpl();

    public FallingBlockStructureImpl(StructureData data) {
        super(data, block -> block.isPassable() && !block.isLiquid());
    }

    @Override
    public double spawn(Location location) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            NMSEntity nmsFallingBlock = PLUGIN.getNMSManager().createFallingBlock(location.getWorld(), blockData, tracker);
            if (nmsFallingBlock.getBukkitEntity() instanceof FallingBlock fallingBlock) {
                fallingBlock.setGravity(false);
            }
            location.add(vector);
            nmsFallingBlock.setPositionRotation(location);
            location.subtract(vector);

            placedEntries.add(nmsFallingBlock);
        }
        tracker.startTracking();
        return angle;
    }

    @Override
    public void remove() {
        tracker.destroy();
        placedEntries.clear();
    }
}
