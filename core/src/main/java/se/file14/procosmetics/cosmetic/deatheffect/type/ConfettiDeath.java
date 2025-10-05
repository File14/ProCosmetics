package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.material.Materials;

public class ConfettiDeath implements DeathEffectBehavior {

    private static final int AMOUNT = 70;
    private static final double SPREAD = 5.0d;

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.add(0.0d, 1.0d, 0.0d);

        for (int i = 0; i < AMOUNT; i++) {
            location.getWorld().spawnParticle(Particle.ITEM,
                    location,
                    0,
                    MathUtil.randomRange(-SPREAD, SPREAD),
                    MathUtil.randomRange(-SPREAD, SPREAD),
                    MathUtil.randomRange(-SPREAD, SPREAD),
                    0.1d,
                    Materials.getRandomInkItem()
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}