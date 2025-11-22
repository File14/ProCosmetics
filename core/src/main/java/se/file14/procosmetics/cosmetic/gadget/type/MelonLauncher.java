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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.ArrayList;
import java.util.List;

public class MelonLauncher implements GadgetBehavior, Listener {

    private static final ItemStack MELON_BLOCK = new ItemStack(Material.MELON);
    private static final ItemStack MELON_SLICE = new ItemStack(Material.MELON_SLICE);

    private static final PotionEffect POTION_EFFECT = new PotionEffect(PotionEffectType.SPEED, 100, 1);

    private static final double EXPLOSION_SPREAD = 0.3d;
    private static final long SLICES_DESPAWN = 20 * 6L;
    private static final long THROWN_ITEM_DESPAWN = 100;

    private static final int PICKUP_DELAY = 200;
    private static final int SLICES = 6;

    private Item item;
    private final List<Item> slices = new ArrayList<>();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        if (item != null && item.isValid()) {
            item.remove();
            item = null;
        }
        Player player = context.getPlayer();
        Location location = player.getEyeLocation();
        item = location.getWorld().dropItem(location, MELON_BLOCK, entity -> {
            entity.setPickupDelay(PICKUP_DELAY);
            entity.setVelocity(location.getDirection());

            MetadataUtil.setCustomEntity(entity);
        });
        player.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 0.8f);

        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (item == null || !item.isValid()) {
            return;
        }

        if (item.isOnGround()) {
            Location location = item.getLocation();
            location.getWorld().playEffect(location, Effect.STEP_SOUND, MELON_BLOCK.getType());

            List<Item> items = new ArrayList<>();

            for (int i = 0; i < SLICES; i++) {
                Item droppedItem = location.getWorld().dropItem(location,
                        new ItemBuilderImpl(MELON_SLICE).setMaxSize(1).getItemStack(),
                        entity -> {
                            entity.setVelocity(new Vector(
                                    MathUtil.randomRange(-EXPLOSION_SPREAD, EXPLOSION_SPREAD),
                                    MathUtil.randomRange(0.2d, EXPLOSION_SPREAD),
                                    MathUtil.randomRange(-EXPLOSION_SPREAD, EXPLOSION_SPREAD)

                            ));
                            MetadataUtil.setCustomEntity(entity);
                        });
                items.add(droppedItem);
                slices.add(droppedItem);
            }
            despawn();

            context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                    () -> despawnSlices(items),
                    SLICES_DESPAWN
            );
        } else {
            if (item.getTicksLived() > THROWN_ITEM_DESPAWN) {
                despawn();
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        despawn();
        despawnSlices(slices);
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
    public void onPlayerPickup(EntityPickupItemEvent event) {
        Item pickupItem = event.getItem();

        if (event.getEntity() instanceof Player pickupPlayer && slices.remove(pickupItem)) {
            pickupPlayer.addPotionEffect(POTION_EFFECT);
            pickupPlayer.playSound(pickupPlayer.getLocation(), Sound.ENTITY_GENERIC_EAT, 0.5f, 1.0f);

            pickupItem.remove();
            event.setCancelled(true);
        }
    }

    private void despawn() {
        if (item != null) {
            item.remove();
            item = null;
        }
    }

    private void despawnSlices(List<Item> items) {
        for (Item slice : items) {
            if (items != slices) {
                slices.remove(slice);
            }
            if (slice.isValid()) {
                Location location = slice.getLocation();
                location.getWorld().spawnParticle(Particle.LARGE_SMOKE, location.add(0.0d, 0.2d, 0.0d), 0);
                slice.remove();
            }
        }
        items.clear();
    }
}
