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
