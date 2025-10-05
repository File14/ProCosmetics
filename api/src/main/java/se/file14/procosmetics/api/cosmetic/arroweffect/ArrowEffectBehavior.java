package se.file14.procosmetics.api.cosmetic.arroweffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface ArrowEffectBehavior extends CosmeticBehavior<ArrowEffectType> {

    /**
     * Called when the arrow is in flight
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
