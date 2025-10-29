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
package se.file14.procosmetics.cosmetic.pet.type;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;

public class ColorfulLamb implements PetBehavior {

    private final static DyeColor[] DYE_COLORS = DyeColor.values();
    private int color;

    @Override
    public void onEquip(CosmeticContext<PetType> context) {
    }

    @Override
    public void onSetupEntity(CosmeticContext<PetType> context, Entity entity) {
    }

    @Override
    public void onUpdate(CosmeticContext<PetType> context, Entity entity) {
        if (entity instanceof Sheep sheep) {
            if (color >= DYE_COLORS.length) {
                color = 0;
            }
            sheep.setColor(DYE_COLORS[color]);
            color++;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<PetType> context) {
    }
}
