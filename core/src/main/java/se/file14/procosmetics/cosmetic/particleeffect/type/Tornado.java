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

public class Tornado implements ParticleEffectBehavior {

    private static final double MOVING_UPWARDS_MOTION_X = 0.0d;
    private static final double MOVING_UPWARDS_MOTION_Y = 0.6d;
    private static final double MOVING_UPWARDS_MOTION_Z = 0.0d;
    private static final double MOVING_HEIGHT_OFFSET = 0.1d;
    private static final float MOVING_RANGE = 0.1f;
    private static final double MOVING_SPEED = 0.3d;

    private static final double STATIC_HEIGHT_OFFSET = 0.4d;
    private static final int STATIC_LINES = 5;
    private static final float STATIC_ANGLE_BETWEEN_LINE = 360.0f / STATIC_LINES;
    private static final float STATIC_ROTATION_SPEED = 4.0f;
    private static final float STATIC_SPIRAL_RADIUS = 2.5f;
    private static final double STATIC_SPEED = -0.1d;

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

        float yawAngle = FastMathUtil.toRadians(location.getYaw());
        float offsetX = MOVING_RANGE * FastMathUtil.cos(yawAngle);
        float offsetZ = MOVING_RANGE * FastMathUtil.sin(yawAngle);

        location.add(offsetX, 0.0d, offsetZ);
        location.getWorld().spawnParticle(
                Particle.FIREWORK,
                location,
                0,
                MOVING_UPWARDS_MOTION_X,
                MOVING_UPWARDS_MOTION_Y,
                MOVING_UPWARDS_MOTION_Z,
                MOVING_SPEED
        );

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        location.getWorld().spawnParticle(
                Particle.FIREWORK,
                location,
                0,
                MOVING_UPWARDS_MOTION_X,
                MOVING_UPWARDS_MOTION_Y,
                MOVING_UPWARDS_MOTION_Z,
                MOVING_SPEED
        );
    }

    private void spawnStaticEffect(Location location) {
        location.add(0.0d, STATIC_HEIGHT_OFFSET, 0.0d);

        for (int i = 0; i < STATIC_LINES; i++) {
            float angle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(STATIC_ANGLE_BETWEEN_LINE * i + ticks);
            float offsetX = STATIC_SPIRAL_RADIUS * FastMathUtil.sin(angle);
            float offsetZ = STATIC_SPIRAL_RADIUS * FastMathUtil.cos(angle);

            location.add(offsetX, 0.0d, offsetZ);
            location.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    location,
                    0,
                    offsetX,
                    0.0d,
                    offsetZ,
                    STATIC_SPEED
            );
            location.subtract(offsetX, 0.0d, offsetZ);
        }

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }
}
