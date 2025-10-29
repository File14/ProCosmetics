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
package se.file14.procosmetics.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * Provides static access to the ProCosmetics API instance.
 * This class serves as a singleton provider for accessing the main plugin API
 * from external plugins or internal components.
 *
 * <p>Example usage:
 * <pre>{@code
 * ProCosmetics api = ProCosmeticsProvider.get();
 * UserManager userManager = api.getUserManager();
 * }</pre>
 */
public final class ProCosmeticsProvider {

    private static ProCosmetics plugin;

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws UnsupportedOperationException always, as this class cannot be instantiated
     */
    private ProCosmeticsProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Gets the ProCosmetics API instance.
     *
     * @return The ProCosmetics instance
     * @throws IllegalStateException if ProCosmetics has not been registered yet
     */
    public static ProCosmetics get() {
        if (plugin == null) {
            throw new IllegalStateException("ProCosmetics is not loaded yet!");
        }
        return plugin;
    }

    /**
     * Registers the active ProCosmetics API instance.
     *
     * @param plugin the active {@link ProCosmetics} instance
     */
    @ApiStatus.Internal
    public static void register(ProCosmetics plugin) {
        ProCosmeticsProvider.plugin = plugin;
    }

    /**
     * Unregisters the ProCosmetics API instance.
     */
    @ApiStatus.Internal
    public static void unregister() {
        plugin = null;
    }
}
