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

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player attempts to use a gadget.
 */
public class PlayerPreUseGadgetEvent extends ProCosmeticsEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final User user;
    private final Player player;
    private final Gadget gadget;
    private boolean cancelled;

    /**
     * Constructs a new PlayerPreUseGadgetEvent.
     *
     * @param plugin the ProCosmetics plugin instance
     * @param user   the user who is attempting to use the gadget
     * @param player the player who is attempting to use the gadget
     * @param gadget the gadget being used
     */
    public PlayerPreUseGadgetEvent(ProCosmetics plugin, User user, Player player, Gadget gadget) {
        super(plugin);
        this.user = user;
        this.player = player;
        this.gadget = gadget;
    }

    /**
     * Gets the user for this event.
     *
     * @return the {@link User} instance
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the player for this event.
     *
     * @return the {@link Player} instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the gadget for this event.
     *
     * @return the {@link Gadget} instance
     */
    public Gadget getGadget() {
        return gadget;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
