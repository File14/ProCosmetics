package se.file14.procosmetics.api.cosmetic.particleeffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface ParticleEffectBehavior extends CosmeticBehavior<ParticleEffectType> {

    void onUpdate(CosmeticContext<ParticleEffectType> context, Location location);

}