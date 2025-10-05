package se.file14.procosmetics.command.argument;

// FROM https://github.com/GC-spigot/simple-spigot

public interface ArgumentType<T> {

    T parse(String arg);
}