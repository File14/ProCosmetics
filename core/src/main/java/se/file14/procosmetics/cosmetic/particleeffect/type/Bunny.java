package se.file14.procosmetics.cosmetic.particleeffect.type;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.AbstractShapeParticleEffect;

public class Bunny extends AbstractShapeParticleEffect {

    private static final ChatColor[] COLORS = new ChatColor[]{
            null,
            ChatColor.WHITE,
            ChatColor.LIGHT_PURPLE,
            ChatColor.BLACK
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
        super(Bunny.builder()
                .shape(SHAPE)
                .colorProvider(value -> {
                    if (value > 0 && value < COLORS.length && COLORS[value] != null) {
                        java.awt.Color awtColor = COLORS[value].getColor();
                        return Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
                    }
                    return Color.WHITE;
                })
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractShapeParticleEffect.Builder {
        @Override
        public Bunny build() {
            validate();
            return new Bunny();
        }
    }
}