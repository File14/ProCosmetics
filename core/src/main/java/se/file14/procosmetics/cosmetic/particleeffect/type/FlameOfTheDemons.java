package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class FlameOfTheDemons implements ParticleEffectBehavior {

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
            x = FastMathUtil.cos(angle) * range;
            z = FastMathUtil.sin(angle) * range;

            location.add(x, 0.2d, z);
            location.getWorld().spawnParticle(Particle.FLAME, location, 0, 0.0d, 1.0d, 0.0d, 0.2d);
            location.subtract(2.0d * x, 0.0d, 2.0d * z);
            location.getWorld().spawnParticle(Particle.FLAME, location, 0, 0.0d, 1.0d, 0.0d, 0.2d);
        } else {
            float speed = 4.0f;
            location.add(0.0d, 0.2d, 0.0d);

            for (int l = 0; l < 6; l++) {
                angle = FastMathUtil.toRadians(ticks * speed + (120.0f * l));
                range = 2.4f;
                x = range * FastMathUtil.cos(angle);
                z = range * FastMathUtil.sin(angle);

                if (l < 3) {
                    float tmp = x;
                    x = z;
                    z = tmp;
                }

                location.add(x, 0.0d, z);
                Vector vector = context.getPlayer().getLocation().toVector().subtract(location.toVector());
                location.getWorld().spawnParticle(Particle.FLAME, location, 0, vector.getX(), vector.getY(), vector.getZ(), 0.05d);
                location.subtract(x, 0.0d, z);
            }

            if (ticks++ > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}
