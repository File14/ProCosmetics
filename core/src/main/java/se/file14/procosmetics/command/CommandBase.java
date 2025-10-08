// Portions of this class are adapted from Simple-Spigot
// https://github.com/GC-spigot/simple-spigot
// Copyright (c) 2020 Hyfe (Alex)
// Licensed under the MIT License.

package se.file14.procosmetics.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.argument.ArgumentHandler;
import se.file14.procosmetics.command.argument.ArgumentType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class CommandBase implements CommandExecutor, TabCompleter {

    private final ProCosmeticsPlugin plugin;
    private final Set<SimpleCommand<? extends CommandSender>> commands = Sets.newHashSet();

    public CommandBase(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        registerArgumentTypes();
    }

    public void registerCommand(SimpleCommand<? super CommandSender> command) {
        PluginCommand pluginCommand = plugin.getCommand(command.getCommand());
        if (pluginCommand == null) {
            plugin.getLogger().log(Level.WARNING, "Failed to load the command " + command.getCommand());
            return;
        }
        pluginCommand.setExecutor(this);
        commands.add(command);
    }

    public CommandBase registerArgumentType(Class<?> clazz, ArgumentType<?> argumentType) {
        ArgumentHandler.register(clazz, argumentType);
        return this;
    }

    @Override
    public synchronized boolean onCommand(@Nonnull CommandSender sender, org.bukkit.command.Command command, @Nonnull String label, String[] args) {
        String commandName = command.getName();
        for (SimpleCommand<? extends CommandSender> simpleCommand : commands) {
            if (!simpleCommand.getCommand().equalsIgnoreCase(commandName)) {
                continue;
            }
            if (simpleCommand.getPermission() != null && !simpleCommand.getPermission().isEmpty() && !sender.hasPermission(simpleCommand.getPermission())) {
                simpleCommand.audience(sender).sendMessage(simpleCommand.translator(sender).translate("player.not_permission"));
                return true;
            }
            if (!simpleCommand.isConsole() && sender instanceof ConsoleCommandSender) {
                simpleCommand.audience(sender).sendMessage(Component.text("The console can not execute this command.", NamedTextColor.RED));
                return true;
            }
            if (args.length == 0) {
                simpleCommand.middleMan(sender, args);
                return true;
            }
            SubCommand<? extends CommandSender> subResult = null;

            for (SubCommand<? extends CommandSender> subCommand : simpleCommand.getSubCommands()) {
                if ((args.length > subCommand.getArgumentsSize() && subCommand.isEndless()) || (subCommand.getArgumentsSize() == args.length && subCommand.isMatch(args))) {
                    subResult = subCommand;
                    break;
                }
            }
            if (subResult == null) {
                simpleCommand.middleMan(sender, args);
                return true;
            }
            if (!subResult.doesInheritPermission() && subResult.getPermission() != null && !sender.hasPermission(subResult.getPermission()) && !simpleCommand.getPermission().isEmpty()) {
                subResult.audience(sender).sendMessage(subResult.translator(sender).translate("player.not_permission"));
                return true;
            }
            if (!subResult.isConsole() && sender instanceof ConsoleCommandSender) {
                subResult.audience(sender).sendMessage(Component.text("The console can not execute this command.", NamedTextColor.RED));
                return true;
            }
            subResult.middleMan(sender, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, org.bukkit.command.Command command, @Nonnull String alias, String[] args) {
        List<String> tabCompleteSuggestions = Lists.newArrayList();
        String commandName = command.getName();

        for (SimpleCommand<? extends CommandSender> simpleCommand : commands) {
            if (!simpleCommand.getCommand().equalsIgnoreCase(commandName)) {
                continue;
            }
            if (simpleCommand.getPermission() != null && !simpleCommand.getPermission().isEmpty() && !sender.hasPermission(simpleCommand.getPermission())) {
                continue;
            }
            if (!simpleCommand.isConsole() && sender instanceof ConsoleCommandSender) {
                continue;
            }
            if (args.length == 0) {
                continue;
            }
            Set<SubCommand<? extends CommandSender>> subResults = Sets.newHashSet();

            for (SubCommand<? extends CommandSender> subCommand : simpleCommand.getSubCommands()) {
                if (subCommand.isMatchUntilIndex(args, args.length - 1)) {
                    subResults.add(subCommand);
                }
            }
            if (subResults.isEmpty()) {
                continue;
            }
            for (SubCommand<? extends CommandSender> subResult : subResults) {
                if (!subResult.doesInheritPermission() && subResult.getPermission() != null && !sender.hasPermission(subResult.getPermission()) && !simpleCommand.getPermission().isEmpty()) {
                    continue;
                }
                if (!subResult.isConsole() && sender instanceof ConsoleCommandSender) {
                    continue;
                }
                List<String> list = subResult.tabCompletionSuggestion(sender, args.length - 1);

                list.forEach(entry -> {
                    if (entry.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                        tabCompleteSuggestions.add(entry);
                    }
                });
            }
        }
        return tabCompleteSuggestions;
    }

    public Set<SimpleCommand<? extends CommandSender>> getCommands() {
        return commands;
    }

    private void registerArgumentTypes() {
        registerArgumentType(String.class, string -> string)
                .registerArgumentType(Player.class, Bukkit::getPlayerExact)
                .registerArgumentType(Integer.class, string -> StringUtils.isNumeric(string) ? Integer.parseInt(string) : 0)
                .registerArgumentType(Boolean.class, string -> string.equalsIgnoreCase("true") || (string.equalsIgnoreCase("false") ? false : null));
    }
}