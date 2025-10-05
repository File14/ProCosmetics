package se.file14.procosmetics.cosmetic.arroweffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;

public class LoveArrows implements ArrowEffectBehavior {

    private static final int HIT_AMOUNT = 4;
    private static final double HIT_SPREAD = 0.5d;

    @Override
    public void onEquip(CosmeticContext<ArrowEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.HEART, location, 1);
    }

    @Override
    public void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.HEART,
                location,
                HIT_AMOUNT,
                HIT_SPREAD,
                HIT_SPREAD,
                HIT_SPREAD
        );
    }

    @Override
    public void onUnequip(CosmeticContext<ArrowEffectType> context) {
    }
}