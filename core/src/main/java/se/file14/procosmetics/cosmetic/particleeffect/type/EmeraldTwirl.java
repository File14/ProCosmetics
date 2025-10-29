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
package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class EmeraldTwirl implements ParticleEffectBehavior {

    private static final float ROTATION_SPEED = 10.0f;
    private static final float FREQUENCY_MULTIPLIER = 0.2f;
    private static final float HEIGHT_OFFSET = 1.2f;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        float angle = ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float offsetX = FastMathUtil.sin(angle);
        float offsetY = FastMathUtil.sin(FREQUENCY_MULTIPLIER * angle) + HEIGHT_OFFSET;
        float offsetZ = FastMathUtil.cos(angle);

        location.add(offsetX, offsetY, offsetZ);
        location.getWorld().spawnParticle(
                Particle.HAPPY_VILLAGER,
                location,
                0
        );

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}
