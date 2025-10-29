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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.material.Materials;

import javax.annotation.Nullable;

public class Confetti implements GadgetBehavior {

    private static final double SPREAD = 0.5d;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        Location location = player.getEyeLocation();
        Vector vector = player.getLocation().getDirection();

        player.getWorld().playSound(location, Sound.ENTITY_BLAZE_SHOOT, 0.8f, 2.0f);

        for (int i = 0; i < 3; ++i) {
            location.getWorld().spawnParticle(Particle.FIREWORK,
                    location,
                    0,
                    vector.getX(),
                    vector.getY(),
                    vector.getZ(),
                    0.3d
            );
        }

        for (int i = 0; i < 50; ++i) {
            location.getWorld().spawnParticle(Particle.ITEM,
                    location,
                    0,
                    vector.getX() + MathUtil.randomRange(-SPREAD, SPREAD),
                    vector.getY() + MathUtil.randomRange(-SPREAD, SPREAD),
                    vector.getZ() + MathUtil.randomRange(-SPREAD, SPREAD),
                    0.8d,
                    Materials.getRandomWoolItem()
            );
        }
        return InteractionResult.SUCCESS;
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
        return true;
    }
}
