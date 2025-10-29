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

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

public class Slime implements MountBehavior {

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private int ticks;

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
        context.getPlayer().getLocation(location);
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (entity instanceof org.bukkit.entity.Slime slime) {
            slime.setSize(3);
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        // Custom controls
        if (player.getVehicle() == entity) {
            nmsEntity.moveRide(player);
        }

        if (context.getUser().isMoving() && player.getVehicle() == entity && entity.isOnGround()) {
            if (ticks % 30 == 0) {
                Vector vector = entity.getVelocity();
                vector.setY(vector.getY() + 0.8d);
                entity.setVelocity(vector);
            }

            if (ticks % 20 == 0) {
                for (Player hitPlayer : MathUtil.getClosestPlayersFromLocation(entity.getLocation(location), 2.5d)) {
                    if (hitPlayer != player && hitPlayer.isValid() && hitPlayer.isOnGround()) {
                        Vector vector = hitPlayer.getVelocity();
                        vector.setY(vector.getY() + 0.6d);
                        hitPlayer.setVelocity(vector);

                        player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_SLIME_HURT, 0.5f, 1.0f);
                        hitPlayer.getWorld().spawnParticle(Particle.ITEM_SLIME, hitPlayer.getLocation(), 30, 0.0d, 0.0d, 0.0d, 0.0d);
                    }
                }
            }
        }

        if (ticks % 10 == 0) {
            entity.getLocation(location);
            location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 5, 1.0d, 1.2d, 1.0d, 0.1d);
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
    }
}
