package se.file14.procosmetics.cosmetic.arroweffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.util.MathUtil;

public class SparklyArrows implements ArrowEffectBehavior {

    private static final int HIT_AMOUNT = 20;
    private static final double HIT_SPREAD = 5.0d;

    @Override
    public void onEquip(CosmeticContext<ArrowEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.FIREWORK, location, 0);
    }

    @Override
    public void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location) {
        for (int i = 0; i < HIT_AMOUNT; i++) {
            location.getWorld().spawnParticle(Particle.FIREWORK,
                    location,
                    0,
                    MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    0.1d
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ArrowEffectType> context) {
    }
}