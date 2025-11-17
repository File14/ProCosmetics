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
package se.file14.procosmetics.command.commands.procosmetics;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.event.PluginReloadEventImpl;

public class ReloadCommand extends SubCommand<CommandSender> {

    public ReloadCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.reload", true);
        addFlat("reload");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        long before = System.currentTimeMillis();
        audience(sender).sendMessage(Component.text().append(
                Component.text("Reloading ", NamedTextColor.YELLOW),
                Component.text(plugin.getDescription().getName(), NamedTextColor.GOLD),
                Component.text("...", NamedTextColor.YELLOW)
        ));
        plugin.onDisable();
        plugin.onLoad();
        plugin.onEnable();
        plugin.getServer().getPluginManager().callEvent(new PluginReloadEventImpl(plugin));

        long took = System.currentTimeMillis() - before;
        audience(sender).sendMessage(Component.text().append(
                Component.text(plugin.getDescription().getName(), NamedTextColor.GREEN),
                Component.text(" has been reloaded and it took ", NamedTextColor.GREEN),
                Component.text(took + "ms", NamedTextColor.GOLD),
                Component.text("! ", NamedTextColor.GREEN),
                Component.text("If your changes do not take effect, restart the server!", NamedTextColor.YELLOW)
        ));
    }
}
