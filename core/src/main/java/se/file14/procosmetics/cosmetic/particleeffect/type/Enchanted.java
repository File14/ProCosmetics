package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;

public class Enchanted implements ParticleEffectBehavior {

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.add(0.0d, 0.8d, 0.0d);
            location.getWorld().spawnParticle(Particle.ENCHANT, location, 5, 0, 0, 0, 0.2d);
        } else {
            location.add(0.0d, 1.5d, 0.0d);
            location.getWorld().spawnParticle(Particle.ENCHANT, location, 5, 0, 0, 0, 1.0d);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}