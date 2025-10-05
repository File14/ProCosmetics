package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.util.MathUtil;

public class MusicalDeath implements DeathEffectBehavior {

    private static final int AMOUNT = 20;
    private static final double SPREAD = 1.0d;

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.add(0.0d, 1.0d, 0.0d);

        for (int i = 0; i < AMOUNT; i++) {
            location.getWorld().spawnParticle(Particle.NOTE,
                    location.getX() + MathUtil.randomRange(-SPREAD, SPREAD),
                    location.getY() + MathUtil.randomRange(-SPREAD, SPREAD),
                    location.getZ() + MathUtil.randomRange(-SPREAD, SPREAD),
                    0,
                    MathUtil.randomRange(0.0d, 24.0d),
                    0.0f,
                    0.0f
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}