package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class FlameRings implements ParticleEffectBehavior {

    private static final double MOVING_OFFSET_X = 0.3d;
    private static final double MOVING_OFFSET_Y = 0.0d;
    private static final double MOVING_OFFSET_Z = 0.6d;
    private static final float MOVING_RANGE = 0.1f;
    private static final double MOVING_SPEED = 0.0d;
    private static final int MOVING_PARTICLE_COUNT = 1;

    private static final double STATIC_HEIGHT_OFFSET = 1.0d;
    private static final float STATIC_ROTATION_SPEED = 10.0f;
    private static final double STATIC_AXIS_Z_ROTATION = 45.0d;
    private static final int STATIC_PARTICLE_COUNT = 0;

    private int ticks;
    private final Vector vector = new Vector();

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

        location.add(offsetX, 0.0d, offsetZ);
        spawnFlameParticle(location, MOVING_PARTICLE_COUNT, MOVING_OFFSET_X, MOVING_OFFSET_Y, MOVING_OFFSET_Z, MOVING_SPEED);

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        spawnFlameParticle(location, MOVING_PARTICLE_COUNT, MOVING_OFFSET_X, MOVING_OFFSET_Y, MOVING_OFFSET_Z, MOVING_SPEED);
    }

    private void spawnStaticEffect(Location location) {
        location.add(0.0d, STATIC_HEIGHT_OFFSET, 0.0d);

        float angle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float x = FastMathUtil.cos(angle);
        float z = FastMathUtil.sin(angle);

        vector.setX(x).setY(0.0d).setZ(z);

        MathUtil.rotateAroundAxisZ(vector, STATIC_AXIS_Z_ROTATION);
        MathUtil.rotateAroundAxisY(vector, -FastMathUtil.toRadians(location.getYaw()));

        location.add(vector);
        spawnFlameParticle(location, STATIC_PARTICLE_COUNT, 0.0d, 0.0d, 0.0d, 0.0d);

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    private void spawnFlameParticle(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(
                Particle.FLAME,
                location,
                count,
                offsetX,
                offsetY,
                offsetZ,
                speed
        );
    }
}
