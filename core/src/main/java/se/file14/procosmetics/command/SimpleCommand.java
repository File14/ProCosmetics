// Portions of this class are adapted from Simple-Spigot
// https://github.com/GC-spigot/simple-spigot
// Copyright (c) 2020 Hyfe (Alex)
// Licensed under the MIT License.

package se.file14.procosmetics.command;

import com.google.common.collect.Sets;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;

import java.util.List;
import java.util.Set;

public abstract class SimpleCommand<T extends CommandSender> extends Command<T> {

    private final String command;
    private Set<SubCommand<? extends CommandSender>> subCommands = Sets.newLinkedHashSet();

    public SimpleCommand(ProCosmeticsPlugin plugin, String command, String permission, boolean isConsole) {
        super(plugin, permission, isConsole);
        this.command = command;
    }

    public SimpleCommand(ProCosmeticsPlugin plugin, String command, boolean isConsole) {
        this(plugin, command, "", isConsole);
    }

    public SimpleCommand(ProCosmeticsPlugin plugin, String command, String permission) {
        this(plugin, command, permission, true);
    }

    public SimpleCommand(ProCosmeticsPlugin plugin, String command) {
        this(plugin, command, true);
    }

    public String getCommand() {
        return command;
    }

    public Set<SubCommand<? extends CommandSender>> getSubCommands() {
        return subCommands;
    }

    public void setSubCommands(Set<SubCommand<? extends CommandSender>> subCommands) {
        this.subCommands = subCommands;
    }

    @SafeVarargs
    protected final void setSubCommands(SubCommand<? extends CommandSender>... subCommands) {
        this.subCommands.addAll(List.of(subCommands));
    }
}