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
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

public class PirateShip implements MountBehavior, Listener {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private TNTPrimed tnt;
    private Entity ship;

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (tnt != null) {
            tnt.remove();
            tnt = null;
        }
        entity.setGravity(false);
        entity.setPassenger(context.getPlayer());
        ship = entity;
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (player.getVehicle() == entity) {
            entity.setVelocity(player.getLocation(location).getDirection().multiply(0.3d));
        }
        if (tnt != null) {
            tnt.getLocation(location).add(0.0d, 0.7d, 0.0d);
            tnt.getWorld().spawnParticle(Particle.LARGE_SMOKE,
                    location,
                    1,
                    0.2d,
                    0.0d,
                    0.2d,
                    0.01d
            );
        }
        entity.getLocation(location).add(0.0d, 0.4d, 0.0d);

        location.getWorld().spawnParticle(Particle.CLOUD, location, 0);
        location.getWorld().spawnParticle(Particle.DRIPPING_WATER,
                location,
                2,
                0.4d,
                0.3d,
                0.4d,
                0.01d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        context.getUser().setFallDamageProtection(10);

        if (tnt != null) {
            tnt.remove();
            tnt = null;
        }
        ship = null;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR && tnt == null && player.getVehicle() == ship) {
            player.getLocation(location).add(0.0d, 1.8d, 0.0d);

            tnt = location.getWorld().spawn(location, TNTPrimed.class, entity -> {
                entity.setFuseTicks(80);
                entity.setYield(0.0f);
                MetadataUtil.setCustomEntity(entity);
                // Do not use entity.setSource(player) because then HangingBreakByEntity will change its remover entity
            });
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (entity == tnt) {
            entity.getLocation(location);

            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 0.0f);
            location.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, location, 0);

            for (Player player : MathUtil.getClosestPlayersFromLocation(location, 4.0d)) {
                MathUtil.pushEntity(player, location, MathUtil.randomRange(1.0d, 3.0d), MathUtil.randomRange(1.0d, 2.0d));
                User otherUser = PLUGIN.getUserManager().getConnected(player);

                if (otherUser != null) {
                    otherUser.setFallDamageProtection(5);
                }
            }
            event.setCancelled(true);
            tnt = null;
        }
    }
}
