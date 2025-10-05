package se.file14.procosmetics.api.cosmetic.status;

import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a status cosmetic instance associated with a user.
 * Status cosmetics display dynamic text above the player,
 * such as custom titles, statistics, or other information that can update in real-time.
 */
public interface Status extends Cosmetic<StatusType, StatusBehavior> {

    /**
     * Gets the NMS entity used to display the status text.
     *
     * @return the NMS entity for this status display
     */
    NMSEntity getNMSEntity();
}
