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
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class GlacialSteed extends BlockTrailBehavior {

    private static final List<ItemStack> BLOCKS = List.of(new ItemStack(Material.SNOW_BLOCK));
    private static final ItemStack SNOWBALL_ITEM = new ItemStack(Material.SNOWBALL);
    private static final double ITEM_SPREAD = 0.5d;

    private int ticks;

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.setupEntity(context, entity, nmsEntity);

        if (entity instanceof Horse horse) {
            horse.setJumpStrength(1.0d);
            horse.setAdult();
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            horse.setColor(Horse.Color.WHITE);

            horse.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.3d);
            horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        if (ticks % 10 == 0) {
            entity.getLocation(location).add(0.0d, 1.0d, 0.0d);
            location.getWorld().spawnParticle(Particle.SNOWFLAKE, location, 15, 0.1d, 0.1d, 0.1d, 0.0d);

            if (isTossItemsEnabled(context)) {
                NMSEntity itemEntity = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
                itemEntity.setPositionRotation(location);
                itemEntity.setEntityItemStack(SNOWBALL_ITEM);
                itemEntity.setVelocity(
                        MathUtil.randomRange(-ITEM_SPREAD, ITEM_SPREAD),
                        MathUtil.randomRange(-0.1d, ITEM_SPREAD),
                        MathUtil.randomRange(-ITEM_SPREAD, ITEM_SPREAD)
                );
                itemEntity.getTracker().startTracking();
                itemEntity.getTracker().destroyAfter(30);
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
