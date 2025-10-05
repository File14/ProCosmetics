package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class Music implements ParticleEffectBehavior {

    private static final float SPEED = 12.0f;
    private static final float RANGE = 0.6f;

    private int ticks;
    private int color;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(
                    Particle.NOTE,
                    location.add(0.0d, 0.6d, 0.0d),
                    0,
                    color / 24.0d,
                    0.0d,
                    0.0d
            );
        } else {
            float angle = SPEED * FastMathUtil.toRadians(ticks);
            float x = RANGE * FastMathUtil.cos(angle);
            float z = RANGE * FastMathUtil.sin(angle);

            location.getWorld().spawnParticle(
                    Particle.NOTE,
                    location.add(x, 2.2d, z),
                    0,
                    color / 24.0d,
                    0.0d,
                    0.0d
            );

            if (ticks++ > 360) {
                ticks = 0;
            }
        }

        if (color++ > 23) {
            color = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
        // No unequip behavior
    }
}