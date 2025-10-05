package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;

public class SnowCloud implements ParticleEffectBehavior {

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(Particle.SNOWFLAKE,
                    location.add(0.0d, 0.4d, 0.0d),
                    2,
                    0.0d,
                    0.0d,
                    0.0d,
                    0.0d
            );
        } else {
            location.getWorld().spawnParticle(Particle.CLOUD,
                    location.add(0.0d, 2.8d, 0.0d),
                    4,
                    0.5d,
                    0.1d,
                    0.5d,
                    0.0d
            );
            location.getWorld().spawnParticle(Particle.SNOWFLAKE,
                    location,
                    2,
                    0.25d,
                    0.05d,
                    0.25d,
                    0.0d
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}