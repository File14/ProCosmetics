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
package se.file14.procosmetics.api.event;

import org.bukkit.event.Event;
import se.file14.procosmetics.api.ProCosmetics;

/**
 * A superinterface for all ProCosmetics events.
 */
public abstract class ProCosmeticsEvent extends Event {

    private final ProCosmetics plugin;

    /**
     * Constructs a new ProCosmeticsEvent.
     *
     * @param plugin the ProCosmetics plugin instance
     */
    public ProCosmeticsEvent(ProCosmetics plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the API instance this event was dispatched from.
     *
     * @return the api instance
     */
    public ProCosmetics getPlugin() {
        return plugin;
    }
}
