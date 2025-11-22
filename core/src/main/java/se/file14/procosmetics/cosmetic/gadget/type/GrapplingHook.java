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
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.gadget.GadgetImpl;

public class GrapplingHook implements GadgetBehavior, Listener {

    private static final Vector MULTIPLIER_VECTOR = new Vector(2.5d, 1.5d, 2.5d);

    private Player player;
    private User user;
    private boolean thrownHook;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (player == null) {
            player = context.getPlayer();
            user = context.getUser();
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        // PlayerInteractEvent triggers twice when reeling in the hook:
        // first with LEFT_CLICK_AIR, then with RIGHT_CLICK.
        // We ignore all left-clicks entirely.
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            return InteractionResult.fail();
        }

        if (thrownHook) {
            return InteractionResult.of(false, false, false);
        }
        // Hook is not thrown if you are looking at a block
        if (clickedBlock != null) {
            return InteractionResult.fail();
        }
        return InteractionResult.of(false, true, true);
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
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
    public void onFish(PlayerFishEvent event) {
        if (event.getPlayer() != player
                || !GadgetImpl.GADGET_ID.is(event.getPlayer().getInventory().getItemInMainHand())
                || event.getState() == PlayerFishEvent.State.BITE
                || event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            return;
        }

        if (event.getState() == PlayerFishEvent.State.FISHING) {
            thrownHook = true;
        } else {
            Entity hook = event.getHook();

            Vector vector = hook.getLocation().subtract(player.getLocation()).toVector().multiply(1.3d);
            vector.setY(vector.getY() + 0.5d);
            vector.normalize().multiply(MULTIPLIER_VECTOR);

            player.setVelocity(vector);
            user.setFallDamageProtection(8);

            thrownHook = false;

            hook.remove();
            event.setCancelled(true);
        }
    }
}
