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
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.SubCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ToggleSelfViewCommand extends SimpleCommand<CommandSender> {

    public ToggleSelfViewCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "toggleselfview", "procosmetics.command.toggleselfview", false);
        setSubCommands(new ArgumentSubCommand(plugin));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // NOTE: Don't translate this, when we switch to brigadier command lib it solves itself
        audience(sender).sendMessage(Component.text("/toggleselfview <type>", NamedTextColor.RED));
    }

    private static class ArgumentSubCommand extends SubCommand<CommandSender> {

        private final Map<String, Consumer<User>> TYPES = new HashMap<>();

        public ArgumentSubCommand(ProCosmeticsPlugin plugin) {
            super(plugin, "procosmetics.command.toggleselfview", false);

            if (plugin.getCategoryRegistries().morphs().isEnabled()) {
                TYPES.put("morph", user -> user.setSelfViewMorph(!user.hasSelfViewMorph(), true));
            }
            if (plugin.getCategoryRegistries().statuses().isEnabled()) {
                TYPES.put("status", user -> user.setSelfViewStatus(!user.hasSelfViewStatus(), true));
            }
            addArgument(String.class, "category", sender -> new ArrayList<>(TYPES.keySet()));
        }

        @Override
        public void onExecute(CommandSender sender, String[] args) {
            User user = plugin.getUserManager().getConnected((Player) sender);

            if (user == null) {
                audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
                return;
            }
            String inputType = parseArgument(args, 0).toString().toLowerCase();
            Optional<Map.Entry<String, Consumer<User>>> found = TYPES.entrySet().stream().filter(type -> type.getKey().equals(inputType)).findAny();

            if (found.isPresent()) {
                found.get().getValue().accept(user);
                user.sendMessage(user.translate("command.toggleselfview.success"));
            } else {
                user.sendMessage(user.translate("command.toggleselfview.not_equipped"));
            }
        }
    }
}
