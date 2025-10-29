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

public class Music implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.6d;
    private static final int MOVING_PARTICLE_COUNT = 0;

    private static final double STATIC_HEIGHT_OFFSET = 2.2d;
    private static final float STATIC_ROTATION_SPEED = 12.0f;
    private static final float STATIC_ORBIT_RADIUS = 0.6f;
    private static final int STATIC_PARTICLE_COUNT = 0;

    private static final double COLOR_DIVISOR = 24.0d;
    private static final int MAX_COLOR = 23;

    private int ticks;
    private int color;

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

        if (color++ >= MAX_COLOR) {
            color = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    private void spawnMovingEffect(Location location) {
        location.add(0.0d, MOVING_HEIGHT_OFFSET, 0.0d);
        spawnNoteParticle(location, MOVING_PARTICLE_COUNT);
    }

    private void spawnStaticEffect(Location location) {
        float angle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float offsetX = STATIC_ORBIT_RADIUS * FastMathUtil.cos(angle);
        float offsetZ = STATIC_ORBIT_RADIUS * FastMathUtil.sin(angle);

        location.add(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);
        spawnNoteParticle(location, STATIC_PARTICLE_COUNT);

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    private void spawnNoteParticle(Location location, int count) {
        location.getWorld().spawnParticle(
                Particle.NOTE,
                location,
                count,
                color / COLOR_DIVISOR,
                0.0d,
                0.0d
        );
    }
}
