package se.file14.procosmetics.api.cosmetic.morph;

import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a morph cosmetic instance associated with a user.
 * Morphs transform the player's appearance into a different entity,
 * allowing them to look like various creatures or mobs with optional special abilities.
 */
public interface Morph extends Cosmetic<MorphType, MorphBehavior> {

    /**
     * Gets the NMS entity representing the morphed appearance.
     *
     * @return the NMS entity for this morph
     */
    NMSEntity getNMSEntity();
}
