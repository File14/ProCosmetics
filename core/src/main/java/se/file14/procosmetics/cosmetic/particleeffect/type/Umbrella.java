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
import se.file14.procosmetics.util.MathUtil;

public class Umbrella implements ParticleEffectBehavior {

    private static final Particle.DustOptions DUST_OPTIONS_1 = new Particle.DustOptions(Color.fromRGB(151, 177, 199), 1);
    private static final Particle.DustOptions DUST_OPTIONS_2 = new Particle.DustOptions(Color.fromRGB(152, 152, 152), 1);
    private static final Particle.DustOptions DUST_OPTIONS_3 = new Particle.DustOptions(Color.fromRGB(208, 93, 86), 1);

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, 0.3d, 0.0d);

        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(Particle.DUST, location, 1, 0, 0, 0, 0.0d, DUST_OPTIONS_1);
        } else {
            location.add(0.0d, 0.4d, 0.0d);
            Location rotatedLocation = location.clone().add(-0.5d, 0.0d, 0.2d);
            Location baseLocation = location.clone();
            Location offsetLocation = rotatedLocation.subtract(baseLocation);
            Location rotatedOffset = MathUtil.rotateAroundAxisY(offsetLocation.toVector(), -Math.toRadians(location.getYaw())).toLocation(location.getWorld());
            location.add(rotatedOffset.toVector());

            int height = 7;
            for (int i = 0; i < height; i++) {
                location.add(0.0d, 0.28d, 0.0d);
                location.getWorld().spawnParticle(Particle.DUST, location, 1, 0, 0, 0, 0.0d, DUST_OPTIONS_2);
            }
            location.subtract(0.0d, height * 0.28d, 0.0d);

            float range = 1.2f;
            float y = 0;

            for (int i = 0; i < 5; i++) {
                range -= 0.2f;
                y += 0.10f;

                for (float theta = 0; theta <= 6.0d; theta += 0.35f) {
                    float x = range * FastMathUtil.cos(theta);
                    float z = range * FastMathUtil.sin(theta);

                    location.add(x, 1.4d + y, z);

                    boolean inSpecialRange = (theta > 0.0d && theta < 0.75d) ||
                            (theta > 1.5d && theta < 2.25d) ||
                            (theta > 3.0d && theta < 3.75d) ||
                            (theta > 4.5d && theta < 5.3d);

                    location.getWorld().spawnParticle(
                            Particle.DUST,
                            location,
                            1,
                            0,
                            0,
                            0,
                            0.0d,
                            inSpecialRange ? DUST_OPTIONS_3 : DUST_OPTIONS_1
                    );
                    location.subtract(x, 1.4d + y, z);
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}
