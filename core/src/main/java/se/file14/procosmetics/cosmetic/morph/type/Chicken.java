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
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Chicken implements MorphBehavior {

    private static final ItemStack EGG_ITEM = new ItemStack(Material.EGG);

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
            player.getWorld().playSound(player, Sound.ENTITY_CHICKEN_AMBIENT, 1.0f, 1.0f);
            return InteractionResult.success();
        }
        return InteractionResult.noAction();
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        if (event.isSneaking()) {
            Player player = context.getPlayer();
            Location location = player.getLocation();

            NMSEntity eggEntity = context.getPlugin().getNMSManager().createEntity(player.getWorld(), EntityType.ITEM);
            eggEntity.setEntityItemStack(EGG_ITEM);
            eggEntity.setPositionRotation(location.add(0.0d, 0.1d, 0.0d));
            eggEntity.getTracker().startTracking();
            eggEntity.getTracker().destroyAfter(80);

            player.getWorld().playSound(player, Sound.ENTITY_CHICKEN_EGG, 0.8f, 1.0f);
            return InteractionResult.success();
        }
        return InteractionResult.noAction();
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}
