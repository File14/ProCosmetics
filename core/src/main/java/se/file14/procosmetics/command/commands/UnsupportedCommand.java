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
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.util.version.VersionUtil;

public class UnsupportedCommand extends SimpleCommand<CommandSender> {

    public UnsupportedCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics", "procosmetics.command");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        audience(sender).sendMessage(Component.text(VersionUtil.getUnsupportedMessage(), NamedTextColor.RED));
    }
}
