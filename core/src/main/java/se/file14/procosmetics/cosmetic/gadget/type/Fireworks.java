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

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;

public class Fireworks implements GadgetBehavior {

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Location location;

        if (clickedBlock != null) {
            location = clickedBlock.getLocation().add(0.5d, 1.0d, 0.5d);
        } else {
            location = context.getPlayer().getLocation();
        }
        location.getWorld().spawn(location, Firework.class, entity -> {
            FireworkMeta fireworkMeta = entity.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder()
                    .flicker(MathUtil.THREAD_LOCAL_RANDOM.nextBoolean())
                    .withColor(Color.fromBGR(MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255)))
                    .withFade(Color.fromBGR(MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255)))
                    .with(FireworkEffect.Type.STAR)
                    .build());
            fireworkMeta.setPower(2);
            entity.setFireworkMeta(fireworkMeta);

            MetadataUtil.setCustomEntity(entity);
        });
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
        return false;
    }
}
