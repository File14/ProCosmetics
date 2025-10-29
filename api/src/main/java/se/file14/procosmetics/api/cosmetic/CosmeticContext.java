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
