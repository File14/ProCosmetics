package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player purchases a treasure chest.
 */
public interface PlayerPurchaseTreasureChestEvent extends ProCosmeticsEvent {

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
     * Gets the treasure chest for this event.
     *
     * @return the {@link TreasureChest} instance
     */
    TreasureChest getTreasureChest();
}