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
import org.bukkit.event.block.Action;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import java.util.List;

public class Fireworks implements GadgetBehavior {

    private static final List<FireworkEffect.Type> FIREWORK_EFFECTS = List.of(FireworkEffect.Type.values());
    private static final int RGB_COLOR_COUNT = 1 << 24; // 0x1000000

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Location location;

        if (clickedBlock != null && clickedPosition != null) {
            location = clickedBlock.getLocation().add(clickedPosition.multiply(1.2d));
        } else {
            location = context.getPlayer().getLocation();
        }
        location.getWorld().spawn(location, Firework.class, entity -> {
            FireworkMeta fireworkMeta = entity.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder()
                    .flicker(MathUtil.THREAD_LOCAL_RANDOM.nextBoolean())
                    .withColor(Color.fromRGB(MathUtil.THREAD_LOCAL_RANDOM.nextInt(RGB_COLOR_COUNT)))
                    .withFade(Color.fromRGB(MathUtil.THREAD_LOCAL_RANDOM.nextInt(RGB_COLOR_COUNT)))
                    .with(MathUtil.getRandomElement(FIREWORK_EFFECTS))
                    .build());
            fireworkMeta.setPower(2);
            entity.setFireworkMeta(fireworkMeta);

            MetadataUtil.setCustomEntity(entity);
        });
        return InteractionResult.success();
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
