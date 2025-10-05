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