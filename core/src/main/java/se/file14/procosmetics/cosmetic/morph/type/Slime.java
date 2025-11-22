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

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;

public class Slime implements MorphBehavior, Listener {

    private static final BlockData BLOCK_DATA = Material.SLIME_BLOCK.createBlockData();

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private FallingBlock fallingBlock;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (nmsEntity.getBukkitEntity() instanceof org.bukkit.entity.Slime slime) {
            slime.setSize(3);
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        if (event.getAction() == Action.LEFT_CLICK_AIR && fallingBlock == null) {
            Player player = context.getPlayer();
            Location location = player.getLocation();

            fallingBlock = player.getWorld().spawnFallingBlock(location, BLOCK_DATA);
            fallingBlock.setDropItem(false);
            fallingBlock.setHurtEntities(false);
            Vector vector = location.add(0.0D, 0.3, 0.0D).getDirection().multiply(1.3D);
            fallingBlock.setVelocity(vector.add(new Vector(0.0D, 0.5D, 0.0D)));

            context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), () -> {
                if (fallingBlock != null) {
                    removeBlock();
                }
            }, 75L);
            return InteractionResult.success();
        }
        return InteractionResult.noAction();
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (player.isSneaking() && player.isOnGround()) {
            player.getLocation(location);
            location.setPitch(0.0f);
            player.setVelocity(location.getDirection().multiply(0.8d).add(new Vector(0.0d, 0.6d, 0.0d)));
            player.getWorld().playSound(player, Sound.ENTITY_SLIME_JUMP, 0.8f, 1.0f);
            location.getWorld().spawnParticle(Particle.ITEM_SLIME,
                    location,
                    30,
                    1.5d,
                    0.5d,
                    1.5d,
                    0.0d
            );
        }

        if (fallingBlock == null || !fallingBlock.isValid()) {
            return;
        }
        Location fallingBlockLocation = fallingBlock.getLocation(location);
        Player hitPlayer = MathUtil.getClosestPlayerFromLocation(fallingBlockLocation, 1.5D);

        if (hitPlayer == null || hitPlayer == player) {
            return;
        }
        MathUtil.pushEntity(hitPlayer, location, 0.4d, 0.3d);
        User otherUser = context.getPlugin().getUserManager().getConnected(hitPlayer);

        if (otherUser != null) {
            otherUser.setFallDamageProtection(3);
        }
        player.getWorld().playSound(fallingBlock, Sound.ENTITY_SLIME_ATTACK, 1.0f, 1.0f);
        removeBlock();
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        if (fallingBlock != null) {
            removeBlock();
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() == fallingBlock) {
            removeBlock();
            event.setCancelled(true);
        }
    }

    private void removeBlock() {
        fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.STEP_SOUND, fallingBlock.getBlockData().getMaterial());
        fallingBlock.remove();
        fallingBlock = null;
    }
}
