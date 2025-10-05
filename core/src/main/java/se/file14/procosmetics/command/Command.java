// Portions of this class are adapted from Simple-Spigot
// https://github.com/GC-spigot/simple-spigot
// Copyright (c) 2020 Hyfe (Alex)
// Licensed under the MIT License.

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