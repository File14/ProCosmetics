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