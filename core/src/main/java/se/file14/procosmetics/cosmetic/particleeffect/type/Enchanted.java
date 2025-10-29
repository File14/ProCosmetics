package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;

public class Enchanted implements ParticleEffectBehavior {

    private static final double MOVING_HEIGHT_OFFSET = 0.8d;
    private static final double MOVING_SPEED = 0.2d;
    private static final int MOVING_PARTICLE_COUNT = 5;

    private static final double STATIC_HEIGHT_OFFSET = 1.5d;
    private static final double STATIC_SPEED = 1.0d;
    private static final int STATIC_PARTICLE_COUNT = 5;

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
        spawnEnchantParticle(location, MOVING_PARTICLE_COUNT, MOVING_SPEED);
    }

    private void spawnStaticEffect(Location location) {
        location.add(0.0d, STATIC_HEIGHT_OFFSET, 0.0d);
        spawnEnchantParticle(location, STATIC_PARTICLE_COUNT, STATIC_SPEED);
    }

    private void spawnEnchantParticle(Location location, int count, double speed) {
        location.getWorld().spawnParticle(
                Particle.ENCHANT,
                location,
                count,
                0.0d,
                0.0d,
                0.0d,
                speed
        );
    }
}
