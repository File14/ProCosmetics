package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Called when a player unequips a cosmetic.
 */
public interface PlayerUnequipCosmeticEvent extends ProCosmeticsEvent {

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