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
package se.file14.procosmetics.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import se.file14.procosmetics.util.MetadataUtil;

public class EntityListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockForm(EntityBlockFormEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCombust(EntityCombustEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPortal(EntityPortalEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity()) || MetadataUtil.isCustomEntity(event.getTarget())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity()) || MetadataUtil.isCustomEntity(event.getTarget())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTransform(EntityTransformEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity()) || MetadataUtil.isCustomEntity(event.getDamager())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())
                || event.getRemover() != null && MetadataUtil.isCustomEntity(event.getRemover())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity enteredEntity = event.getEntered();

        if (MetadataUtil.isCustomEntity(event.getVehicle()) && !(enteredEntity instanceof HumanEntity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (MetadataUtil.isCustomEntity(event.getVehicle())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (MetadataUtil.isCustomEntity(event.getVehicle())
                || event.getAttacker() != null && MetadataUtil.isCustomEntity(event.getAttacker())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVillagerCareerChange(VillagerCareerChangeEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFish(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        Entity hitEntity = event.getHitEntity();

        if (entity instanceof org.bukkit.entity.FishHook && hitEntity != null && MetadataUtil.isCustomEntity(hitEntity)) {
            entity.remove();
        }
    }

    @EventHandler
    public void onBucketPickupEntity(PlayerBucketEntityEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }
}
