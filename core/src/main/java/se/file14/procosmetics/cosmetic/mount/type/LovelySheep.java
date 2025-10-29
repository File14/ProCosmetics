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

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;

import java.util.List;

public class LovelySheep extends BlockTrailBehavior {

    private static final List<ItemStack> BLOCKS = List.of(
            new ItemStack(Material.PINK_TERRACOTTA),
            new ItemStack(Material.RED_TERRACOTTA),
            new ItemStack(Material.MAGENTA_TERRACOTTA)
    );

    private int ticks;

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.setupEntity(context, entity, nmsEntity);

        if (entity instanceof Sheep sheep) {
            sheep.setColor(DyeColor.RED);
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        // Custom controls
        if (context.getPlayer().getVehicle() == entity) {
            nmsEntity.moveRide(context.getPlayer());
        }

        if (ticks % 10 == 0) {
            entity.getLocation(location);

            for (int i = 0; i < 3; i++) {
                location.getWorld().spawnParticle(Particle.HEART, location, 1, 0.5d, 1.0d, 0.5d, 0.3d);
                location.getWorld().spawnParticle(Particle.FIREWORK, location, 1, 0.8d, 2.0d, 0.8d, 0.2d);
            }
            if (entity instanceof Sheep sheep) {
                switch (sheep.getColor()) {
                    case RED: {
                        sheep.setColor(DyeColor.PURPLE);
                        break;
                    }
                    case PURPLE: {
                        sheep.setColor(DyeColor.MAGENTA);
                    }
                    default: {
                        sheep.setColor(DyeColor.RED);
                    }
                }
            }
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
