package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player purchases gadget ammo.
 */
public interface PlayerPurchaseGadgetAmmoEvent extends ProCosmeticsEvent {

    /**
     * Gets the user for this event.
     *
     * @return the {@link User} instance
     */
    User getUser();

    /**
     * Gets the player for this event.
     *
     * @return the {@link Player} instance
     */
    Player getPlayer();

    /**
     * Gets the gadget type for this event.
     *
     * @return the {@link GadgetType} instance
     */
    GadgetType getGadgetType();
}