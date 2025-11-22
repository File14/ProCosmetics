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

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.material.Materials;

import java.util.HashSet;
import java.util.Set;

public class Paintball implements GadgetBehavior, Listener {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private static final int PAINT_RANGE = 2;
    private static final int PAINT_DURATION = 5;
    private static final double PAINT_PARTICLE_CHANCE = 0.6d;

    private final Set<Projectile> balls = new HashSet<>();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        Location location = player.getEyeLocation();

        balls.add(location.getWorld().spawn(location, Snowball.class, entity -> {
            entity.setVelocity(location.getDirection().multiply(2.0d));
            entity.setShooter(player);
            entity.setItem(Materials.getRandomWoolItem());

            MetadataUtil.setCustomEntity(entity);
        }));
        location.getWorld().playSound(location, Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        for (Entity entity : balls) {
            entity.remove();
        }
        balls.clear();
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
    public void onBallHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball snowball && balls.remove(snowball)) {
            Location location = snowball.getLocation().add(snowball.getVelocity());
            snowball.getWorld().playSound(location, Sound.BLOCK_STONE_BREAK, 1.0f, 0.0f);
            ItemStack itemStack = snowball.getItem();

            for (Block block : MathUtil.getIn3DRadius(location, PAINT_RANGE)) {
                if (PLUGIN.getBlockRestoreManager().setFakeBlock(block, itemStack, true, PAINT_DURATION)) {
                    if (MathUtil.THREAD_LOCAL_RANDOM.nextDouble() < PAINT_PARTICLE_CHANCE) {
                        location.getWorld().playEffect(block.getLocation(location), Effect.STEP_SOUND, itemStack.getType());
                    }
                }
            }
            snowball.remove();
            event.setCancelled(true);
        }
    }
}
