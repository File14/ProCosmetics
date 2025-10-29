package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class YinAndYang implements ParticleEffectBehavior {

    private static final double MOVING_FIREWORK_MOTION_X = 0.0d;
    private static final double MOVING_FIREWORK_MOTION_Y = 1.0d;
    private static final double MOVING_FIREWORK_MOTION_Z = 0.0d;
    private static final double MOVING_HEIGHT_OFFSET = 0.2d;
    private static final float MOVING_RANGE = 0.12f;
    private static final double MOVING_SPEED = 0.2d;

    private static final double STATIC_HEIGHT_OFFSET = 0.1d;
    private static final float STATIC_ROTATION_SPEED = 10.0f;
    private static final float STATIC_ORBIT_RADIUS = 1.5f;
    private static final double STATIC_FIREWORK_MOTION_Y = 0.1d;
    private static final double STATIC_FIREWORK_SPEED = 0.2d;
    private static final double STATIC_SMOKE_MOTION_Y = 0.1d;
    private static final double STATIC_SMOKE_SPEED = 0.1d;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            spawnMovingEffect(location);
        } else {
            spawnStaticEffect(location);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    private void spawnMovingEffect(Location location) {
        float yawAngle = FastMathUtil.toRadians(location.getYaw());
        float offsetX = MOVING_RANGE * FastMathUtil.cos(yawAngle);
        float offsetZ = MOVING_RANGE * FastMathUtil.sin(yawAngle);

        location.add(offsetX, MOVING_HEIGHT_OFFSET, offsetZ);
        location.getWorld().spawnParticle(
                Particle.FIREWORK,
                location,
                0,
                MOVING_FIREWORK_MOTION_X,
                MOVING_FIREWORK_MOTION_Y,
                MOVING_FIREWORK_MOTION_Z,
                MOVING_SPEED
        );

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        location.getWorld().spawnParticle(
                Particle.FIREWORK,
                location,
                0,
                MOVING_FIREWORK_MOTION_X,
                MOVING_FIREWORK_MOTION_Y,
                MOVING_FIREWORK_MOTION_Z,
                MOVING_SPEED
        );
    }

    private void spawnStaticEffect(Location location) {
        location.add(0.0d, STATIC_HEIGHT_OFFSET, 0.0d);

        float angle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float directionX = FastMathUtil.sin(angle);
        float directionZ = FastMathUtil.cos(angle);

        for (int i = 0; i < 2; i++) {
            float radius = i == 0 ? STATIC_ORBIT_RADIUS : -STATIC_ORBIT_RADIUS;
            float motionX = directionX * radius;
            float motionZ = directionZ * radius;

            location.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    location,
                    0,
                    motionX,
                    STATIC_FIREWORK_MOTION_Y,
                    motionZ,
                    STATIC_FIREWORK_SPEED
            );

            location.add(motionX, 0.0d, motionZ);
            location.getWorld().spawnParticle(
                    Particle.SMOKE,
                    location,
                    0,
                    0.0d,
                    STATIC_SMOKE_MOTION_Y,
                    0.0d,
                    STATIC_SMOKE_SPEED
            );
            location.subtract(motionX, 0.0d, motionZ);
        }

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }
}
