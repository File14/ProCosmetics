package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class YinAndYang implements ParticleEffectBehavior {

    private static final float SPEED = 10.0f;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        float angle;
        float range;
        float x;
        float z;

        if (context.getUser().isMoving()) {
            angle = FastMathUtil.toRadians(location.getYaw());
            range = 0.12f;
            x = range * FastMathUtil.cos(angle);
            z = range * FastMathUtil.sin(angle);

            location.add(x, 0.2d, z);
            location.getWorld().spawnParticle(Particle.FIREWORK, location, 0, 0.0d, 1.0d, 0.0d, 0.2d);
            location.subtract(2 * x, 0.0d, 2 * z);
            location.getWorld().spawnParticle(Particle.FIREWORK, location, 0, 0.0d, 1.0d, 0.0d, 0.2d);
        } else {
            location.add(0.0d, 0.1d, 0.0d);
            angle = SPEED * FastMathUtil.toRadians(ticks);
            x = FastMathUtil.sin(angle);
            z = FastMathUtil.cos(angle);

            for (int i = 0; i < 2; i++) {
                range = i == 0 ? 1.5f : -1.5f;
                float x2 = x * range;
                float z2 = z * range;

                location.getWorld().spawnParticle(Particle.FIREWORK, location, 0, x2, 0.1d, z2, 0.2d);

                location.add(x2, 0.0d, z2);
                location.getWorld().spawnParticle(Particle.SMOKE, location, 0, 0.0d, 0.1d, 0.0d, 0.1d);
                location.subtract(x2, 0.0d, z2);
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