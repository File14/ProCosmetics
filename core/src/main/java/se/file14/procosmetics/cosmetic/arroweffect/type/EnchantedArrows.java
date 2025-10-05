package se.file14.procosmetics.cosmetic.arroweffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;

public class EnchantedArrows implements ArrowEffectBehavior {

    private static final double UPDATE_SPREAD = 0.2d;
    private static final double HIT_SPREAD = 0.5d;

    @Override
    public void onEquip(CosmeticContext<ArrowEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.ENCHANT,
                location,
                10,
                UPDATE_SPREAD,
                UPDATE_SPREAD,
                UPDATE_SPREAD,
                0.2d
        );
    }

    @Override
    public void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.ENCHANT,
                location,
                50,
                HIT_SPREAD,
                HIT_SPREAD,
                HIT_SPREAD,
                0.2d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<ArrowEffectType> context) {
    }
}