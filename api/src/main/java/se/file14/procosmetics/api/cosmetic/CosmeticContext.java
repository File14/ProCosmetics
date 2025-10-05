package se.file14.procosmetics.api.cosmetic;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.user.User;

/**
 * Provides contextual information for cosmetic operations.
 *
 * @param <T> the specific cosmetic type
 */
public interface CosmeticContext<T extends CosmeticType<T, ?>> {

    /**
     * Gets the ProCosmetics plugin instance.
     *
     * @return the plugin instance
     */
    ProCosmetics getPlugin();

    /**
     * Gets the user associated with this context.
     *
     * @return the user
     */
    User getUser();

    /**
     * Gets the Bukkit player associated with this context.
     *
     * @return the player
     */
    Player getPlayer();

    /**
     * Gets the cosmetic type for this context.
     *
     * @return the cosmetic type
     */
    T getType();
}
