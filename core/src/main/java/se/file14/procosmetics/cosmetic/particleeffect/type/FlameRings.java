package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class FlameRings implements ParticleEffectBehavior {

    private static final float SPEED = 10.0f;

    private int ticks;
    private final Vector vector = new Vector();

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            float angle = FastMathUtil.toRadians(location.getYaw());
            float range = 0.1f;
            float x = range * FastMathUtil.cos(angle);
            float z = range * FastMathUtil.sin(angle);

            location.getWorld().spawnParticle(
                    Particle.FLAME,
                    location.add(x, 0.0d, z),
                    1,
                    0.3d,
                    0.0d,
                    0.6d,
                    0.0d
            );
            location.getWorld().spawnParticle(
                    Particle.FLAME,
                    location.subtract(2.0d * x, 0.0d, 2.0d * z),
                    1,
                    0.3d,
                    0.0d,
                    0.6d,
                    0.0d
            );
        } else {
            location.add(0.0d, 1.0d, 0.0d);

            float angle = SPEED * FastMathUtil.toRadians(ticks);
            float x = FastMathUtil.cos(angle);
            float z = FastMathUtil.sin(angle);

            vector.setX(x).setZ(z);

            MathUtil.rotateAroundAxisZ(vector, 45.0d);
            MathUtil.rotateAroundAxisY(vector, -FastMathUtil.toRadians(location.getYaw()));

            location.getWorld().spawnParticle(Particle.FLAME, location.add(vector), 0);

            if (++ticks > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
        // No unequip behavior
    }
}