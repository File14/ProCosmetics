package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player attempts to equip a cosmetic before the
 * equip action is actually applied.
 */
public interface PlayerPreEquipCosmeticEvent extends ProCosmeticsEvent, Cancellable {

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