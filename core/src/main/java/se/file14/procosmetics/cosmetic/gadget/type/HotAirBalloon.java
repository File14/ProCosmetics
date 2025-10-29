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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.FallingBlocksStructure;

import javax.annotation.Nullable;

public class HotAirBalloon implements GadgetBehavior {

    private static final double MAX_SPEED = 0.075d;
    private static final double ACCELERATION = 0.001d;

    private FallingBlocksStructure structure;
    private Location center;
    private Location seatLocation;
    private NMSEntity seat;
    private double speed = 0.0d;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new FallingBlocksStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        center = player.getLocation();

        structure.spawn(center);

        seatLocation = center.clone().add(0.0d, 0.5d, 0.0d);

        ArmorStand seatEntity = seatLocation.getWorld().spawn(seatLocation, ArmorStand.class, entity -> {
            entity.setSmall(true);
            entity.setGravity(false);
            entity.setVisible(false);

            MetadataUtil.setCustomEntity(entity);
        });
        seatEntity.addPassenger(player);
        seat = context.getPlugin().getNMSManager().entityToNMSEntity(seatEntity);

        seatEntity.getWorld().playSound(seatLocation, Sound.ENTITY_WITHER_SHOOT, 0.3f, 0.5f);

        context.getUser().setFallDamageProtection((int) context.getType().getDuration());

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    private void addY(Location location, double amount) {
        location.setY(location.getY() + amount);
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (seat == null) {
            return;
        }
        if (speed < MAX_SPEED) {
            speed += ACCELERATION;
        }
        addY(seatLocation, speed);
        seat.setPositionRotation(seatLocation);

        for (NMSEntity entity : structure.getPlacedEntries()) {
            addY(entity.getPreviousLocation(), speed);
            entity.setPositionRotation(entity.getPreviousLocation());
            entity.setVelocity(0.0d, speed, 0.0d);
            entity.sendVelocityPacket();
        }
        seatLocation.add(0.0d, 3.0d, 0.0d);
        seatLocation.getWorld().spawnParticle(Particle.LARGE_SMOKE,
                seatLocation,
                0,
                0.0d,
                speed,
                0.0d,
                2.0d
        );
        seatLocation.subtract(0.0d, 3.0d, 0.0d);
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        context.getUser().setFallDamageProtection(6);

        if (center == null) {
            return;
        }
        seatLocation.getWorld().playSound(seatLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.3f, 1.0f);
        seatLocation.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, seatLocation.add(0.0d, 5.0d, 0.0d), 1);

        if (seat != null) {
            seat.getBukkitEntity().remove();
            seat = null;
        }
        speed = 0.0d;
        center = null;
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
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
