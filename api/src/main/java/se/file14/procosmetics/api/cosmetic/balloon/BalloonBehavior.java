package se.file14.procosmetics.api.cosmetic.balloon;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for balloon cosmetics.
 *
 * @see CosmeticBehavior
 * @see BalloonType
 */
public interface BalloonBehavior extends CosmeticBehavior<BalloonType> {

    /**
     * Called every tick to update the balloon's state.
     *
     * @param context  the context containing information about the balloon
     * @param location the location where the balloon is
     */
    void onUpdate(CosmeticContext<BalloonType> context, Location location);
}
