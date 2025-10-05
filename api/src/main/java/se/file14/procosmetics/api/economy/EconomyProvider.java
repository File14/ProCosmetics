package se.file14.procosmetics.api.economy;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * Economy provider interface for handling coin transactions in ProCosmetics.
 * <p>
 * This interface provides both synchronous operations and asynchronous operations.
 */
public interface EconomyProvider {

    /**
     * Gets the name of the economy plugin this provider hooks into.
     *
     * @return the plugin name
     */
    String getPlugin();

    /**
     * Initializes the economy provider and establishes connection to the underlying economy system.
     *
     * @param plugin the ProCosmetics plugin instance
     * @throws EconomyFailureException if the economy system cannot be hooked
     */
    void hook(ProCosmetics plugin) throws EconomyFailureException;

    /**
     * Gets the current coin balance for a user (synchronous).
     * <p>
     * This method is used for immediate results and should return cached data.
     *
     * @param user the user to get balance for
     * @return the user's current coin balance
     */
    int getCoins(User user);

    /**
     * Checks if a user has at least the specified amount of coins (synchronous).
     *
     * @param user   the user to check
     * @param amount the minimum amount to check for
     * @return true if the user has at least the specified amount, false otherwise
     */
    default boolean hasCoins(User user, int amount) {
        return getCoins(user) >= amount;
    }

    CompletableFuture<BooleanIntPair> getCoinsAsync(User user);

    default CompletableFuture<Boolean> hasCoinsAsync(User user, int amount) {
        return getCoinsAsync(user).thenApply(result ->
                result.leftBoolean() && result.rightInt() >= amount);
    }

    /**
     * Sends an insufficient coins message to the user showing how much more they need.
     *
     * @param user the user to send the message to
     * @param cost the cost they cannot afford
     */
    default void sendInsufficientCoinsMessage(User user, int cost) {
        int missing = cost - getCoins(user);
        user.sendMessage(user.translate(
                "player.not_enough_money",
                Placeholder.unparsed("amount", String.valueOf(missing))
        ));
    }

    /**
     * Adds coins to a user's balance (asynchronous).
     *
     * @param user   the user to add coins to
     * @param amount the amount of coins to add
     * @return a CompletableFuture containing true if the operation was successful
     */
    CompletableFuture<Boolean> addCoinsAsync(User user, int amount);

    CompletableFuture<Boolean> setCoinsAsync(User user, int amount);

    CompletableFuture<Boolean> removeCoinsAsync(User user, int amount);
}
