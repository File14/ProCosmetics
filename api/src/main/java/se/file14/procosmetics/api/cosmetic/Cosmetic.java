package se.file14.procosmetics.api.cosmetic;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.user.User;

/**
 * Represents an instance of a cosmetic that is associated with a specific user.
 * This interface manages the equipped state of a cosmetic and provides access
 * to the cosmetic's type and behavior implementations.
 *
 * @param <T> the specific cosmetic type
 * @param <B> the behavior associated with this cosmetic type
 */
public interface Cosmetic<T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>> {

    /**
     * Equips this cosmetic to the associated user.
     *
     * @param silent         whether to suppress equip notifications
     * @param saveToDatabase whether to persist the equip state to the database
     */
    void equip(boolean silent, boolean saveToDatabase);

    /**
     * Unequips this cosmetic from the associated user.
     *
     * @param silent         whether to suppress unequip notifications
     * @param saveToDatabase whether to persist the unequip state to the database
     */
    void unequip(boolean silent, boolean saveToDatabase);

    /**
     * Checks if this cosmetic is currently equipped.
     *
     * @return true if equipped, false otherwise
     */
    boolean isEquipped();

    /**
     * Gets the user this cosmetic is associated with.
     *
     * @return the user
     */
    User getUser();

    /**
     * Gets the Bukkit player this cosmetic is associated with. Null before equip.
     *
     * @return the player
     */
    Player getPlayer();

    /**
     * Gets the type of this cosmetic.
     *
     * @return the cosmetic type
     */
    T getType();

    /**
     * Gets the behavior implementation for this cosmetic.
     *
     * @return the cosmetic behavior
     */
    B getBehavior();
}
