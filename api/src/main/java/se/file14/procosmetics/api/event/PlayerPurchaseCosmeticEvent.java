package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player purchases a cosmetic.
 */
public interface PlayerPurchaseCosmeticEvent extends ProCosmeticsEvent {

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
     * Gets the cosmetic type for this event.
     *
     * @return the {@link CosmeticType} instance
     */
    CosmeticType<?, ?> getCosmeticType();
}