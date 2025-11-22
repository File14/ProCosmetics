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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TNT implements GadgetBehavior, Listener {

    private static final ProCosmetics PLUGIN = ProCosmeticsPlugin.getPlugin();

    private static final int FUSE_TICKS = 80;

    private final Set<Entity> entities = new HashSet<>();
    private final Set<FallingBlock> fallingBlocks = new HashSet<>();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        Location location = player.getEyeLocation().add(0.0d, 0.8d, 0.0d);

        entities.add(location.getWorld().spawn(location, TNTPrimed.class, entity -> {
            entity.setVelocity(location.getDirection().multiply(0.8d));
            entity.setFuseTicks(FUSE_TICKS);
            // Do not use entity.setSource(player) because then HangingBreakByEntity will change its remover entity

            MetadataUtil.setCustomEntity(entity);
        }));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        for (Entity entity : entities) {
            entity.remove();
        }
        entities.clear();

        for (FallingBlock fallingBlock : fallingBlocks) {
            fallingBlock.remove();
        }
        fallingBlocks.clear();
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
        return true;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (entities.contains(entity)) {
            event.setCancelled(true);

            Location location = entity.getLocation();

            location.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, location, 0);
            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 0.0f);

            for (Player player : MathUtil.getClosestPlayersFromLocation(location, 4.0d)) {
                MathUtil.pushEntity(player, location, MathUtil.randomRange(1.0d, 3.0d), MathUtil.randomRange(1.0d, 2.0d));

                User otherUser = PLUGIN.getUserManager().getConnected(player);

                if (otherUser != null) {
                    otherUser.setFallDamageProtection(8);
                }
            }
            int fallingBlockAmount = 0;

            for (Block block : event.blockList()) {
                if (!block.getType().isSolid()) {
                    continue;
                }
                if (fallingBlockAmount < 10) {
                    fallingBlockAmount++;

                    Location airLocation = block.getLocation().add(0.0d, 1.0d, 0.0d);

                    if (airLocation.getBlock().getType().isAir()) {
                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(location, block.getBlockData());

                        Vector direction = block.getLocation().subtract(entity.getLocation()).toVector().add(new Vector(0.0d, 6.0d, 0.0d));

                        fallingBlock.setVelocity(direction.normalize());
                        fallingBlock.setDropItem(false);
                        fallingBlock.setHurtEntities(false);

                        MetadataUtil.setCustomEntity(fallingBlock);

                        fallingBlocks.add(fallingBlock);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockImpact(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock fallingBlock && fallingBlocks.remove(fallingBlock)) {
            fallingBlock.getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, fallingBlock.getBlockData().getMaterial());
            event.setCancelled(true);
        }
    }
}
