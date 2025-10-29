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

public class ProtectiveShield implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.4d;
    private static final double MOVING_OFFSET_X = 0.0d;
    private static final double MOVING_OFFSET_Y = 0.0d;
    private static final double MOVING_OFFSET_Z = 0.0d;
    private static final double MOVING_SPEED = 0.0d;
    private static final int MOVING_PARTICLE_COUNT = 3;

    private static final float STATIC_SHIELD_RADIUS = 1.3f;
    private static final float STATIC_HEIGHT_OFFSET = 1.4f;
    private static final float STATIC_STEP_INCREMENT = FastMathUtil.PI / 10.0f;
    private static final float STATIC_ANGLE_INCREMENT = FastMathUtil.PI / 15.0f;
    private static final float STATIC_MAX_ANGLE = 2.0f * FastMathUtil.PI;
    private static final float STATIC_MAX_STEP = 8.0f * FastMathUtil.PI;
    private static final int STATIC_MAX_TICKS = 40;
    private static final int STATIC_ANIMATION_TICKS = 20;
    private static final int STATIC_PARTICLE_COUNT = 1;

    private int ticks;
    private float step;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            spawnMovingEffect(location);
        } else {
            spawnStaticEffect(location);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    private void spawnMovingEffect(Location location) {
        location.add(0.0d, MOVING_HEIGHT_OFFSET, 0.0d);
        spawnEnchantedHitParticle(location, MOVING_PARTICLE_COUNT, MOVING_OFFSET_X, MOVING_OFFSET_Y, MOVING_OFFSET_Z, MOVING_SPEED);

        step = 0.0f;
        ticks = 0;
    }

    private void spawnStaticEffect(Location location) {
        ticks++;

        if (ticks >= STATIC_MAX_TICKS) {
            ticks = 0;
        } else if (ticks <= STATIC_ANIMATION_TICKS) {
            step += STATIC_STEP_INCREMENT;

            float sinStep = FastMathUtil.sin(step);
            float cosStep = FastMathUtil.cos(step);

            for (float angle = 0.0f; angle <= STATIC_MAX_ANGLE; angle += STATIC_ANGLE_INCREMENT) {
                float cosAngle = FastMathUtil.cos(angle);
                float sinAngle = FastMathUtil.sin(angle);

                float offsetX = STATIC_SHIELD_RADIUS * cosAngle * sinStep;
                float offsetY = STATIC_SHIELD_RADIUS * cosStep + STATIC_HEIGHT_OFFSET;
                float offsetZ = STATIC_SHIELD_RADIUS * sinAngle * sinStep;

                location.add(offsetX, offsetY, offsetZ);
                spawnEnchantedHitParticle(location, STATIC_PARTICLE_COUNT, 0.0d, 0.0d, 0.0d, 0.0d);
                location.subtract(offsetX, offsetY, offsetZ);
            }

            if (step >= STATIC_MAX_STEP) {
                step = 0.0f;
            }
        }
    }

    private void spawnEnchantedHitParticle(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(
                Particle.ENCHANTED_HIT,
                location,
                count,
                offsetX,
                offsetY,
                offsetZ,
                speed
        );
    }
}
