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
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.version.BukkitVersion;
import se.file14.procosmetics.util.version.VersionUtil;

import javax.annotation.Nullable;

public class FleshHook implements GadgetBehavior {

    private static final ItemStack ITEM_STACK = new ItemStack(Material.TRIPWIRE_HOOK);
    private static final int MAX_TICKS_ALIVE = 120;

    private Item item;
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        if (item != null) {
            item.remove();
        }
        location = context.getPlayer().getEyeLocation();

        item = location.getWorld().dropItem(location, ITEM_STACK, entity -> {
            entity.setPickupDelay(Integer.MAX_VALUE);
            entity.setVelocity(location.getDirection().multiply(1.5d));

            MetadataUtil.setCustomEntity(entity);
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (item == null || !item.isValid()) {
            return;
        }
        Player player = context.getPlayer();
        item.getLocation(location);
        location.getWorld().spawnParticle(Particle.CRIT, location, 0);
        player.playSound(location, Sound.ITEM_FLINTANDSTEEL_USE, 0.5f, 0.8f);

        if (item.isOnGround() || item.getTicksLived() > MAX_TICKS_ALIVE) {
            despawn();
            return;
        }
        Player hitPlayer = MathUtil.getClosestVisiblePlayerFromLocation(player, location, 1.5d);

        if (hitPlayer != null && hitPlayer != player) {
            if (VersionUtil.isHigherThanOrEqualTo(BukkitVersion.v1_20)) {
                hitPlayer.playHurtAnimation(0.0f);
                hitPlayer.getWorld().playSound(hitPlayer.getLocation(location), Sound.ENTITY_PLAYER_HURT, 0.5f, 1.0f);
            } else {
                hitPlayer.playEffect(org.bukkit.EntityEffect.HURT);
            }
            MathUtil.pullEntity(hitPlayer, player.getLocation(location), 3.0d, 1.2d);
            player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            despawn();

            User user = context.getPlugin().getUserManager().getConnected(hitPlayer);

            if (user != null) {
                user.setFallDamageProtection(8);
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        despawn();
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

    private void despawn() {
        if (item != null) {
            item.remove();
            item = null;
        }
    }
}
