package se.file14.procosmetics.api.cosmetic.deatheffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for death effect cosmetics.
 *
 * @see CosmeticBehavior
 * @see DeathEffectType
 */
public interface DeathEffectBehavior extends CosmeticBehavior<DeathEffectType> {

    /**
     * Plays the death effect at the specified location.
     *
     * @param context  the context containing information about the death effect
     * @param location the location where the player died and where the effect
     *                 should be displayed
     */
    void playEffect(CosmeticContext<DeathEffectType> context, Location location);
}
