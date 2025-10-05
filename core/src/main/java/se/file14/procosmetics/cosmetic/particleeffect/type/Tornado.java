package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class Tornado implements ParticleEffectBehavior {

    private static final Vector UPWARDS_MOTION = new Vector(0, 0.6d, 0);

    private static final int LINES = 5;
    private static final float ANGLE_BETWEEN_LINE = 360.0f / LINES;
    private static final float SPEED = 4.0f;
    private static final float RANGE = 2.5f;
    private static final float MOVING_RANGE = 0.1f;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, 0.1d, 0.0d);

        if (context.getUser().isMoving()) {
            float radians = FastMathUtil.toRadians(location.getYaw());
            float x = MOVING_RANGE * FastMathUtil.cos(radians);
            float z = MOVING_RANGE * FastMathUtil.sin(radians);

            location.getWorld().spawnParticle(Particle.FIREWORK,
                    location.add(x, 0.0d, z),
                    0,
                    UPWARDS_MOTION.getX(),
                    UPWARDS_MOTION.getY(),
                    UPWARDS_MOTION.getZ(),
                    0.3d
            );
            location.getWorld().spawnParticle(Particle.FIREWORK,
                    location.subtract(2.0d * x, 0.0d, 2.0d * z),
                    0,
                    UPWARDS_MOTION.getX(),
                    UPWARDS_MOTION.getY(),
                    UPWARDS_MOTION.getZ(),
                    0.3d
            );
        } else {
            location.add(0.0d, 0.3d, 0.0d);

            for (int i = 0; i < LINES; i++) {
                float radians = SPEED * FastMathUtil.toRadians(ANGLE_BETWEEN_LINE * i + ticks);
                float x = RANGE * FastMathUtil.sin(radians);
                float z = RANGE * FastMathUtil.cos(radians);

                location.add(x, 0.0d, z);
                location.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        location,
                        0,
                        x,
                        0.0d,
                        z,
                        -0.1d
                );
                location.subtract(x, 0.0d, z);
            }

            if (ticks++ > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}