package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.material.Materials;

public class PartyTime implements ParticleEffectBehavior {

    private static final float SPEED = 14.0f;
    private static final float RANGE = 0.8f;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.add(0.0d, 0.6d, 0.0d);
            location.getWorld().spawnParticle(Particle.ITEM,
                    location,
                    3,
                    0.1d,
                    0.1d,
                    0.1d,
                    0.0d,
                    Materials.getRandomInkItem()
            );
        } else {
            float angle = SPEED * FastMathUtil.toRadians(ticks);
            float x = RANGE * FastMathUtil.cos(angle);
            float z = RANGE * FastMathUtil.sin(angle);
            location.add(x, 2.4d, z);

            for (int i = 0; i < 3; i++) {
                location.getWorld().spawnParticle(Particle.ITEM,
                        location,
                        2,
                        0.2d,
                        0.2d,
                        0.2d,
                        0.0d,
                        Materials.getRandomInkItem()
                );
            }

            if (++ticks > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}