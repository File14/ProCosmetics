package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.material.Materials;

public class PartyTime implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.6d;
    private static final double MOVING_OFFSET_X = 0.1d;
    private static final double MOVING_OFFSET_Y = 0.1d;
    private static final double MOVING_OFFSET_Z = 0.1d;
    private static final double MOVING_SPEED = 0.0d;
    private static final int MOVING_PARTICLE_COUNT = 3;

    private static final double STATIC_HEIGHT_OFFSET = 2.4d;
    private static final float STATIC_ROTATION_SPEED = 14.0f;
    private static final float STATIC_ORBIT_RADIUS = 0.8f;
    private static final double STATIC_OFFSET_X = 0.2d;
    private static final double STATIC_OFFSET_Y = 0.2d;
    private static final double STATIC_OFFSET_Z = 0.2d;
    private static final double STATIC_SPEED = 0.0d;
    private static final int STATIC_PARTICLE_COUNT = 2;
    private static final int STATIC_PARTICLE_BURSTS = 3;

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
        spawnItemParticle(location, MOVING_PARTICLE_COUNT, MOVING_OFFSET_X, MOVING_OFFSET_Y, MOVING_OFFSET_Z, MOVING_SPEED);
    }

    private void spawnStaticEffect(Location location) {
        float angle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(ticks);
        float offsetX = STATIC_ORBIT_RADIUS * FastMathUtil.cos(angle);
        float offsetZ = STATIC_ORBIT_RADIUS * FastMathUtil.sin(angle);

        location.add(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);

        for (int i = 0; i < STATIC_PARTICLE_BURSTS; i++) {
            spawnItemParticle(location, STATIC_PARTICLE_COUNT, STATIC_OFFSET_X, STATIC_OFFSET_Y, STATIC_OFFSET_Z, STATIC_SPEED);
        }

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    private void spawnItemParticle(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(
                Particle.ITEM,
                location,
                count,
                offsetX,
                offsetY,
                offsetZ,
                speed,
                Materials.getRandomInkItem()
        );
    }
}
