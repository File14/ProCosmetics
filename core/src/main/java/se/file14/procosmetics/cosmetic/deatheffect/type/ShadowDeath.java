package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;

public class ShadowDeath implements DeathEffectBehavior {

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.add(0.0d, 1.0d, 0.0d);

        location.getWorld().spawnParticle(Particle.LARGE_SMOKE,
                location,
                15,
                0.5d,
                1.0d,
                0.5d,
                0.1d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}