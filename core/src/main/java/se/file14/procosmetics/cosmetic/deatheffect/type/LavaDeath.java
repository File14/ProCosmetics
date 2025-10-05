package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;

public class LavaDeath implements DeathEffectBehavior {

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.LAVA, location, 10);
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}