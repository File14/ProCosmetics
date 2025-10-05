package se.file14.procosmetics.cosmetic.arroweffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.util.MathUtil;

public class MusicArrows implements ArrowEffectBehavior {

    private static final int HIT_AMOUNT = 15;
    private static final double HIT_SPREAD = 1.0d;

    @Override
    public void onEquip(CosmeticContext<ArrowEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.NOTE,
                location,
                0,
                MathUtil.randomRange(0.0d, 24.0d),
                0.0f,
                0.0f
        );
    }

    @Override
    public void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location) {
        for (int i = 0; i < HIT_AMOUNT; ++i) {
            location.getWorld().spawnParticle(Particle.NOTE,
                    location.getX() + MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    location.getY() + MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    location.getZ() + MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    0,
                    MathUtil.randomRange(0.0d, 24.0d),
                    0.0f,
                    0.0f
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ArrowEffectType> context) {
    }
}