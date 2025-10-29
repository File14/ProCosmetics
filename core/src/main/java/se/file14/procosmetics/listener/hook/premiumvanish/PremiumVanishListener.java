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
package se.file14.procosmetics.listener.hook.premiumvanish;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;

public class PremiumVanishListener implements Listener {

    private final ProCosmeticsPlugin plugin;

    public PremiumVanishListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVanish(PlayerHideEvent event) {
        Player player = event.getPlayer();

        if (!player.isOnline()) {
            return;
        }
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }
        user.unequipCosmetics(true, false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onUnVanish(PlayerShowEvent event) {
        Player player = event.getPlayer();

        if (!player.isOnline()) {
            return;
        }
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }
        user.equipSavedCosmetics(true);
    }
}
