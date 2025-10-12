package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.ShapeParticleEffectBehavior;

public class ChristmasTree extends ShapeParticleEffectBehavior {

    private static final Color[] COLORS = new Color[]{
            null,
            Color.fromRGB(0x663300), // Brown
            Color.fromRGB(0x006600), // Green
            Color.YELLOW,
            Color.RED,
            Color.AQUA
    };

    private static final int[][] SHAPE = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 5, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 5, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 4, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 2, 4, 4, 4, 2, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 4, 2, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 3, 3, 2, 2, 2, 2, 4, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 2, 2, 3, 3, 3, 2, 2, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public ChristmasTree() {
        super(settings()
                .shape(SHAPE)
                .colorProvider(value -> COLORS[value])
                .spacing(0.2d)
                .heightOffset(0.1d)
                .distanceBehind(0.3d)
                .movingParticleCount(3)
                .movingParticleSpread(0.0d)
                .shouldRotate(false)
                .rotationSpeed(0.5f)
                .positionMode(PositionMode.BEHIND_PLAYER)
        );
    }
}