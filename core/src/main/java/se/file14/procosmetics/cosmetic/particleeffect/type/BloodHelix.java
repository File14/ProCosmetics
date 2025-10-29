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

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class BloodHelix implements ParticleEffectBehavior {

    private static final Particle.DustOptions RED_DUST = new Particle.DustOptions(Color.RED, 1);

    private static final double MOVING_HEIGHT_OFFSET = 0.2d;

    private static final float HELIX_START_RADIUS = 1.2f;
    private static final float HELIX_RADIUS_DECAY = 0.08f;
    private static final int HELIX_STEPS = 14;
    private static final float HELIX_ANGLE_PER_STEP = 270.0f / HELIX_STEPS;
    private static final float HELIX_ROTATION_SPEED = 2.0f;
    private static final double HELIX_HEIGHT_PER_STEP = 0.2d;

    private int ticks;

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
        location.getWorld().spawnParticle(
                Particle.DUST,
                location,
                1,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                RED_DUST
        );
    }

    private void spawnStaticEffect(Location location) {
        float radius = HELIX_START_RADIUS;

        for (int step = 0; step < HELIX_STEPS; step++) {
            float angle = FastMathUtil.toRadians((HELIX_ANGLE_PER_STEP * step) + ticks * HELIX_ROTATION_SPEED);
            float oppositeAngle = angle + FastMathUtil.PI;

            float x = radius * FastMathUtil.cos(angle);
            float z = radius * FastMathUtil.sin(angle);
            float x2 = radius * FastMathUtil.cos(oppositeAngle);
            float z2 = radius * FastMathUtil.sin(oppositeAngle);

            location.add(x, 0.0d, z);
            location.getWorld().spawnParticle(
                    Particle.DUST,
                    location,
                    1,
                    0.0d,
                    0.0d,
                    0.0d,
                    0.0d,
                    RED_DUST
            );
            location.subtract(x, 0.0d, z);

            location.add(x2, 0.0d, z2);
            location.getWorld().spawnParticle(
                    Particle.DUST,
                    location,
                    1,
                    0.0d,
                    0.0d,
                    0.0d,
                    0.0d,
                    RED_DUST
            );
            location.subtract(x2, 0.0d, z2);

            location.add(0.0d, HELIX_HEIGHT_PER_STEP, 0.0d);
            radius -= HELIX_RADIUS_DECAY;
        }

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }
}
