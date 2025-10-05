package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.AbstractShapeParticleEffect;

public class VampireWings extends AbstractShapeParticleEffect {

    private static final int[][] SHAPE = new int[][]{
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public VampireWings() {
        super(VampireWings.builder()
                .shape(SHAPE)
                .colorProvider(value -> Color.fromRGB(0, 0, 0))
                .spacing(0.2d)
                .heightOffset(0.1d)
                .distanceBehind(0.3d)
                .movingParticleCount(5)
                .movingParticleSpread(0.0d)
                .shouldRotate(false)
                .rotationSpeed(0.0f)
                .positionMode(PositionMode.BEHIND_PLAYER)
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractShapeParticleEffect.Builder {
        @Override
        public VampireWings build() {
            validate();
            return new VampireWings();
        }
    }
}