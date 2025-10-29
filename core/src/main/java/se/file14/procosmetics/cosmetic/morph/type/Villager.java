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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Villager implements MorphBehavior {

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();
        Player player = context.getPlayer();
        Location location = player.getEyeLocation();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            player.getWorld().playSound(player, Sound.ENTITY_VILLAGER_YES, 0.5f, 1.0f);
            player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 20, 0.8d, 0.8d, 0.8d);
        } else {
            player.getWorld().playSound(player, Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            player.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, location, 10, 0.8d, 0.8d, 0.8d);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }

    @Override
    public boolean hasAttackAnimation() {
        return false;
    }

    @Override
    public boolean hasItemHoldAnimation() {
        return true;
    }
}
