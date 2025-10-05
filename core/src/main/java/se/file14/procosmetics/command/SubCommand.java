// Portions of this class are adapted from Simple-Spigot
// https://github.com/GC-spigot/simple-spigot
// Copyright (c) 2020 Hyfe (Alex)
// Licensed under the MIT License.

package se.file14.procosmetics.command;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.argument.Argument;
import se.file14.procosmetics.command.argument.ArgumentHandler;

import java.util.List;
import java.util.function.Function;

public abstract class SubCommand<T extends CommandSender> extends Command<T> {

    private final boolean endless;
    private List<Argument<?>> arguments = Lists.newArrayList();
    private boolean inheritPermission;

    public SubCommand(ProCosmeticsPlugin plugin, String permission, boolean isConsole) {
        this(plugin, permission, isConsole, false);
    }

    public SubCommand(ProCosmeticsPlugin plugin, String permission, boolean isConsole, boolean endless) {
        super(plugin, permission, isConsole);
        this.endless = endless;
    }

    public SubCommand(ProCosmeticsPlugin plugin) {
        this(plugin, "", true);
    }

    public SubCommand(ProCosmeticsPlugin plugin, String permission) {
        this(plugin, permission, true);
    }

    public SubCommand(ProCosmeticsPlugin plugin, boolean isConsole) {
        this(plugin, "", isConsole);
    }

    protected void inheritPermission() {
        inheritPermission = true;
    }

    public boolean doesInheritPermission() {
        return inheritPermission;
    }

    public boolean isEndless() {
        return endless;
    }

    public void setArguments(List<Argument<?>> arguments) {
        this.arguments = arguments;
    }

    public void addFlat(String flat) {
        arguments.add(new Argument<>(null, flat));
    }

    public void addFlatWithAliases(String flat, String... aliases) {
        arguments.add(new Argument<>(null, flat, aliases));
    }

    public void addFlats(String... flat) {
        for (String flatArgument : flat) {
            addFlat(flatArgument);
        }
    }

    protected <S> void addArgument(Class<S> clazz, String argument, String... aliases) {
        arguments.add(new Argument<S>(ArgumentHandler.getArgumentType(clazz), argument, aliases));
    }

    protected <S> void addArgument(Class<S> clazz, String argument, Function<CommandSender, List<String>> onTabComplete, String... aliases) {
        arguments.add(new Argument<S>(ArgumentHandler.getArgumentType(clazz), argument, onTabComplete, aliases));
    }

    public int getArgumentsSize() {
        return arguments.size();
    }

    public List<Argument<?>> getArguments() {
        return arguments;
    }

    @SuppressWarnings("unchecked")
    public <U> U parseArgument(String[] args, int index) {
        return ((Argument<U>) arguments.get(index)).getType().parse(args[index]);
    }

    public boolean isMatch(String[] args) {
        return isMatchUntilIndex(args, args.length);
    }

    public boolean isMatchUntilIndex(String[] args, int index) {
        for (int i = 0; i < index; i++) {
            if (!isArgumentValid(args, i)) {
                return false;
            }
        }
        return true;
    }

    public List<String> tabCompletionSuggestion(CommandSender commandSender, int index) {
        if (index > arguments.size() - 1) {
            return Lists.newArrayList();
        }
        return arguments.get(index).getOnTabComplete().apply(commandSender);
    }

    private boolean isArgumentValid(String[] arguments, int index) {
        if (this.arguments.size() - 1 < index && endless) {
            return true;
        }
        if (this.arguments.size() - 1 < index) {
            return false;
        }
        Argument<?> argument = this.arguments.get(index);

        if (argument.getType() == null) {
            String matchTo = arguments[index].toLowerCase();

            for (String alias : argument.getAliases()) {
                if (alias.equalsIgnoreCase(matchTo)) {
                    return true;
                }
            }
            return arguments[index].equalsIgnoreCase(argument.getArgument());
        }
        return true;
    }
}