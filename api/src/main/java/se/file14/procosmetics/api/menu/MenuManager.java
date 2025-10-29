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
package se.file14.procosmetics.api.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Manages menu instances and tracks which menus are currently open for players.
 * This interface provides functionality for menu registration and retrieval.
 */
public interface MenuManager {

    /**
     * Registers a menu instance for internal tracking.
     *
     * @param menu the {@link Menu} instance to register
     */
    @ApiStatus.Internal
    void register(Menu menu);

    /**
     * Gets the menu that is currently open for the specified player.
     *
     * @param player the player to check
     * @return the menu currently open for the player, or null if no menu is open
     */
    @Nullable
    Menu getOpenMenu(Player player);
}
