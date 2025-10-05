package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class InLove implements ParticleEffectBehavior {

    private static final float SPEED = 30.0f;
    private static final float RANGE = 0.6f;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(Particle.HEART, location.clone().add(0.0d, 0.6d, 0.0d), 0);
        } else {
            float angle = SPEED * FastMathUtil.toRadians(ticks);

            location.getWorld().spawnParticle(Particle.HEART,
                    location.add(
                            RANGE * FastMathUtil.cos(angle),
                            2.1d,
                            RANGE * FastMathUtil.sin(angle)
                    ),
                    0
            );

            if (++ticks > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}