package se.file14.procosmetics.api.cosmetic.particleeffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for particle effect cosmetics.
 *
 * @see CosmeticBehavior
 * @see ParticleEffectType
 */
public interface ParticleEffectBehavior extends CosmeticBehavior<ParticleEffectType> {

    /**
     * Called to update and display the particle effect.
     * <p>
     * This method is responsible for spawning or managing particle visuals at the given location.
     *
     * @param context  the context containing information about the particle effect cosmetic
     * @param location the location where the particle effect should be displayed
     */
    void onUpdate(CosmeticContext<ParticleEffectType> context, Location location);
}
