package se.file14.procosmetics.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a cosmetic-related server-entity is spawned for a player.
 */
public interface CosmeticEntitySpawnEvent extends ProCosmeticsEvent {

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
     * Gets the cosmetic-related entity that was spawned.
     *
     * @return the spawned {@link Entity}
     */
    Entity getEntity();
}