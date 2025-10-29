package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class ColorfulTrail implements ParticleEffectBehavior {

    private static final double HEIGHT_OFFSET = 0.1d;
    private static final int PARTICLE_COUNT = 10;
    private static final float TRAIL_START_OFFSET = -0.85f;
    private static final float TRAIL_SPACING = 0.2f;
    private static final int RGB_STEP = 15;

    private int red = 255;
    private int green = 0;
    private int blue = 0;

    private final Vector vector = new Vector();

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            cycleRainbowColor();
        }
        spawnParticles(location);
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    private void spawnParticles(Location location) {
        location.add(0.0d, HEIGHT_OFFSET, 0.0d);

        float yawAngle = -FastMathUtil.toRadians(location.getYaw());
        float offsetX = TRAIL_START_OFFSET;
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(red, green, blue), 1);

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            vector.setX(offsetX).setY(0.0d).setZ(0.0d);
            MathUtil.rotateAroundAxisY(vector, yawAngle);

            location.add(vector);
            location.getWorld().spawnParticle(
                    Particle.DUST,
                    location,
                    1,
                    0.0d,
                    0.0d,
                    0.0d,
                    0.0d,
                    dustOptions
            );
            location.subtract(vector);

            offsetX += TRAIL_SPACING;
        }
    }

    private void cycleRainbowColor() {
        if (red == 255 && green < 255 && blue == 0) {
            green = Math.min(green + RGB_STEP, 255);
        } else if (green == 255 && red > 0 && blue == 0) {
            red = Math.max(red - RGB_STEP, 0);
        } else if (green == 255 && blue < 255 && red == 0) {
            blue = Math.min(blue + RGB_STEP, 255);
        } else if (blue == 255 && green > 0 && red == 0) {
            green = Math.max(green - RGB_STEP, 0);
        } else if (blue == 255 && red < 255 && green == 0) {
            red = Math.min(red + RGB_STEP, 255);
        } else if (red == 255 && blue > 0 && green == 0) {
            blue = Math.max(blue - RGB_STEP, 0);
        }
    }
}
