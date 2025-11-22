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
package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Squid implements MorphBehavior {

    private final static ItemStack ITEMSTACK = new ItemStack(Material.INK_SAC);
    private static final Vector UP_FORCE = new Vector(0.0d, 0.35d, 0.0d);
    private static final PotionEffect POTION_EFFECT = new PotionEffect(PotionEffectType.BLINDNESS, 60, 0);
    private final List<Item> items = new ArrayList<>();
    private int ticks;
    private boolean shooting;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            ticks = 0;
            shooting = true;
            Player player = context.getPlayer();
            player.getWorld().playSound(player, Sound.ENTITY_SQUID_SQUIRT, 1.0f, 2.0f);
            context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), this::clearItems, 140L);
            return InteractionResult.success();
        }
        return InteractionResult.noAction();
    }

    private final Location itemLocation = new Location(null, 0.0d, 0.0d, 0.0d);

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Iterator<Item> iterator = items.iterator();

        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item.isOnGround()) {
                item.remove();
                iterator.remove();
            } else {
                item.getLocation(itemLocation);
                itemLocation.getWorld().spawnParticle(Particle.SPLASH, itemLocation, 0);

                Player hitPlayer = MathUtil.getClosestPlayerFromLocation(itemLocation, 1.0d);
                Player player = context.getPlayer();

                if (hitPlayer != null && hitPlayer != player) {
                    hitPlayer.addPotionEffect(POTION_EFFECT);
                    hitPlayer.getWorld().spawnParticle(Particle.SPLASH,
                            hitPlayer.getEyeLocation(),
                            10,
                            0.3d,
                            0.4d,
                            0.3d,
                            0.1d
                    );
                    MathUtil.pushEntity(hitPlayer, itemLocation, 0.05d, 0.0d);

                    item.remove();
                    iterator.remove();
                }
            }
        }

        if (shooting) {
            if (ticks++ > 50) {
                shooting = false;
            }
            Player player = context.getPlayer();
            player.getLocation(itemLocation);

            items.add(player.getWorld().dropItem(
                    itemLocation.add(0.0d, 0.2d, 0.0d),
                    new ItemBuilderImpl(ITEMSTACK).setMaxSize(1).getItemStack(),
                    item -> {
                        Vector vector = itemLocation.getDirection().multiply(1.3d);
                        item.setVelocity(vector.add(UP_FORCE));
                        item.setPickupDelay(Integer.MAX_VALUE);
                        MetadataUtil.setCustomEntity(item);
                    }
            ));

            if (ticks % 5 == 0) {
                player.getWorld().playSound(player, Sound.BLOCK_WATER_AMBIENT, 1.0f, 2.0f);
            }
            itemLocation.getWorld().spawnParticle(Particle.SPLASH,
                    itemLocation,
                    10,
                    0.3d,
                    0.3d,
                    0.3d,
                    0.1d
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        clearItems();
    }

    private void clearItems() {
        for (Item item : items) {
            item.remove();
        }
        items.clear();
    }
}
