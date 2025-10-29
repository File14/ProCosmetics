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

public class FlameOfMagic implements ParticleEffectBehavior {

    private static final double MOVING_WITCH_MOTION_X = 0.0d;
    private static final double MOVING_WITCH_MOTION_Y = 1.0d;
    private static final double MOVING_WITCH_MOTION_Z = 0.0d;
    private static final double MOVING_HEIGHT_OFFSET = 0.2d;
    private static final float MOVING_RANGE = 0.12f;
    private static final double MOVING_SPEED = 0.2d;

    private static final double STATIC_WITCH_SPEED = 0.3d;
    private static final int STATIC_WITCH_PARTICLE_COUNT = 2;
    private static final double STATIC_FLAME_HEIGHT_OFFSET = 2.5d;
    private static final float STATIC_ROTATION_SPEED = 8.0f;
    private static final float STATIC_ORBIT_RADIUS = 1.5f;
    private static final double STATIC_FLAME_MOTION_Y = -1.5d;
    private static final double STATIC_FLAME_SPEED = 0.1d;

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
        float yawAngle = FastMathUtil.toRadians(location.getYaw());
        float offsetX = MOVING_RANGE * FastMathUtil.cos(yawAngle);
        float offsetZ = MOVING_RANGE * FastMathUtil.sin(yawAngle);

        location.add(offsetX, MOVING_HEIGHT_OFFSET, offsetZ);
        spawnWitchParticle(location, 0, MOVING_WITCH_MOTION_X, MOVING_WITCH_MOTION_Y, MOVING_WITCH_MOTION_Z, MOVING_SPEED);

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        spawnWitchParticle(location, 0, MOVING_WITCH_MOTION_X, MOVING_WITCH_MOTION_Y, MOVING_WITCH_MOTION_Z, MOVING_SPEED);
    }

    private void spawnStaticEffect(Location location) {
        spawnWitchParticle(location, STATIC_WITCH_PARTICLE_COUNT, 0.0d, 0.0d, 0.0d, STATIC_WITCH_SPEED);

        location.add(0.0d, STATIC_FLAME_HEIGHT_OFFSET, 0.0d);

        float rotationAngle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float motionX = STATIC_ORBIT_RADIUS * FastMathUtil.cos(rotationAngle);
        float motionZ = STATIC_ORBIT_RADIUS * FastMathUtil.sin(rotationAngle);

        spawnFlameParticle(location, motionX, STATIC_FLAME_MOTION_Y, motionZ);
        spawnFlameParticle(location, -motionX, STATIC_FLAME_MOTION_Y, -motionZ);

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    private void spawnWitchParticle(Location location, int count, double motionX, double motionY, double motionZ, double speed) {
        location.getWorld().spawnParticle(
                Particle.WITCH,
                location,
                count,
                motionX,
                motionY,
                motionZ,
                speed
        );
    }

    private void spawnFlameParticle(Location location, double motionX, double motionY, double motionZ) {
        location.getWorld().spawnParticle(
                Particle.FLAME,
                location,
                0,
                motionX,
                motionY,
                motionZ,
                STATIC_FLAME_SPEED
        );
    }
}
