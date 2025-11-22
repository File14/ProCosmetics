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
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MetadataUtil;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Cowboy implements GadgetBehavior, Listener {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private Entity armorStand;
    private UUID lastClicked;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        Player clickedPlayer = null;

        if (lastClicked != null) {
            clickedPlayer = context.getPlugin().getJavaPlugin().getServer().getPlayer(lastClicked);
        }

        if (clickedPlayer == null) {
            return InteractionResult.fail();
        }
        Location location = clickedPlayer.getLocation().add(0.0d, 2.0d, 0.0d);
        armorStand = location.getWorld().spawn(clickedPlayer.getLocation(), ArmorStand.class, entity -> {
            entity.setSmall(true);
            entity.setVisible(false);
            entity.setGravity(false);
            entity.setCollidable(false);

            MetadataUtil.setCustomEntity(entity);
        });
        clickedPlayer.addPassenger(armorStand);
        armorStand.addPassenger(player);
        player.playSound(location, Sound.ENTITY_HORSE_ARMOR, 0.5f, 1.2f);

        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        if (armorStand != null) {
            despawnArmorStand();
        }
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
        return false;
    }

    @EventHandler
    public void onLeftClick22(PlayerInteractAtEntityEvent event) {

    }

    @EventHandler
    public void onClickedPlayer(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player player) {
            lastClicked = player.getUniqueId();
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Action action = event.getAction();

        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && armorStand != null) {
            Player player = event.getPlayer();
            Location location = player.getLocation();

            if (player.getPassengers().contains(armorStand)) {
                for (Entity passenger : armorStand.getPassengers()) {
                    passenger.eject();
                    despawnArmorStand();

                    passenger.setVelocity(location.getDirection().multiply(2.0d));

                    if (passenger instanceof Player passengerPlayer) {
                        User user = PLUGIN.getUserManager().getConnected(passengerPlayer);

                        if (user != null) {
                            user.setFallDamageProtection(8);
                        }
                    }
                    player.getWorld().playSound(player, Sound.ENTITY_WITHER_SHOOT, 0.4f, 1.5f);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getDismounted() == armorStand) {
            despawnArmorStand();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (armorStand != null && armorStand.getPassengers().contains(event.getPlayer())) {
            despawnArmorStand();
        }
    }

    private void despawnArmorStand() {
        armorStand.remove();
        armorStand = null;
    }
}
