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
package se.file14.procosmetics.placeholder.outgoing;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

public class OUT_PlaceHolderAPI extends PlaceholderExpansion {

    private final ProCosmeticsPlugin plugin;

    public OUT_PlaceHolderAPI(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
        if (identifier.equals("coins")) {
            User user = plugin.getUserManager().getConnected(offlinePlayer.getUniqueId());

            if (user == null) {
                return "0";
            }
            return String.valueOf(plugin.getEconomyManager().getEconomyProvider().getCoins(user));
        } else if (identifier.contains("equipped_")) {
            for (CosmeticCategory<?, ?, ?> category : plugin.getCategoryRegistries().getCategories()) {
                if (identifier.equals("equipped_" + category.getKey())) {
                    User user = plugin.getUserManager().getConnected(offlinePlayer.getUniqueId());

                    if (user == null) {
                        return "None";
                    }
                    Cosmetic<?, ?> cosmetic = user.getCosmetic(category);

                    if (cosmetic == null) {
                        return "None";
                    }
                    return cosmetic.getType().getName(plugin.getLanguageManager());
                }
            }
        } else if (identifier.contains("unlocked_")) {
            for (CosmeticCategory<?, ?, ?> category : plugin.getCategoryRegistries().getCategories()) {
                if (identifier.equals("unlocked_" + category.getKey())) {
                    Player player = offlinePlayer.getPlayer();

                    if (player == null) {
                        return "0/0";
                    }
                    return category.getUnlockedCosmetics(player) + "/" + category.getCosmeticRegistry().getEnabledTypes().size();
                }
            }
        } else if (identifier.contains("treasures_")) {
            for (TreasureChest treasureChest : plugin.getTreasureChestManager().getTreasureChests()) {
                if (identifier.equals("treasures_" + treasureChest.getKey())) {
                    User user = plugin.getUserManager().getConnected(offlinePlayer.getUniqueId());

                    if (user == null) {
                        return "0";
                    }
                    return String.valueOf(user.getTreasureChests(treasureChest));
                }
            }
        }
        return identifier;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Nonnull
    @Override
    public String getAuthor() {
        return "File14";
    }

    @Nonnull
    @Override
    public String getIdentifier() {
        return "procosmetics";
    }

    @Nonnull
    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
