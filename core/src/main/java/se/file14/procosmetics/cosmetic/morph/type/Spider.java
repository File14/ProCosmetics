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

public class Spider implements MorphBehavior {

    private static final ItemStack ITEMSTACK = new ItemStack(Material.COBWEB);
    private static final Vector UP_FORCE = new Vector(0.0d, 0.35d, 0.0d);
    private static final PotionEffect POTION_EFFECT = new PotionEffect(PotionEffectType.SLOWNESS, 80, 2);

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private final List<Item> items = new ArrayList<>();
    private int tick;
    private boolean activated;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {

    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            Player player = context.getPlayer();

            context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), this::clearItems, 140L);
            player.playSound(player, Sound.ENTITY_SPIDER_HURT, 1.0f, 1.0f);

            activated = true;
            tick = 0;

            return InteractionResult.SUCCESS;
        }
        return MorphBehavior.InteractionResult.NO_ACTION;
    }


    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (activated) {
            Iterator<Item> iterator = items.iterator();

            while (iterator.hasNext()) {
                Item item = iterator.next();
                item.getLocation(location);

                if (item.isOnGround()) {
                    location.getWorld().spawnParticle(Particle.ITEM,
                            item.getLocation().add(0.0d, 0.3d, 0.0d),
                            5,
                            0.3d,
                            0.1d,
                            0.3d,
                            0.1d,
                            ITEMSTACK
                    );
                    item.remove();
                    iterator.remove();
                } else {
                    Player hitPlayer = MathUtil.getClosestPlayerFromLocation(location, 1.0d);

                    if (hitPlayer != null && hitPlayer != player) {
                        hitPlayer.addPotionEffect(POTION_EFFECT);
                        MathUtil.pushEntity(hitPlayer, location, 0.05d, 0.0d);

                        location.getWorld().spawnParticle(Particle.ITEM,
                                hitPlayer.getLocation().add(0.0d, 1.2d, 0.0d),
                                20,
                                0.3d,
                                0.3d,
                                0.3d,
                                0.1d,
                                ITEMSTACK
                        );
                        item.remove();
                        iterator.remove();
                    }
                }
            }

            if (tick++ < 50) {
                player.getLocation(location);

                items.add(location.getWorld().dropItem(
                        location.add(0.0d, 0.2d, 0.0d),
                        new ItemBuilderImpl(ITEMSTACK).setDisplayName(String.valueOf(tick)).getItemStack(), item -> {
                            Vector vector = location.getDirection().multiply(1.3d);
                            item.setVelocity(vector.add(UP_FORCE));
                            item.setPickupDelay(Integer.MAX_VALUE);
                            MetadataUtil.setCustomEntity(item);
                        }
                ));
                player.getWorld().playSound(player, Sound.BLOCK_COBWEB_HIT, 1.0f, 1.0f);
            }
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
