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
package se.file14.procosmetics.command.argument;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

// FROM https://github.com/GC-spigot/simple-spigot

public class Argument<T> {

    private final ArgumentType<T> type;
    private final String argument;
    private final Set<String> aliases;
    private final Function<CommandSender, List<String>> onTabComplete;

    public Argument(ArgumentType<T> type, String argument, Function<CommandSender, List<String>> onTabComplete, String... aliases) {
        this.type = type;
        this.argument = argument;
        this.aliases = Sets.newHashSet(aliases);
        this.onTabComplete = onTabComplete;
    }

    public Argument(ArgumentType<T> type, String argument, String... aliases) {
        this.type = type;
        this.argument = argument;
        this.aliases = Sets.newHashSet(aliases);
        onTabComplete = sender -> Lists.newArrayList(argument);
    }

    public ArgumentType<T> getType() {
        return type;
    }

    public String getArgument() {
        return argument;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public Function<CommandSender, List<String>> getOnTabComplete() {
        return onTabComplete;
    }
}
