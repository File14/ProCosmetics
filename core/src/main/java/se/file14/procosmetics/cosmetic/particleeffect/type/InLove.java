package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class InLove implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.6d;
    private static final int MOVING_PARTICLE_COUNT = 0;

    private static final double STATIC_HEIGHT_OFFSET = 2.1d;
    private static final float STATIC_ROTATION_SPEED = 30.0f;
    private static final float STATIC_ORBIT_RADIUS = 0.6f;
    private static final int STATIC_PARTICLE_COUNT = 0;

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
        location.add(0.0d, MOVING_HEIGHT_OFFSET, 0.0d);
        location.getWorld().spawnParticle(Particle.HEART, location, MOVING_PARTICLE_COUNT);
    }

    private void spawnStaticEffect(Location location) {
        float angle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float offsetX = STATIC_ORBIT_RADIUS * FastMathUtil.cos(angle);
        float offsetZ = STATIC_ORBIT_RADIUS * FastMathUtil.sin(angle);

        location.add(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);
        location.getWorld().spawnParticle(Particle.HEART, location, STATIC_PARTICLE_COUNT);

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }
}
