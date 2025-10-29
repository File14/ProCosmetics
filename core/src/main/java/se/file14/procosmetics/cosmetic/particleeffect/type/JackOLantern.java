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
import se.file14.procosmetics.cosmetic.particleeffect.shape.ShapeParticleEffectBehavior;

public class JackOLantern extends ShapeParticleEffectBehavior {

    private static final Color[] COLORS = new Color[]{
            null,
            Color.fromRGB(0x663300), // Brown
            Color.fromRGB(0xcc7a00), // Dark orange
            Color.YELLOW,            // Yellow
            Color.fromRGB(0xffa31a), // Orange
    };

    private static final int[][] SHAPE = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 2, 2, 2, 1, 1, 2, 2, 2, 0, 0, 0},
            {0, 2, 2, 2, 4, 4, 4, 4, 4, 4, 2, 2, 2, 0},
            {2, 2, 4, 3, 4, 4, 4, 4, 4, 4, 3, 4, 2, 2},
            {2, 4, 4, 3, 3, 4, 4, 4, 4, 3, 3, 4, 4, 2},
            {2, 4, 4, 3, 3, 3, 4, 4, 3, 3, 3, 4, 4, 2},
            {2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2},
            {2, 4, 4, 4, 3, 4, 3, 3, 3, 4, 3, 4, 4, 2},
            {2, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 2},
            {2, 2, 4, 4, 3, 4, 4, 3, 4, 3, 4, 4, 2, 2},
            {0, 2, 2, 2, 4, 4, 4, 4, 4, 4, 2, 2, 2, 0},
            {0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0},
    };

    public JackOLantern() {
        super(settings()
                .shape(SHAPE)
                .colorProvider(value -> COLORS[value])
                .spacing(0.15d)
                .heightOffset(0.1d)
                .distanceBehind(0.3d)
                .movingParticleCount(5)
                .movingParticleSpread(0.0d)
                .shouldRotate(false)
                .rotationSpeed(0.0f)
                .positionMode(PositionMode.BEHIND_PLAYER)
        );
    }
}
