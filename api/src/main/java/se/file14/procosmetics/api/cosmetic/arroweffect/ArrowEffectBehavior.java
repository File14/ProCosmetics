package se.file14.procosmetics.api.cosmetic.arroweffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for arrow effect cosmetics.
 *
 * @see CosmeticBehavior
 * @see ArrowEffectType
 */
public interface ArrowEffectBehavior extends CosmeticBehavior<ArrowEffectType> {

    /**
     * Called every tick when the arrow is in flight.
     *
     * @param context  the context containing information about the arrow effect
     * @param location the current location of the arrow
     */
    void onUpdate(CosmeticContext<ArrowEffectType> context, Location location);

    /**
     * Called when the arrow hits a target or surface.
     *
     * @param context  the context containing information about the arrow effect
     * @param location the location where the arrow hit
     */
    void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location);
}
