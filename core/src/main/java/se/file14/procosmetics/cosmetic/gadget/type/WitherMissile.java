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
package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;

public class WitherMissile implements GadgetBehavior, Listener {

    private WitherSkull witherSkull;
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        despawn();

        Player player = context.getPlayer();
        location = player.getEyeLocation();

        witherSkull = location.getWorld().spawn(location, WitherSkull.class, entity -> {
            entity.setCharged(true);
            entity.setIsIncendiary(false);
            entity.setYield(0.0f);
            entity.setShooter(player);
            entity.setVelocity(location.getDirection());
            MetadataUtil.setCustomEntity(entity);
        });
        witherSkull.addPassenger(player);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (witherSkull != null && witherSkull.isValid()) {
            location = witherSkull.getLocation(location);

            location.getWorld().spawnParticle(Particle.CLOUD, location, 0);
            location.getWorld().playSound(location, Sound.BLOCK_TRIPWIRE_CLICK_ON, 0.2f, 2.0f);

            if (witherSkull.getTicksLived() > context.getType().getDurationInTicks()) {
                explode();
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        despawn();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }

    private void despawn() {
        if (witherSkull != null) {
            witherSkull.remove();
            witherSkull = null;
        }
    }

    @EventHandler
    public void onSkullExplode(ExplosionPrimeEvent event) {
        if (event.getEntity() == witherSkull) {
            event.setCancelled(true);
            explode();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSkullHit(ProjectileHitEvent event) {
        if (event.getEntity() == witherSkull) {
            event.setCancelled(true);
        }
    }

    private void explode() {
        location.getWorld().playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.5f, 1.0f);
        location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1, 0.1d, 0.1d, 0.1d, 0.1d);
        despawn();
    }
}
