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
package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;

import java.util.List;

public class FrostySnowman extends BlockTrailBehavior {

    private static final List<ItemStack> BLOCKS = List.of(
            new ItemStack(Material.PACKED_ICE),
            new ItemStack(Material.SNOW_BLOCK)
    );

    private int ticks;

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        // Custom controls
        if (context.getPlayer().getVehicle() == entity) {
            nmsEntity.moveRide(context.getPlayer());
        }

        if (ticks % 5 == 0) {
            entity.getLocation(location).add(0.0d, 1.0d, 0.0d);
            location.getWorld().spawnParticle(Particle.SNOWFLAKE, location, 8, 0.1d, 0.1d, 0.1d, 0.0d);
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public int getBlockTrailRadius() {
        return 1;
    }

    @Override
    public List<ItemStack> getTrailBlocks() {
        return BLOCKS;
    }
}
