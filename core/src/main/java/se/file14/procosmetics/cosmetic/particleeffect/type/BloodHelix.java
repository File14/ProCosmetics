package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class BloodHelix implements ParticleEffectBehavior {

    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.fromRGB(250, 0, 0), 1);

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, 0.1d, 0.0d);

        if (context.getUser().isMoving()) {
            location.add(0.0d, 0.2d, 0.0d);
            location.getWorld().spawnParticle(Particle.DUST, location, 2, 0, 0, 0, 0.0d, DUST_OPTIONS);
        } else {
            float range = 1.2f;
            int size = 14;

            for (int step = 0; step < size; step++) {
                float angle = FastMathUtil.toRadians((270.0f / size * step) + ticks * 2.0f);
                float x = range * FastMathUtil.cos(angle);
                float z = range * FastMathUtil.sin(angle);
                float x2 = range * FastMathUtil.cos(angle + FastMathUtil.PI);
                float z2 = range * FastMathUtil.sin(angle + FastMathUtil.PI);

                location.add(x, 0.0d, z);
                location.getWorld().spawnParticle(Particle.DUST, location, 0, 0, 0, 0, 0.0d, DUST_OPTIONS);
                location.subtract(x, 0.0d, z);

                location.add(x2, 0.0d, z2);
                location.getWorld().spawnParticle(Particle.DUST, location, 0, 0, 0, 0, 0.0d, DUST_OPTIONS);
                location.subtract(x2, 0.0d, z2);

                location.add(0.0d, 0.2d, 0.0d);
                range -= 0.08f;
            }

            ticks++;
            if (ticks > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}