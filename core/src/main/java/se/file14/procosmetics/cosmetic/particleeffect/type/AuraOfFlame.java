package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class AuraOfFlame implements ParticleEffectBehavior {

    private static final Vector PARTICLE_MOTION = new Vector(0.0d, 0.4d, 0.0d);

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
            range = 0.1f;
            x = range * FastMathUtil.cos(angle);
            z = range * FastMathUtil.sin(angle);

            location.add(x, 0.1d, z);
            location.getWorld().spawnParticle(Particle.FLAME,
                    location,
                    0,
                    PARTICLE_MOTION.getX(),
                    PARTICLE_MOTION.getY(),
                    PARTICLE_MOTION.getZ(),
                    0.3d
            );
            location.subtract(2.0d * x, 0.0d, 2.0d * z);
            location.getWorld().spawnParticle(Particle.FLAME,
                    location,
                    0,
                    PARTICLE_MOTION.getX(),
                    PARTICLE_MOTION.getY(),
                    PARTICLE_MOTION.getZ(),
                    0.3d
            );
        } else {
            location.add(0.0d, 0.3d, 0.0d);
            float speed = 12.0f;
            angle = speed * FastMathUtil.toRadians(ticks);
            range = 0.8f;
            x = range * FastMathUtil.cos(angle);
            z = range * FastMathUtil.sin(angle);

            location.add(x, 0.0d, z);
            location.getWorld().spawnParticle(Particle.FLAME,
                    location,
                    0,
                    PARTICLE_MOTION.getX(),
                    PARTICLE_MOTION.getY(),
                    PARTICLE_MOTION.getZ(),
                    0.2d
            );
            location.subtract(2.0d * x, 0.0d, 2.0d * z);
            location.getWorld().spawnParticle(Particle.FLAME,
                    location,
                    0,
                    PARTICLE_MOTION.getX(),
                    PARTICLE_MOTION.getY(),
                    PARTICLE_MOTION.getZ(),
                    0.2d
            );

            if (ticks++ > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}