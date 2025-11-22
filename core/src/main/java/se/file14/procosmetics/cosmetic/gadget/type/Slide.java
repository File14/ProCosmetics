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
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.BlockStructure;

import javax.annotation.Nullable;

public class Slide implements GadgetBehavior, Listener {

    private static final ProCosmetics PLUGIN = ProCosmeticsPlugin.getPlugin();

    private BlockStructure structure;
    private Location center;
    private double angle;
    private Location plate;
    private NMSEntity seat;
    private int ticks;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        center = context.getPlayer().getLocation();

        angle = structure.spawn(center);

        for (Block block : structure.getPlacedEntries()) {
            if (Tag.WOODEN_PRESSURE_PLATES.isTagged(block.getType())) {
                plate = block.getLocation();
                break;
            }
        }
        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (seat != null) {
            if (seat.getBukkitEntity().getPassengers().isEmpty()) {
                seat.getBukkitEntity().remove();
                seat = null;
                return;
            }
            double movementAngle = FastMathUtil.toRadians(ticks) * 4;
            double forward = -Math.abs(Math.sin(movementAngle)) * 4.5d;
            double height = -Math.abs(Math.cos(movementAngle)) * 4.2d;

            Vector vector = MathUtil.rotateAroundAxisY(new Vector(0.0d, height + 3.0d, forward + 6.0d), angle);
            seat.setPositionRotation(center.clone().add(vector));

            ticks++;

            if (ticks == 28) {
                Player player = context.getPlayer();
                player.getWorld().playSound(player, Sound.ENTITY_WITHER_SHOOT, 0.5f, 0.5f);
            }

            if (ticks == 44) {
                Entity passenger = seat.getBukkitEntity().getPassenger();
                seat.getBukkitEntity().remove();

                // Must run 1 tick later. Cannot apply velocity on 1.19.4+ in the same tick as the entity remove
                context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), () -> {
                    if (passenger instanceof Player player && player.isOnline()) {
                        passenger.setVelocity(passenger.getVelocity().add(
                                center.getDirection().multiply(0.5d).add(new Vector(0.0d, 0.4d, 0.0d))));
                    }
                }, 1L);
                seat = null;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();

        if (seat != null) {
            seat.getBukkitEntity().remove();
            seat = null;
        }
        plate = null;
        center = null;
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

    @EventHandler
    public void onPlayerPlateStep(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();

            if (block != null && block.getLocation().equals(plate)) {
                Player player = event.getPlayer();

                if (seat == null && !player.isSneaking()) {
                    ArmorStand seatEntity = center.getWorld().spawn(
                            center.clone().add(0.0d, 4.0d, 0.0d), ArmorStand.class, entity -> {
                                entity.setBasePlate(false);
                                entity.setVisible(false);
                                entity.setGravity(false);

                                MetadataUtil.setCustomEntity(entity);

                                seat = PLUGIN.getNMSManager().entityToNMSEntity(entity);
                                seat.setNoClip(true);
                            });
                    seatEntity.addPassenger(player);
                    ticks = 22;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPlateStep(EntityInteractEvent event) {
        if (event.getBlock().getLocation().equals(plate)) {
            event.setCancelled(true);
        }
    }
}
