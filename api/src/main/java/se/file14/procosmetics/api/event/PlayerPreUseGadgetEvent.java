package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player attempts to use a gadget.
 */
public interface PlayerPreUseGadgetEvent extends ProCosmeticsEvent, Cancellable {

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
     * Gets the gadget for this event.
     *
     * @return the {@link Gadget} instance
     */
    Gadget getGadget();
}