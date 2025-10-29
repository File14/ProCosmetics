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
package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class CandyCaneRemains implements DeathEffectBehavior {

    private static final List<ItemStack> ITEMS = List.of(
            new ItemStack(Material.RED_DYE),
            new ItemStack(Material.GREEN_DYE),
            new ItemStack(Material.BONE_MEAL)
    );

    private static final int AMOUNT = 50;
    private static final double SPREAD = 5.0d;

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.add(0.0d, 1.0d, 0.0d);

        for (int i = 0; i < AMOUNT; i++) {
            for (ItemStack itemStack : ITEMS) {
                location.getWorld().spawnParticle(Particle.ITEM,
                        location,
                        0,
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        0.1d,
                        itemStack
                );
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}
