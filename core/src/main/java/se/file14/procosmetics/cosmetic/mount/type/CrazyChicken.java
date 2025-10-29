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

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

public class CrazyChicken implements MountBehavior {

    private static final PotionEffect POTION_EFFECT = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false);
    private static final double ITEM_SPREAD = 0.5d;
    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addPotionEffect(POTION_EFFECT);
        }
        nmsEntity.setRideSpeed(0.3f);
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        // Custom controls
        if (context.getPlayer().getVehicle() == entity) {
            nmsEntity.moveRide(context.getPlayer());
        }

        if (ticks % 5 == 0) {
            if (isTossItemsEnabled(context)) {
                entity.getLocation(location);

                NMSEntity itemEntity = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
                itemEntity.setPositionRotation(location);
                itemEntity.setEntityItemStack(new ItemStack(Material.FEATHER));
                itemEntity.setVelocity(
                        MathUtil.randomRange(-ITEM_SPREAD, ITEM_SPREAD),
                        MathUtil.randomRange(-0.1d, ITEM_SPREAD),
                        MathUtil.randomRange(-ITEM_SPREAD, ITEM_SPREAD)
                );
                itemEntity.getTracker().startTracking();
                itemEntity.getTracker().destroyAfter(30);
            }
            location.getWorld().spawnParticle(Particle.ENTITY_EFFECT, location.add(
                            MathUtil.randomRange(-0.5d, 1.0d),
                            MathUtil.randomRange(-0.2d, 1.0d),
                            MathUtil.randomRange(-0.5d, 1.0d)
                    ), 5, Color.fromBGR(MathUtil.randomRangeInt(0, 255),
                            MathUtil.randomRangeInt(0, 255),
                            MathUtil.randomRangeInt(0, 255))
            );
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
    }
}
