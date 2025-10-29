package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class DemonTwist implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.4d;
    private static final double MOVING_SPEED = 0.1d;
    private static final int MOVING_PARTICLE_COUNT = 1;

    private static final double STATIC_HEIGHT_OFFSET = 0.2d;
    private static final float STATIC_ROTATION_SPEED = 0.15f;
    private static final float STATIC_WAVE_FREQUENCY = 4.0f;
    private static final float STATIC_WAVE_AMPLITUDE = 2.0f;
    private static final double STATIC_SPEED = 0.0d;
    private static final int STATIC_PARTICLE_COUNT = 2;

    private int ticks;
    private float angle;

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
        location.add(0.0d, MOVING_HEIGHT_OFFSET, 0.0d);
        spawnFlameParticle(location, MOVING_PARTICLE_COUNT, MOVING_SPEED);
    }

    private void spawnStaticEffect(Location location) {
        float range = STATIC_WAVE_AMPLITUDE * FastMathUtil.sin(STATIC_WAVE_FREQUENCY * FastMathUtil.toRadians(ticks));

        if (ticks++ >= 180) {
            ticks = 0;
        }

        angle += STATIC_ROTATION_SPEED;
        if (angle > Math.PI) {
            angle = 0;
        }

        float offsetX = range * FastMathUtil.cos(angle);
        float offsetZ = range * FastMathUtil.sin(angle);

        location.add(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);
        spawnFlameParticle(location, STATIC_PARTICLE_COUNT, STATIC_SPEED);

        location.subtract(2.0d * offsetX, 0.0d, 2.0d * offsetZ);
        spawnFlameParticle(location, STATIC_PARTICLE_COUNT, STATIC_SPEED);
    }

    private void spawnFlameParticle(Location location, int count, double speed) {
        location.getWorld().spawnParticle(
                Particle.FLAME,
                location,
                count,
                0.0d,
                0.0d,
                0.0d,
                speed
        );
    }
}
