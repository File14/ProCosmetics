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
import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class EtherealDragon implements MountBehavior {

    private static final double SPEED_MULTIPLIER = 0.6d;

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        nmsEntity.setNoClip(false);
        nmsEntity.setHurtTicks(-1);

        entity.addPassenger(context.getPlayer());
        entity.setSilent(true);
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (entity.getPassengers().isEmpty()) {
            context.getUser().removeCosmetic(context.getType().getCategory(), false, true);
            return;
        }
        context.getPlayer().getLocation(location);
        nmsEntity.move(location.getDirection().multiply(SPEED_MULTIPLIER));
        nmsEntity.setYaw(location.getYaw() - 180.0f);
        nmsEntity.setPitch(location.getPitch());

        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(location), 70, 2.0d, 2.0d, 2.0d, 0.5d);
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        context.getUser().setFallDamageProtection(10);
    }
}
