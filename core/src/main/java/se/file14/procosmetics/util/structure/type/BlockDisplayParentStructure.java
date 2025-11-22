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
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.Structure;

import java.util.Map;

public class BlockDisplayParentStructure extends Structure<NMSEntity> {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private final EntityTracker tracker = new EntityTrackerImpl();

    public BlockDisplayParentStructure(StructureData data) {
        super(data, block -> block.isPassable() && !block.isLiquid());
    }

    public double spawn(Location location, Entity parent, float heightOffset) {
        double angle = calculateAngle(location);
        Matrix4f transformationMatrix = new Matrix4f();

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            NMSEntity nmsFallingBlock = PLUGIN.getNMSManager().createEntity(location.getWorld(), EntityType.BLOCK_DISPLAY, tracker);
            if (nmsFallingBlock.getBukkitEntity() instanceof BlockDisplay blockDisplay) {
                blockDisplay.setBlock(blockData);
                blockDisplay.setTeleportDuration(1);

                transformationMatrix.identity()
                        //.scale(scale)
                        //.rotateY(rotation)
                        .translate((float) vector.getX() - 0.5f, (float) vector.getY() - heightOffset, (float) vector.getZ() - 0.5f);

                blockDisplay.setTransformationMatrix(transformationMatrix);
                parent.addPassenger(nmsFallingBlock.getBukkitEntity());
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
    public double spawn(Location location) {
        return 0;
    }

    @Override
    public void remove() {
        tracker.destroy();
        placedEntries.clear();
    }
}
