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
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;

/**
 * Called when a player purchases gadget ammo.
 */
public class PlayerPurchaseGadgetAmmoEvent extends ProCosmeticsEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final User user;
    private final Player player;
    private final GadgetType gadgetType;

    /**
     * Constructs a new PlayerPurchaseGadgetAmmoEvent.
     *
     * @param plugin     the ProCosmetics plugin instance
     * @param user       the user who purchased the gadget ammo
     * @param player     the player who purchased the gadget ammo
     * @param gadgetType the type of gadget for which ammo was purchased
     */
    public PlayerPurchaseGadgetAmmoEvent(ProCosmetics plugin, User user, Player player, GadgetType gadgetType) {
        super(plugin);
        this.user = user;
        this.player = player;
        this.gadgetType = gadgetType;
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
     * Gets the gadget type for this event.
     *
     * @return the {@link GadgetType} instance
     */
    public GadgetType getGadgetType() {
        return gadgetType;
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
