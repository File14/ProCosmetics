package se.file14.procosmetics.api.cosmetic.miniature;

import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a miniature cosmetic instance associated with a user.
 * Miniatures are small companion entities that follow the player around.
 */
public interface Miniature extends Cosmetic<MiniatureType, MiniatureBehavior> {

    /**
     * Gets the NMS entity representing this miniature.
     *
     * @return the NMS entity for this miniature
     */
    NMSEntity getNMSEntity();
}
