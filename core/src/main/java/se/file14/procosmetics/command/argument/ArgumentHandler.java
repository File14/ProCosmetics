package se.file14.procosmetics.command.argument;

import com.google.common.collect.Maps;

import java.util.Map;

// FROM https://github.com/GC-spigot/simple-spigot

public class ArgumentHandler {

    private static final Map<Class<?>, ArgumentType<?>> argumentTypes = Maps.newHashMap();

    public static void register(Class<?> clazz, ArgumentType<?> argumentType) {
        argumentTypes.put(clazz, argumentType);
    }

    @SuppressWarnings("unchecked")
    public static <T> ArgumentType<T> getArgumentType(Class<?> clazz) {
        return (ArgumentType<T>) argumentTypes.get(clazz);
    }
}