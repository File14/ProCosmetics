package se.file14.procosmetics.cosmetic.particleeffect.type;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import se.file14.procosmetics.cosmetic.particleeffect.shape.AbstractShapeParticleEffect;

public class ChristmasTree extends AbstractShapeParticleEffect {

    private static final ChatColor[] COLORS = new ChatColor[]{
            null,
            ChatColor.of("#663300"), // Brown
            ChatColor.of("#006600"), // Green
            ChatColor.YELLOW,
            ChatColor.RED,
            ChatColor.AQUA
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
        super(ChristmasTree.builder()
                .shape(SHAPE)
                .colorProvider(value -> {
                    if (value > 0 && value < COLORS.length && COLORS[value] != null) {
                        java.awt.Color awtColor = COLORS[value].getColor();
                        return Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
                    }
                    return Color.WHITE;
                })
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractShapeParticleEffect.Builder {
        @Override
        public ChristmasTree build() {
            validate();
            return new ChristmasTree();
        }
    }
}