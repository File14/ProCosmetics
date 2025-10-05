package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.util.MathUtil;

public class EnchantedDeath implements DeathEffectBehavior {

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.add(0.0d, MathUtil.randomRange(0.8d, 2.0d), 0.0d);

        location.getWorld().spawnParticle(Particle.ENCHANT,
                location,
                150,
                0.0d,
                0.0d,
                0.0d,
                1.2d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}