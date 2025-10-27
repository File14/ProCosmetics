package se.file14.procosmetics.api.user;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Manages user instances and provides various methods for retrieving users.
 *
 * <p>Access patterns:
 * <ul>
 *   <li><b>Connected</b> - Only returns users currently online</li>
 *   <li><b>ConnectedOrCached</b> - Returns online users or recently cached offline users</li>
 *   <li><b>Get</b> - Loads from database if not found in memory (synchronous)</li>
 *   <li><b>GetAsync</b> - Loads from database if not found in memory (asynchronous)</li>
 * </ul>
 */
public interface UserManager {

    /**
     * Gets all currently connected users.
     *
     * @return An immutable collection of all online users
     */
    Collection<? extends User> getAllConnected();

    /**
     * Gets a connected user by their UUID.
     *
     * @param uuid The user's UUID
     * @return The connected user, or null if not online
     */
    @Nullable
    User getConnected(@Nullable UUID uuid);

    /**
     * Gets a connected user by their Player object.
     *
     * @param player The player
     * @return The connected user, or null if player is null or not online
     */
    @Nullable
    default User getConnected(@Nullable Player player) {
        if (player == null) {
            return null;
        }
        return getConnected(player.getUniqueId());
    }

    /**
     * Gets a connected user by their name (case-insensitive).
     * This method iterates through all connected users.
     *
     * @param name The user's name
     * @return The connected user, or null if name is null or user is not online
     */
    @Nullable
    default User getConnected(@Nullable String name) {
        if (name == null) {
            return null;
        }

        for (User user : getAllConnected()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets a connected user by their internal database ID.
     * This method iterates through all connected users.
     *
     * @param id The user's database ID
     * @return The connected user, or null if not online
     */
    @Nullable
    default User getConnected(int id) {
        for (User user : getAllConnected()) {
            if (user.getDatabaseId() == id) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets a user by their UUID from connected users or cache.
     * This checks online users first, then falls back to recently cached offline users.
     *
     * @param uuid The user's UUID
     * @return The user if connected or cached, or null otherwise
     */
    @Nullable
    User getConnectedOrCached(@Nullable UUID uuid);

    /**
     * Gets a user by their Player object from connected users or cache.
     *
     * @param player The player
     * @return The user if connected or cached, or null if player is null or user not found
     */
    @Nullable
    default User getConnectedOrCached(@Nullable Player player) {
        if (player == null) {
            return null;
        }
        return getConnectedOrCached(player.getUniqueId());
    }

    /**
     * Gets a user by their name from connected users or cache.
     *
     * @param name The user's name
     * @return The user if connected or cached, or null otherwise
     */
    @Nullable
    User getConnectedOrCached(@Nullable String name);

    /**
     * Gets a user by their database ID from connected users or cache.
     *
     * @param id The user's database ID
     * @return The user if connected or cached, or null otherwise
     */
    @Nullable
    User getConnectedOrCached(int id);

    /**
     * Gets a user by their UUID, loading from database if necessary (synchronous).
     * This checks memory first, then loads from the database if not found.
     * <b>Warning:</b> This method blocks the current thread during database access.
     *
     * @param uuid The user's UUID
     * @return The user, or null if not found in database
     */
    @Nullable
    User get(@Nullable UUID uuid);

    /**
     * Gets a user by their name, loading from database if necessary (synchronous).
     * This checks memory first, then loads from the database if not found.
     * <b>Warning:</b> This method blocks the current thread during database access.
     *
     * @param name The user's name
     * @return The user, or null if not found in database
     */
    @Nullable
    User get(@Nullable String name);

    /**
     * Gets a user by their database ID, loading from database if necessary (synchronous).
     * This checks memory first, then loads from the database if not found.
     * <b>Warning:</b> This method blocks the current thread during database access.
     *
     * @param id The user's database ID
     * @return The user, or null if not found in database
     */
    @Nullable
    User get(int id);

    /**
     * Gets a user by their UUID asynchronously, loading from database if necessary.
     * This checks memory first, then loads from the database if not found.
     *
     * @param uuid The user's UUID
     * @return A CompletableFuture containing the user, or null if not found in database
     */
    CompletableFuture<@Nullable User> getAsync(@Nullable UUID uuid);

    /**
     * Gets a user by their name asynchronously, loading from database if necessary.
     * This checks memory first, then loads from the database if not found.
     *
     * @param name The user's name
     * @return A CompletableFuture containing the user, or null if not found in database
     */
    CompletableFuture<@Nullable User> getAsync(@Nullable String name);

    /**
     * Gets a user by their database ID asynchronously, loading from database if necessary.
     * This checks memory first, then loads from the database if not found.
     *
     * @param id The user's database ID
     * @return A CompletableFuture containing the user, or null if not found in database
     */
    CompletableFuture<@Nullable User> getAsync(int id);
}
