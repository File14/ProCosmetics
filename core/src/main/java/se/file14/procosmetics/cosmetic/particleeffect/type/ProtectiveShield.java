package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class ProtectiveShield implements ParticleEffectBehavior {

    private static final float RANGE = 1.3f;

    private int i;
    private float step;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(Particle.ENCHANTED_HIT,
                    location.add(0.0d, 0.4d, 0.0d),
                    3,
                    0,
                    0,
                    0,
                    0.0d
            );
            step = 0;
            i = 0;
        } else {
            i++;

            if (i >= 40) {
                i = 0;
            } else if (i <= 20) {
                step += FastMathUtil.PI / 10.0f;

                for (float angle = 0; angle <= 2.0f * FastMathUtil.PI; angle += FastMathUtil.PI / 15.0f) {
                    float sinStep = FastMathUtil.sin(step);
                    float x = RANGE * FastMathUtil.cos(angle) * sinStep;
                    float y = RANGE * FastMathUtil.cos(step) + 1.4f;
                    float z = RANGE * FastMathUtil.sin(angle) * sinStep;

                    location.add(x, y, z);
                    location.getWorld().spawnParticle(Particle.ENCHANTED_HIT, location, 1, 0, 0, 0, 0.0d);
                    location.subtract(x, y, z);
                }

                if (step > 8 * Math.PI) {
                    step = 0;
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}