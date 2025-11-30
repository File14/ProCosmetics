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

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.material.Materials;

import java.util.ArrayList;
import java.util.List;

public class HypeTrain implements MountBehavior, Listener {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private static final BlockData BLOCK_DATA = new ItemStack(Material.DIAMOND_BLOCK).getType().createBlockData();

    private final Object2ObjectArrayMap<Player, TrainData> carriagesByPlayers = new Object2ObjectArrayMap<>();
    private final List<BlockDisplay> carriages = new ArrayList<>();
    private int ticks;

    private static Vector getTrajectory2d(Location from, Location to) {
        return to.clone().subtract(from).toVector().normalize();
    }

    private Player player;
    private Entity train;

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
        player = context.getPlayer();
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        nmsEntity.setNoClip(true);
        Player player = context.getPlayer();

        // Move up location on spawn to prevent despawn
        nmsEntity.setPositionRotation(entity.getLocation().add(0.0d, 1.5d, 0.0d));

        if (entity instanceof BlockDisplay blockDisplay) {
            setProperties(blockDisplay, BLOCK_DATA);
            entity.addPassenger(player);

            carriagesByPlayers.put(player, new TrainData(nmsEntity, player));
            carriages.add(blockDisplay);
        }
        train = entity;
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        Player player = context.getPlayer();
        Location before = null;
        Location location = entity.getLocation().subtract(0.d, 0.5d, 0.d);

        if (location.getBlock().getType().isSolid()) {
            entity.eject();
            Vector vector = player.getLocation(location).getDirection().multiply(0.3d);
            player.teleport(location.subtract(vector));
            player.getWorld().playSound(location, Sound.ENTITY_CHICKEN_EGG, 0.8f, 1.0f);
            return;
        }

        // TODO: Can this be optimized? to avoid .clone()
        for (TrainData trainData : carriagesByPlayers.values()) {
            NMSEntity blockNMSEntity = trainData.nmsEntity();
            Entity blockEntity = blockNMSEntity.getBukkitEntity();
            Player rider = trainData.player;
            Location tail = blockEntity.getLocation();

            if (before != null) {
                Location tp = before.clone().add(getTrajectory2d(before, tail));
                tp.setPitch(tail.getPitch());
                tp.setYaw(tail.getYaw());
                tp.setDirection(before.getDirection());

                blockNMSEntity.setPositionRotation(tp);
            } else {
                Vector forward = rider.getLocation().getDirection();
                tail.setDirection(forward);
                blockNMSEntity.setPositionRotation(tail.add(forward.multiply(0.3d)));
            }
            before = tail;

            if (ticks % 20 == 0 && rider.isOnline()) {
                User user = context.getPlugin().getUserManager().getConnected(rider);

                if (user != null) {
                    user.sendActionBar(Component.text(context.getType().getName(user) + ": " + carriagesByPlayers.size(), NamedTextColor.YELLOW));
                }
            }
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        context.getUser().setFallDamageProtection(10);
        clearTrain();
    }

    @EventHandler
    public void onClickedCarriage(PlayerInteractAtEntityEvent event) {
        Player clicker = event.getPlayer();

        if (clicker.isInsideVehicle() || clicker.isSneaking()) {
            return;
        }
        Entity clickedEntity = event.getRightClicked();

        // Spawn a new carriage if they click the driver
        if (clickedEntity instanceof Player && clickedEntity == player) {
            spawnCarriage(clicker);
        }

        // Spawn a new carriage if they click an existing one
        if (clickedEntity instanceof BlockDisplay blockDisplay && carriages.contains(blockDisplay)) {
            spawnCarriage(clicker);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        Entity dismounted = event.getDismounted();

        if (dismounted == train) {
            User user = PLUGIN.getUserManager().getConnected(player);

            if (user != null) {
                user.removeCosmetic(PLUGIN.getCategoryRegistries().mounts(), false, true);
            }
            return;
        }
        if (event.getEntity() instanceof Player rider) {
            dismountTrainWagon(rider, dismounted);
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        dismountTrainWagon(player, player.getVehicle());
    }

    private void spawnCarriage(Player target) {
        Location tail = player.getLocation();
        int carriageAmount = carriages.size();

        if (carriageAmount > 1) {
            tail = carriages.get(carriageAmount - 1).getLocation();
        }

        if (carriageAmount > 2) {
            tail.add(getTrajectory2d(
                    carriages.get(carriageAmount - 2).getLocation(),
                    carriages.get(carriageAmount - 1).getLocation()
            ));
        } else {
            tail.subtract(tail.getDirection().setY(0));
        }
        BlockDisplay blockDisplay = tail.getWorld().spawn(tail, BlockDisplay.class, entity
                -> setProperties(entity, Materials.getRandomWoolItem().getType().createBlockData()));
        blockDisplay.getWorld().playSound(blockDisplay, Sound.ENTITY_HORSE_SADDLE, 0.5f, 1.0f);
        blockDisplay.addPassenger(target);

        NMSEntity nmsEntity = PLUGIN.getNMSManager().entityToNMSEntity(blockDisplay);
        carriages.add(blockDisplay);
        carriagesByPlayers.put(target, new TrainData(nmsEntity, target));
    }

    private void setProperties(BlockDisplay blockDisplay, BlockData blockData) {
        blockDisplay.setBlock(blockData);
        blockDisplay.setTeleportDuration(2);
        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix.identity()
                .scale(0.6f)
                //.rotateY(DEGREES_180)
                .translate(-0.5f, -1.0f, -0.5f);
        blockDisplay.setTransformationMatrix(transformationMatrix);
        MetadataUtil.setCustomEntity(blockDisplay);
    }

    private void dismountTrainWagon(Player rider, Entity vehicle) {
        if (vehicle instanceof BlockDisplay blockDisplay && rider != player) {
            TrainData trainData = carriagesByPlayers.get(rider);

            if (trainData != null) {
                carriages.remove(blockDisplay);
                carriagesByPlayers.remove(rider);

                vehicle.eject();
                vehicle.remove();
            }
        }
    }

    public void clearTrain() {
        for (BlockDisplay blockDisplay : new ArrayList<>(carriages)) {
            blockDisplay.getWorld().spawnParticle(Particle.CLOUD, blockDisplay.getLocation(), 3, 0.0, 0.0, 0.0, 0.0);
            blockDisplay.remove();
        }
        carriagesByPlayers.clear();
        carriages.clear();
    }

    private record TrainData(NMSEntity nmsEntity, Player player) {
    }
}
