/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
     * @throws IllegalStateException if the economy system cannot be hooked
     */
    void hook(ProCosmetics plugin) throws IllegalStateException;

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

    /**
     * Gets the user's current coin balance asynchronously.
     *
     * @param user the user to get the balance for
     * @return a {@link CompletableFuture} resolving to a {@link BooleanIntPair}
     * containing the success status and balance value
     */
    CompletableFuture<BooleanIntPair> getCoinsAsync(User user);

    /**
     * Checks asynchronously whether a user has at least the specified number of coins.
     *
     * @param user   the user to check
     * @param amount the required minimum amount
     * @return a {@link CompletableFuture} resolving to {@code true} if the user has enough coins,
     * otherwise {@code false}
     */
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
                Placeholder.unparsed("amount", String.valueOf(missing)),
                Placeholder.unparsed("currency", user.translateRaw("generic.currency"))
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

    /**
     * Sets a user's coin balance to a specific value asynchronously.
     *
     * @param user   the user whose balance should be modified
     * @param amount the new balance amount
     * @return a {@link CompletableFuture} resolving to {@code true} if the operation succeeded
     */
    CompletableFuture<Boolean> setCoinsAsync(User user, int amount);

    /**
     * Removes coins from a user's balance asynchronously.
     *
     * @param user   the user to remove coins from
     * @param amount the amount of coins to remove
     * @return a {@link CompletableFuture} resolving to {@code true} if the operation succeeded
     */
    CompletableFuture<Boolean> removeCoinsAsync(User user, int amount);
}
