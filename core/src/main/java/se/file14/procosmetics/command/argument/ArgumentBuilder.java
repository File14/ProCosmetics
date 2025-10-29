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

import java.util.List;

// FROM https://github.com/GC-spigot/simple-spigot

public class ArgumentBuilder {

    private final List<Argument<?>> arguments = Lists.newArrayList();

    public List<Argument<?>> getArguments() {
        return arguments;
    }

    public ArgumentBuilder addFlat(String flat) {
        arguments.add(new Argument<>(null, flat));
        return this;
    }

    public ArgumentBuilder addFlatWithAliases(String flat, String... aliases) {
        arguments.add(new Argument<>(null, flat, aliases));
        return this;
    }

    public ArgumentBuilder addFlats(String... flats) {
        for (String flatArgument : flats) {
            addFlat(flatArgument);
        }
        return this;
    }

    public <T> ArgumentBuilder addArgument(Class<T> clazz, String argument) {
        arguments.add(new Argument<T>(ArgumentHandler.getArgumentType(clazz), argument));
        return this;
    }
}
