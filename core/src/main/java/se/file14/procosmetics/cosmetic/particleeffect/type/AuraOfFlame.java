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
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class AuraOfFlame implements ParticleEffectBehavior {

    private static final Vector PARTICLE_MOTION = new Vector(0.0d, 0.4d, 0.0d);

    private static final double MOVING_HEIGHT_OFFSET = 0.1d;
    private static final float MOVING_RANGE = 0.1f;
    private static final double MOVING_SPEED = 0.3d;

    private static final double STATIC_HEIGHT_OFFSET = 0.3d;
    private static final float STATIC_ROTATION_SPEED = 12.0f;
    private static final float STATIC_ORBIT_RADIUS = 0.8f;
    private static final double STATIC_SPEED = 0.2d;

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
        spawnFlameParticle(location, MOVING_SPEED);

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        spawnFlameParticle(location, MOVING_SPEED);
    }

    private void spawnStaticEffect(Location location) {
        float rotationAngle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float offsetX = STATIC_ORBIT_RADIUS * FastMathUtil.cos(rotationAngle);
        float offsetZ = STATIC_ORBIT_RADIUS * FastMathUtil.sin(rotationAngle);

        location.add(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);
        spawnFlameParticle(location, STATIC_SPEED);

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        spawnFlameParticle(location, STATIC_SPEED);

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    private void spawnFlameParticle(Location location, double speed) {
        location.getWorld().spawnParticle(
                Particle.FLAME,
                location,
                0,
                PARTICLE_MOTION.getX(),
                PARTICLE_MOTION.getY(),
                PARTICLE_MOTION.getZ(),
                speed
        );
    }
}
