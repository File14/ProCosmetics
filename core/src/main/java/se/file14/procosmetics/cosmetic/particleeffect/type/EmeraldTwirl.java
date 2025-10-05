package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

public class EmeraldTwirl implements ParticleEffectBehavior {

    private static final float SPEED = 10.0f;

    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        float angle = SPEED * FastMathUtil.toRadians(ticks);

        location.add(FastMathUtil.sin(angle),
                FastMathUtil.sin(0.2f * angle) + 1.2f,
                FastMathUtil.cos(angle)
        );
        location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 0);

        if (ticks++ > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}