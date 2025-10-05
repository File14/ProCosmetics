package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.util.MathUtil;

public class LastLove implements DeathEffectBehavior {

    private static final int AMOUNT = 8;
    private static final double SPREAD = 1.0d;

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.HEART,
                location,
                AMOUNT,
                MathUtil.randomRange(-SPREAD, SPREAD),
                MathUtil.randomRange(-SPREAD, SPREAD),
                MathUtil.randomRange(-SPREAD, SPREAD),
                0.1d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}