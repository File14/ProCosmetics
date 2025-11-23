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

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import se.file14.procosmetics.api.ProCosmetics;

/**
 * Called after the plugin has finished reloading.
 */
public class PluginReloadEvent extends ProCosmeticsEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * Constructs a new PluginReloadEvent.
     *
     * @param plugin the ProCosmetics plugin instance
     */
    public PluginReloadEvent(ProCosmetics plugin) {
        super(plugin);
    }

    /**
     * Gets the handler list for this event.
     *
     * @return the {@link HandlerList} instance
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return the {@link HandlerList} instance
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
