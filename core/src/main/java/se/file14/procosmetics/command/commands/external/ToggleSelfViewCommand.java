package se.file14.procosmetics.command.commands.external;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.SubCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ToggleSelfViewCommand extends SimpleCommand<CommandSender> {

    public ToggleSelfViewCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "toggleselfview", "procosmetics.command.toggleselfview", false);
        setSubCommands(new ArgumentSubCommand(plugin));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // NOTE: Don't translate this, when we switch to brigadier command lib it solves itself
        audience(sender).sendMessage(Component.text("/toggleselfview <type>", NamedTextColor.RED));
    }

    private static class ArgumentSubCommand extends SubCommand<CommandSender> {

        private final Map<String, Consumer<User>> TYPES = new HashMap<>();

        public ArgumentSubCommand(ProCosmeticsPlugin plugin) {
            super(plugin, "procosmetics.command.toggle-self-view", false);
            TYPES.put("morph", user -> user.setSelfViewMorph(!user.hasSelfViewMorph(), true));
            TYPES.put("status", user -> user.setSelfViewStatus(!user.hasSelfViewStatus(), true));

            addArgument(String.class, "type", sender -> new ArrayList<>(TYPES.keySet()));
        }

        @Override
        public void onExecute(CommandSender sender, String[] args) {
            User user = plugin.getUserManager().getConnected((Player) sender);

            if (user == null) {
                audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
                return;
            }
            String inputType = parseArgument(args, 0).toString().toLowerCase();
            Optional<Map.Entry<String, Consumer<User>>> found = TYPES.entrySet().stream().filter(type -> type.getKey().equals(inputType)).findAny();

            if (found.isPresent()) {
                found.get().getValue().accept(user);
                user.sendMessage(user.translate("command.toggleselfview.success"));
            } else {
                user.sendMessage(user.translate("command.toggleselfview.not_equipped"));
            }
        }
    }
}