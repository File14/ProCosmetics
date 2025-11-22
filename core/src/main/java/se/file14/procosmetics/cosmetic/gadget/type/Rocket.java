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

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.util.structure.type.ParentBlockDisplayStructure;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.ParentBlockDisplayStructureImpl;

import javax.annotation.Nullable;

public class Rocket implements GadgetBehavior {

    private static final double Y_COLLISION_CHECK = 10.0d;

    private static final double MAX_SPEED = 0.5d;
    private static final double ACCELERATION = 0.01d;
    private static final float HEIGHT_OFFSET = 3.5f;

    private ParentBlockDisplayStructure structure;
    private NMSEntity seat;
    private boolean launching;
    private int tick;
    private double speed;
    private Location seatLocation;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new ParentBlockDisplayStructureImpl(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        Location center = player.getLocation();

        seatLocation = center.clone().add(0.0d, HEIGHT_OFFSET, 0.0d);

        Display seatEntity = seatLocation.getWorld().spawn(seatLocation, BlockDisplay.class, entity -> {
            entity.setTeleportDuration(1);
            MetadataUtil.setCustomEntity(entity);
        });
        seatEntity.addPassenger(player);
        structure.spawn(center, seatEntity, HEIGHT_OFFSET);
        seat = context.getPlugin().getNMSManager().entityToNMSEntity(seatEntity);

        context.getUser().setFallDamageProtection((int) (context.getType().getDuration() + 6));

        speed = 0.0d;
        tick = 0;
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (seat == null) {
            return;
        }
        World world = seatLocation.getWorld();

        if (launching) {
            if (speed < MAX_SPEED) {
                speed += ACCELERATION;
            }
            seat.setPositionRotation(seatLocation.add(0.0d, speed, 0.0d));

            // Set this to make sure the tracker updates its location and also prevent a spawn-in flicker
            for (NMSEntity entity : structure.getPlacedEntries()) {
                entity.setPreviousLocation(entity.getPreviousLocation().add(0.0d, speed, 0.0d));
            }
            seatLocation.subtract(0.0d, HEIGHT_OFFSET, 0.0d);
            world.spawnParticle(Particle.FLAME, seatLocation, 10, 0.2f, 0.2f, 0.2f, 0.0d);
            world.spawnParticle(Particle.CLOUD, seatLocation, 10, 0.2f, 0.2f, 0.2f, 0.0d);
            world.spawnParticle(Particle.EXPLOSION, seatLocation, 1, 0.2f, 0.2f, 0.2f, 0.0d);
            seatLocation.add(0.0d, HEIGHT_OFFSET, 0.0d);
            world.playSound(seatLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.8f, 0.0f);

            if (tick > 220 && tick < 240) {
                world.playSound(seatLocation, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4f, 0.0f);
                world.playSound(seatLocation, Sound.BLOCK_ANVIL_LAND, 0.4f, 0.0f);
            } else if (tick == 240) {
                explode(context, seatLocation);
            }
            seatLocation.add(0.0d, Y_COLLISION_CHECK, 0.0d);
            Material topMaterial = seatLocation.getBlock().getType();

            if (!topMaterial.isAir()) {
                explode(context, seatLocation);
                return;
            }
            seatLocation.subtract(0.0d, Y_COLLISION_CHECK, 0.0d);
        } else {
            world.playSound(seatLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.1f, 0.0f);

            if (tick % 20 == 0) {
                seatLocation.subtract(0.0d, HEIGHT_OFFSET, 0.0d);
                world.spawnParticle(Particle.CLOUD, seatLocation, 10, 0.2f, 0.2f, 0.2f, 0.0d);
                seatLocation.add(0.0d, HEIGHT_OFFSET, 0.0d);

                if (tick == 100) {
                    launching = true;
                    world.playSound(seatLocation, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.0f);
                    world.playSound(seatLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.0f);
                }
            }
        }
        tick++;
    }

    private void explode(CosmeticContext<GadgetType> context, Location location) {
        onUnequip(context);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.0f);
        location.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, location, 0);
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        launching = false;

        if (seat != null) {
            seat.getBukkitEntity().remove();
            seat = null;
        }
    }

    @Override
    public boolean requiresGroundOnUse() {
        return true;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return structure.isEnoughSpace(location);
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}
