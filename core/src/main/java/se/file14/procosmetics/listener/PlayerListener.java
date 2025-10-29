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
package se.file14.procosmetics.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MetadataUtil;

public class PlayerListener implements Listener {

    private final ProCosmeticsPlugin plugin;

    public PlayerListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onShearEntity(PlayerShearEntityEvent event) {
        if (MetadataUtil.isCustomEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (MetadataUtil.isCustomEntity(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GameMode fromGameMode = player.getGameMode();

        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            User user = plugin.getUserManager().getConnected(player);

            if (user != null) {
                user.unequipCosmetics(true, false);
            }
        } else if (fromGameMode == GameMode.SPECTATOR) {
            User user = plugin.getUserManager().getConnected(player);

            if (user != null) {
                user.equipSavedCosmetics(true);
            }
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.NOT_SAFE) {
            Player player = event.getPlayer();
            boolean foundNaturalMonster = false;

            for (Entity entity : player.getWorld().getNearbyEntities(event.getBed().getLocation(), 8.0d, 5.0d, 8.0d)) {
                if (entity instanceof Monster && !MetadataUtil.isCustomEntity(entity)) {
                    foundNaturalMonster = true;
                    break;
                }
            }

            if (!foundNaturalMonster) {
                event.setCancelled(false);
            }
        }
    }
}
