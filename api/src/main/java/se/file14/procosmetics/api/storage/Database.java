package se.file14.procosmetics.api.storage;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Provides database operations for user data persistence.
 * This interface handles loading and saving user data including cosmetic ownership,
 * equipped cosmetics, coins, gadget ammunition, treasure keys, and user preferences.
 * Operations are available in both synchronous and asynchronous variants.
 *
 * <p>Async methods return {@link CompletableFuture} instances that can be used for
 * non-blocking operations. Synchronous methods should be used cautiously to avoid
 * blocking the main server thread.</p>
 */
public interface Database {

    /**
     * Gets the database type identifier.
     *
     * @return The database type (e.g., "MySQL")
     */
    String getType();

    /**
     * Loads a user by their UUID synchronously.
     *
     * @param uuid The user's UUID
     * @return The loaded user, or null if not found
     */
    @Nullable
    User loadUser(UUID uuid);

    /**
     * Loads a user by their name synchronously.
     *
     * @param name The user's name
     * @return The loaded user, or null if not found
     */
    @Nullable
    User loadUser(String name);

    /**
     * Loads a user by their internal ID synchronously.
     *
     * @param id The user's internal ID
     * @return The loaded user, or null if not found
     */
    @Nullable
    User loadUser(int id);

    /**
     * Loads a user by their UUID asynchronously.
     *
     * @param uuid The user's UUID
     * @return A CompletableFuture containing the loaded user, or null if not found
     */
    default CompletableFuture<@Nullable User> loadUserAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> loadUser(uuid));
    }

    /**
     * Loads a user by their Player object synchronously.
     *
     * @param player The player
     * @return The loaded user, or null if not found
     */
    @Nullable
    default User loadUser(Player player) {
        return loadUser(player.getUniqueId());
    }

    /**
     * Loads a user by their Player object asynchronously.
     *
     * @param player The player
     * @return A CompletableFuture containing the loaded user, or null if not found
     */
    default CompletableFuture<@Nullable User> loadUserAsync(Player player) {
        return loadUserAsync(player.getUniqueId());
    }

    /**
     * Loads a user by their name asynchronously.
     *
     * @param name The user's name
     * @return A CompletableFuture containing the loaded user, or null if not found
     */
    default CompletableFuture<@Nullable User> loadUserAsync(String name) {
        return CompletableFuture.supplyAsync(() -> loadUser(name));
    }

    /**
     * Loads a user by their internal ID asynchronously.
     *
     * @param id The user's internal ID
     * @return A CompletableFuture containing the loaded user, or null if not found
     */
    default CompletableFuture<@Nullable User> loadUserAsync(int id) {
        return CompletableFuture.supplyAsync(() -> loadUser(id));
    }

    /**
     * Creates a new user entry in the database.
     *
     * @param uuid The user's UUID
     * @param name The user's name
     * @return The created user, or null if creation failed
     */
    @Nullable
    User insertUser(UUID uuid, String name);

    /**
     * Updates a user's name in the database.
     *
     * @param user The user to update
     * @param name The new name
     */
    void updateName(User user, String name);

    /**
     * Updates a user's name in the database asynchronously.
     *
     * @param user The user to update
     * @param name The new name
     * @return A CompletableFuture that completes when the update is done
     */
    default CompletableFuture<Void> updateNameAsync(User user, String name) {
        return CompletableFuture.runAsync(() -> updateName(user, name));
    }

    /**
     * Updates the equipped cosmetic entry for a user in the database asynchronously.
     * <p>
     * This method does not directly equip the cosmetic in-game, it only saves the user's
     * currently equipped cosmetic for the given category in persistent storage.
     * Any previously equipped cosmetic in the same category is automatically replaced.
     *
     * @param user         The user whose equipped cosmetic should be updated
     * @param cosmeticType The cosmetic type to store as equipped
     * @return A {@link CompletableFuture} that completes with {@code true} if the operation succeeded,
     * or {@code false} otherwise
     */
    CompletableFuture<Boolean> saveEquippedCosmeticAsync(User user, CosmeticType<?, ?> cosmeticType);

    /**
     * Removes the equipped cosmetic entry for a user in the specified category from the database asynchronously.
     * <p>
     * This method does not directly unequip the cosmetic in-game, it only removes the stored
     * "equipped" record from persistent storage.
     *
     * @param user     The user whose equipped cosmetic should be cleared
     * @param category The cosmetic category to clear
     * @return A {@link CompletableFuture} that completes with {@code true} if the operation succeeded,
     * or {@code false} otherwise
     */
    CompletableFuture<Boolean> removeEquippedCosmeticAsync(User user, CosmeticCategory<?, ?, ?> category);

    CompletableFuture<Boolean> setSelfViewMorphAsync(User user, boolean enabled);

    CompletableFuture<Boolean> setSelfViewStatusAsync(User user, boolean enabled);

    /**
     * Adds coins to a user's balance asynchronously.
     * On success, the user's in-memory balance is automatically updated on the main thread.
     *
     * @param user   The user
     * @param amount The amount to add
     * @return A CompletableFuture containing a pair: success status and new balance from database
     */
    CompletableFuture<BooleanIntPair> addCoinsAsync(User user, int amount);

    /**
     * Removes coins from a user's balance asynchronously.
     * This operation only succeeds if the user has sufficient coins.
     * On success, the user's in-memory balance is automatically updated on the main thread.
     *
     * @param user   The user
     * @param amount The amount to remove
     * @return A CompletableFuture containing a pair: success status and new balance from database
     */
    CompletableFuture<BooleanIntPair> removeCoinsAsync(User user, int amount);

    /**
     * Sets a user's coin balance asynchronously.
     * This operation fails if the amount is negative.
     * On success, the user's in-memory balance is automatically updated on the main thread.
     *
     * @param user   The user
     * @param amount The new balance (must be non-negative)
     * @return A CompletableFuture containing a pair: success status and new balance from database
     */
    CompletableFuture<BooleanIntPair> setCoinsAsync(User user, int amount);

    /**
     * Adds gadget ammunition to a user's inventory asynchronously.
     * On success, the user's in-memory ammo count is automatically updated on the main thread.
     *
     * @param user       The user
     * @param gadgetType The gadget type
     * @param amount     The amount to add
     * @return A CompletableFuture containing a pair: success status and new ammo count from database
     */
    CompletableFuture<BooleanIntPair> addGadgetAmmoAsync(User user, GadgetType gadgetType, int amount);

    /**
     * Removes gadget ammunition from a user's inventory asynchronously.
     * This operation only succeeds if the user has sufficient ammunition.
     * On success, the user's in-memory ammo count is automatically updated on the main thread.
     *
     * @param user       The user
     * @param gadgetType The gadget type
     * @param amount     The amount to remove
     * @return A CompletableFuture containing a pair: success status and new ammo count from database
     */
    CompletableFuture<BooleanIntPair> removeGadgetAmmoAsync(User user, GadgetType gadgetType, int amount);

    /**
     * Sets a user's gadget ammunition count asynchronously.
     * This operation fails if the amount is negative.
     * On success, the user's in-memory ammo count is automatically updated on the main thread.
     *
     * @param user       The user
     * @param gadgetType The gadget type
     * @param amount     The new ammo count (must be non-negative)
     * @return A CompletableFuture containing a pair: success status and new ammo count from database
     */
    CompletableFuture<BooleanIntPair> setGadgetAmmoAsync(User user, GadgetType gadgetType, int amount);

    /**
     * Adds treasure chest keys to a user's inventory asynchronously.
     * On success, the user's in-memory key count is automatically updated on the main thread.
     *
     * @param user          The user
     * @param treasureChest The treasure chest type
     * @param amount        The amount to add
     * @return A CompletableFuture containing a pair: success status and new key count from database
     */
    CompletableFuture<BooleanIntPair> addTreasureKeysAsync(User user, TreasureChest treasureChest, int amount);

    /**
     * Removes treasure chest keys from a user's inventory asynchronously.
     * This operation only succeeds if the user has sufficient keys.
     * On success, the user's in-memory key count is automatically updated on the main thread.
     *
     * @param user          The user
     * @param treasureChest The treasure chest type
     * @param amount        The amount to remove
     * @return A CompletableFuture containing a pair: success status and new key count from database
     */
    CompletableFuture<BooleanIntPair> removeTreasureKeysAsync(User user, TreasureChest treasureChest, int amount);

    /**
     * Sets a user's treasure chest key count asynchronously.
     * This operation fails if the amount is negative.
     * On success, the user's in-memory key count is automatically updated on the main thread.
     *
     * @param user          The user
     * @param treasureChest The treasure chest type
     * @param amount        The new key count (must be non-negative)
     * @return A CompletableFuture containing a pair: success status and new key count from database
     */
    CompletableFuture<BooleanIntPair> setTreasureKeysAsync(User user, TreasureChest treasureChest, int amount);

    /**
     * Shuts down the database connection and releases resources.
     */
    void shutdown();
}
