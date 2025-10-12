package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.ShapeParticleEffectBehavior;

public class BigHeart extends ShapeParticleEffectBehavior {

    private static final Color[] COLORS = new Color[]{
            null,
            Color.fromRGB(0xFFB2FF) // Pastel magenta
    };

    private static final int[][] SHAPE = new int[][]{
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public BigHeart() {
        super(settings()
                .shape(SHAPE)
                .colorProvider(value -> COLORS[value])
                .spacing(0.2d)
                .heightOffset(0.1d)
                .distanceBehind(0.0d)
                .movingParticleCount(3)
                .movingParticleSpread(0.0d)
                .shouldRotate(false)
                .rotationSpeed(0.5f)
                .positionMode(PositionMode.BELOW_PLAYER)
        );
    }
}