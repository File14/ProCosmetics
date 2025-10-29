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
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.cosmetic.particleeffect.shape.ShapeParticleEffectBehavior;
import se.file14.procosmetics.util.RGBFade;

public class RainbowWings extends ShapeParticleEffectBehavior {

    private static final int[][] SHAPE = new int[][]{
            {0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}
    };

    private final RGBFade rgb;

    public RainbowWings() {
        this(new RGBFade());
    }

    private RainbowWings(RGBFade rgb) {
        super(settings()
                .shape(SHAPE)
                .colorProvider(value -> {
                    if (value == 0) {
                        return null;
                    }
                    return Color.fromRGB(rgb.getR(), rgb.getG(), rgb.getB());
                })
                .spacing(0.2d)
                .heightOffset(0.1d)
                .distanceBehind(0.3d)
                .movingParticleCount(1)
                .movingParticleSpread(0.0d)
                .rotationSpeed(0.0f)
                .positionMode(PositionMode.BEHIND_PLAYER));

        this.rgb = rgb;
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        super.onUpdate(context, location);
        rgb.nextRGB();
    }
}
