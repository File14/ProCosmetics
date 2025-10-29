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
package se.file14.procosmetics.command.commands;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class UnequipCommand extends SimpleCommand<CommandSender> {

    public UnequipCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "unequip", "procosmetics.command.unequip", false);
        setSubCommands(new ArgumentSubCommand(plugin));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // NOTE: Don't translate this, when we switch to brigadier command lib it solves itself
        audience(sender).sendMessage(Component.text("/unequip <type>", NamedTextColor.RED));
    }

    private static class ArgumentSubCommand extends SubCommand<CommandSender> {

        public ArgumentSubCommand(ProCosmeticsPlugin plugin) {
            super(plugin, "procosmetics.command.unequip", false);
            addArgument(String.class, "category", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
        }

        @Override
        public void onExecute(CommandSender sender, String[] args) {
            User user = plugin.getUserManager().getConnected((Player) sender);

            if (user == null) {
                audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
                return;
            }
            String type = parseArgument(args, 0);
            CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(type);

            if (category == null) {
                user.sendMessage(user.translate("category.not_found"));
                return;
            }

            if (user.hasCosmetic(category)) {
                user.removeCosmetic(category, false, true);
            } else {
                user.sendMessage(user.translate("cosmetic.not_equipped"));
            }
        }
    }
}
