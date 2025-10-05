package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class FlameOfMagic implements ParticleEffectBehavior {

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
            location.getWorld().spawnParticle(Particle.WITCH, location, 0, 0.0d, 1.0d, 0.0d, 0.2d);
            location.subtract(2.0d * x, 0.0d, 2.0d * z);
            location.getWorld().spawnParticle(Particle.WITCH, location, 0, 0.0d, 1.0d, 0.0d, 0.2d);
        } else {
            location.getWorld().spawnParticle(Particle.WITCH, location, 2, 0.0d, 0, 0, 0.3);

            location.add(0.0d, 2.5d, 0.0d);
            float speed = 8.0f;
            angle = speed * FastMathUtil.toRadians(ticks);
            range = 1.5f;
            x = range * FastMathUtil.cos(angle);
            z = range * FastMathUtil.sin(angle);

            location.getWorld().spawnParticle(Particle.FLAME, location, 0, x, -1.5d, z, 0.1d);
            location.getWorld().spawnParticle(Particle.FLAME, location, 0, -x, -1.5d, -z, 0.1d);

            if (ticks++ > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}