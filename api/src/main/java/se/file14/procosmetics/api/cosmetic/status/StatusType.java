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
package se.file14.procosmetics.api.cosmetic.status;

import net.kyori.adventure.text.Component;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.user.User;

import java.util.function.BiFunction;

/**
 * Represents a type of status cosmetic.
 */
public interface StatusType extends CosmeticType<StatusType, StatusBehavior> {

    /**
     * Gets the refresh interval for updating the status text in ticks.
     * This determines how frequently the text provider is called to update the display.
     *
     * @return the refresh interval in server ticks
     */
    long getRefreshInterval();

    /**
     * Gets the function that generates the status text for a user.
     * <p>
     * This function is invoked at each refresh interval to produce the current text
     * to be displayed, allowing for dynamic and context-aware content based on the
     * provided {@link StatusType} and {@link User}.
     * </p>
     *
     * @return the text provider function that takes a User and returns a Component
     */
    BiFunction<StatusType, User, Component> getTextProvider();

    /**
     * Builder interface for constructing status type instances.
     */
    interface Builder extends CosmeticType.Builder<StatusType, StatusBehavior, Builder> {

        /**
         * Sets the refresh interval for updating the status text.
         *
         * @param refreshInterval the refresh interval in server ticks
         * @return this builder for method chaining
         */
        Builder refreshInterval(long refreshInterval);

        /**
         * Sets the function that generates the status text for a user.
         *
         * @param textProvider the text provider function
         * @return this builder for method chaining
         */
        Builder textProvider(BiFunction<StatusType, User, Component> textProvider);

        /**
         * Builds and returns the configured status type instance.
         *
         * @return the built status type
         */
        @Override
        StatusType build();
    }
}
