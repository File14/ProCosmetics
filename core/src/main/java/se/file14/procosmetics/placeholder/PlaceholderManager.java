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
package se.file14.procosmetics.placeholder;

import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.placeholder.incoming.IN_PlaceholderAPI;
import se.file14.procosmetics.placeholder.outgoing.OUT_PlaceHolderAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlaceholderManager {

    private final ProCosmeticsPlugin plugin;
    private final List<AbstractPlaceholder> placeholders = new ArrayList<>();

    public PlaceholderManager(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        hookPlugins();
    }

    private void hookPlugins() {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            plugin.getLogger().log(Level.INFO, "Hooking into PlaceholderAPI...");
            new OUT_PlaceHolderAPI(plugin).register();
            register(new IN_PlaceholderAPI());
        }
    }

    public void register(AbstractPlaceholder placeholder) {
        placeholders.add(placeholder);
    }

    public List<String> setPlaceholders(Player player, List<String> text) {
        for (AbstractPlaceholder placeholder : placeholders) {
            text = placeholder.setPlaceholders(player, text);
        }
        return text;
    }

    public String setPlaceholders(Player player, String text) {
        for (AbstractPlaceholder placeholder : placeholders) {
            text = placeholder.setPlaceholders(player, text);
        }
        text = text.replace("<coins>", String.valueOf(plugin.getEconomyManager().getEconomyProvider().getCoins(plugin.getUserManager().getConnected(player))))
                .replace("<ping>", String.valueOf(plugin.getNMSManager().getNMSUtil().getPing(player)));

        return text;
    }
}
