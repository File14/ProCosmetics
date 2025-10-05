package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.AbstractShapeParticleEffect;

public class Skull extends AbstractShapeParticleEffect {

    private static final int[][] SHAPE = new int[][]{
            {0, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 1, 2, 2, 1},
            {1, 2, 2, 1, 2, 2, 1},
            {1, 1, 1, 2, 2, 1, 1},
            {0, 1, 1, 2, 1, 1, 0},
            {0, 1, 0, 1, 0, 1, 0},
    };

    public Skull() {
        super(Skull.builder()
                .shape(SHAPE)
                .colorProvider(value -> switch (value) {
                    case 1 -> Color.WHITE;
                    case 2 -> Color.BLACK;
                    default -> Color.WHITE;
                })
                .spacing(0.25d)
                .heightOffset(0.1d)
                .distanceBehind(0.4d)
                .movingParticleCount(3)
                .movingParticleSpread(0.0d)
                .shouldRotate(false)
                .rotationSpeed(0.5f)
                .positionMode(PositionMode.BEHIND_PLAYER)
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractShapeParticleEffect.Builder {
        @Override
        public Skull build() {
            validate();
            return new Skull();
        }
    }
}