package se.file14.procosmetics.api.cosmetic.balloon;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a balloon cosmetic instance associated with a user.
 * Balloons are floating entities that follow the player, connected
 * by a leash, providing a decorative companion effect.
 */
public interface Balloon extends Cosmetic<BalloonType, BalloonBehavior> {

    /**
     * Gets the entity tracker responsible for managing this balloon's visibility
     * and updates to nearby players.
     *
     * @return the entity tracker for this balloon
     */
    EntityTracker getTracker();

    /**
     * Gets the NMS entity representing the balloon itself.
     * This is the main floating entity that appears above the player.
     *
     * @return the NMS entity for the balloon
     */
    NMSEntity getNMSEntity();

    /**
     * Gets the NMS entity used as the leash anchor point for non-living-entities.
     *
     * @return the leash entity, or null if not used
     */
    @Nullable
    NMSEntity getLeashEntity();
}
