package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;

public class SnowCloud implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.4d;
    private static final int MOVING_PARTICLE_COUNT = 2;

    private static final double STATIC_CLOUD_HEIGHT_OFFSET = 2.8d;
    private static final double STATIC_CLOUD_OFFSET_X = 0.5d;
    private static final double STATIC_CLOUD_OFFSET_Y = 0.1d;
    private static final double STATIC_CLOUD_OFFSET_Z = 0.5d;
    private static final int STATIC_CLOUD_PARTICLE_COUNT = 4;

    private static final double STATIC_SNOW_OFFSET_X = 0.25d;
    private static final double STATIC_SNOW_OFFSET_Y = 0.05d;
    private static final double STATIC_SNOW_OFFSET_Z = 0.25d;
    private static final int STATIC_SNOW_PARTICLE_COUNT = 2;

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
        location.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                location,
                MOVING_PARTICLE_COUNT,
                0.0d,
                0.0d,
                0.0d,
                0.0d
        );
    }

    private void spawnStaticEffect(Location location) {
        location.add(0.0d, STATIC_CLOUD_HEIGHT_OFFSET, 0.0d);
        location.getWorld().spawnParticle(
                Particle.CLOUD,
                location,
                STATIC_CLOUD_PARTICLE_COUNT,
                STATIC_CLOUD_OFFSET_X,
                STATIC_CLOUD_OFFSET_Y,
                STATIC_CLOUD_OFFSET_Z,
                0.0d
        );
        location.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                location,
                STATIC_SNOW_PARTICLE_COUNT,
                STATIC_SNOW_OFFSET_X,
                STATIC_SNOW_OFFSET_Y,
                STATIC_SNOW_OFFSET_Z,
                0.0d
        );
    }
}
