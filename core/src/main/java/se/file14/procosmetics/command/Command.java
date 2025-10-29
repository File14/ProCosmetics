/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2020 File14 and contributors
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
package se.file14.procosmetics.command;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;

public abstract class Command<T extends CommandSender> {

    protected final ProCosmeticsPlugin plugin;
    private final String permission;
    private final boolean isConsole;

    public Command(ProCosmeticsPlugin plugin, String permission, boolean isConsole) {
        this.plugin = plugin;
        this.permission = permission;
        this.isConsole = isConsole;
    }

    public abstract void onExecute(T sender, String[] args);

    @SuppressWarnings("unchecked")
    public void middleMan(CommandSender sender, String[] args) {
        onExecute((T) sender, args);
    }

    public Audience audience(CommandSender sender) {
        return plugin.adventure().sender(sender);
    }

    public Translator translator(CommandSender source) {
        if (source instanceof Player player) {
            User user = plugin.getUserManager().getConnected(player);

            if (user != null) {
                return user;
            }
        }
        return plugin.getLanguageManager();
    }

    public String getPermission() {
        return permission;
    }

    public boolean isConsole() {
        return isConsole;
    }
}
