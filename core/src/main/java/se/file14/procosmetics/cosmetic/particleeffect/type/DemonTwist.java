package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class DemonTwist implements ParticleEffectBehavior {

    private int ticks;
    private float angle;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, 0.2d, 0.0d);

        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(
                    Particle.FLAME,
                    location.add(0.0d, 0.2d, 0.0d),
                    1,
                    0,
                    0,
                    0,
                    0.1d
            );
        } else {
            float range = 2.0f * FastMathUtil.sin(4.0f * FastMathUtil.toRadians(ticks));

            if (ticks++ >= 180) {
                ticks = 0;
            }

            angle += 0.15f;
            if (angle > Math.PI) {
                angle = 0;
            }
            float x = range * FastMathUtil.cos(angle);
            float z = range * FastMathUtil.sin(angle);

            location.add(x, 0.0d, z);
            location.getWorld().spawnParticle(Particle.FLAME, location, 2, 0, 0, 0, 0.0d);
            location.subtract(2.0d * x, 0.0d, 2.0d * z);
            location.getWorld().spawnParticle(Particle.FLAME, location, 2, 0, 0, 0, 0.0d);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}