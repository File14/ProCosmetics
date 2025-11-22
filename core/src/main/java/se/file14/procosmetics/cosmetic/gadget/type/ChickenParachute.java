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
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.MathUtil;

public class ChickenParachute implements GadgetBehavior {

    private static final BlockData BLOCK_DATA = new ItemStack(Material.GRAVEL).getType().createBlockData();
    private static final Vector LEAP_FORCE = new Vector(0.0d, 0.3d, 0.0d);
    private static final Vector UP_FORCE = new Vector(0.0d, 0.1d, 0.0d);

    private static final double PARACHUTE_OFFSET = 7.0d;
    private static final int PARACHUTE_START_HEIGHT = 30;
    private static final double PARACHUTE_SPREAD = 1.5d;
    private static final int CHICKENS = 15;

    private final EntityTracker tracker = new EntityTrackerImpl();
    private boolean parachuting;
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        User user = context.getUser();

        user.setFallDamageProtection(15);

        Location location = player.getLocation().add(0.0d, PARACHUTE_START_HEIGHT, 0.0d);
        player.teleport(location);
        player.setVelocity(new Vector());
        location.add(0.0d, PARACHUTE_OFFSET, 0.0d);
        location.setPitch(0.0f);
        World world = location.getWorld();
        this.location = location.clone();

        for (int i = 0; i < CHICKENS; i++) {
            NMSEntity nmsEntity = context.getPlugin().getNMSManager().createEntity(world, EntityType.CHICKEN, tracker);
            nmsEntity.setLeashHolder(player);
            nmsEntity.setPositionRotation(location.clone().add(
                    MathUtil.randomRange(-PARACHUTE_SPREAD, PARACHUTE_SPREAD),
                    0.0d,
                    MathUtil.randomRange(-PARACHUTE_SPREAD, PARACHUTE_SPREAD)
            ));
        }
        tracker.startTracking();

        if (player.isFlying()) {
            player.setFlying(false);
        }
        player.getWorld().playSound(location, Sound.ENTITY_HORSE_ARMOR, 1.0f, 1.0f);

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> parachuting = true,
                5L
        );
        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (parachuting) {
            Player player = context.getPlayer();

            player.getLocation(location);

            if (!isInAir(location) || player.isFlying()) {
                location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.GRAVEL);
                location.getWorld().spawnParticle(Particle.BLOCK, location.add(0.0d, -0.1d, 0.0d), 0, 0, 0, 0, 0.5d, BLOCK_DATA);
                player.setVelocity(LEAP_FORCE);

                onUnequip(context);
                return;
            }
            if (player.getVelocity().getY() < -0.2d) {
                player.setVelocity(player.getVelocity().add(UP_FORCE));
            }
            location.getWorld().spawnParticle(Particle.BLOCK, location, 0, 0, 0, 0, 0.5d, BLOCK_DATA);
            double y = location.getY();

            for (NMSEntity nmsEntity : tracker.getEntities()) {
                Location chickenLocation = nmsEntity.getPreviousLocation(location);
                chickenLocation.setY(y + PARACHUTE_OFFSET);
                nmsEntity.sendPositionRotationPacket(chickenLocation);
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        tracker.destroy();
        parachuting = false;
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        for (int i = 0; i < PARACHUTE_START_HEIGHT; i++) {
            location.add(0.0d, 1.0d, 0.0d);

            if (!location.getBlock().getType().isAir()) {
                return false;
            }
        }
        location.subtract(0.0d, PARACHUTE_START_HEIGHT * 1.0d, 0.0d);
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }

    private static boolean isInAir(Location location) {
        return location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR;
    }
}
