package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.ShapeParticleEffectBehavior;

public class Bunny extends ShapeParticleEffectBehavior {

    private static final Color[] COLORS = new Color[]{
            null,
            Color.WHITE,
            Color.fromRGB(0xFF55FF), // Light purple
            Color.BLACK
    };

    private static final int[][] SHAPE = new int[][]{
            {0, 1, 1, 0, 1, 1, 0},
            {1, 2, 1, 0, 1, 2, 1},
            {1, 2, 1, 0, 1, 2, 1},
            {1, 2, 1, 0, 1, 2, 1},
            {0, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 3, 1, 1, 1, 3, 1},
            {1, 1, 1, 2, 1, 1, 1},
            {0, 1, 1, 1, 1, 1, 0},
    };

    public Bunny() {
        super(settings()
                .shape(SHAPE)
                .colorProvider(value -> COLORS[value])
                .spacing(0.2d)
                .heightOffset(0.2d)
                .distanceBehind(0.4d)
                .movingParticleCount(3)
                .movingParticleSpread(0.0d)
                .shouldRotate(false)
                .rotationSpeed(0.5f)
                .positionMode(PositionMode.BEHIND_PLAYER)
        );
    }
}