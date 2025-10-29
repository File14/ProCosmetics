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

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents a clickable item in a menu with a custom click handler.
 * Implementations of this interface define the behavior that occurs when
 * a player clicks on an item in a menu inventory.
 */
public interface ClickableItem {

    /**
     * Handles the click event when a player clicks on this item.
     *
     * @param event the inventory click event containing information about the click
     */
    void handle(InventoryClickEvent event);
}
