package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class FrostLord implements ParticleEffectBehavior {

    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.WHITE, 1.0f);
    private static final float ANGLE_STEP = (float) (Math.PI / 2);
    private static final float STEP_INCREMENT = 0.2f;
    private static final float SPIRAL_RADIUS_FACTOR = 0.25f;
    private static final float VERTICAL_SPEED = 0.4f;
    private static final float MAX_STEP = 8.5f;
    private static final int SPIRAL_ARMS = 4;

    private float animationStep = 0.0f;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            Location particleLocation = location.add(0.0, 0.5, 0.0);
            particleLocation.getWorld().spawnParticle(
                    Particle.DUST,
                    particleLocation,
                    5,
                    0.0, 0.0, 0.0,
                    0.0,
                    DUST_OPTIONS
            );
            animationStep = 0.0f;
        } else {
            animationStep += STEP_INCREMENT;
            float height = animationStep * VERTICAL_SPEED;
            float radius = SPIRAL_RADIUS_FACTOR * (3.0f * FastMathUtil.PI - animationStep);

            spawnSpiralParticles(location, height, radius);

            if (animationStep >= MAX_STEP) {
                spawnSnowflakeBurst(location, height);
                animationStep = 0.0f;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    private void spawnSpiralParticles(Location location, float height, float radius) {
        for (int arm = 0; arm < SPIRAL_ARMS; arm++) {
            float angle = animationStep + (arm * ANGLE_STEP);
            float offsetX = radius * FastMathUtil.cos(angle);
            float offsetZ = radius * FastMathUtil.sin(angle);

            location.add(offsetX, height, offsetZ);
            location.getWorld().spawnParticle(
                    Particle.DUST,
                    location,
                    1,
                    0.0d, 0.0d, 0.0d,
                    0.0d,
                    DUST_OPTIONS
            );
            location.subtract(offsetX, height, offsetZ);
        }
    }

    private void spawnSnowflakeBurst(Location location, float height) {
        location.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                location.add(0.0d, height, 0.0d),
                20,
                0.0d, 0.0d, 0.0d,
                0.5d
        );
    }
}