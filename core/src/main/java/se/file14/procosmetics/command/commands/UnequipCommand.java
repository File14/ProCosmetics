package se.file14.procosmetics.command.commands;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class UnequipCommand extends SimpleCommand<CommandSender> {

    public UnequipCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "unequip", "procosmetics.command.unequip", false);
        setSubCommands(new ArgumentSubCommand(plugin));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // NOTE: Don't translate this, when we switch to brigadier command lib it solves itself
        audience(sender).sendMessage(Component.text("/unequip <type>", NamedTextColor.RED));
    }

    private static class ArgumentSubCommand extends SubCommand<CommandSender> {

        public ArgumentSubCommand(ProCosmeticsPlugin plugin) {
            super(plugin, "procosmetics.command.unequip", false);
            addArgument(String.class, "category", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
        }

        @Override
        public void onExecute(CommandSender sender, String[] args) {
            User user = plugin.getUserManager().getConnected((Player) sender);

            if (user == null) {
                audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
                return;
            }
            String type = parseArgument(args, 0);
            CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(type);

            if (category == null) {
                user.sendMessage(user.translate("category.not_found"));
                return;
            }

            if (user.hasCosmetic(category)) {
                user.removeCosmetic(category, false, true);
            } else {
                user.sendMessage(user.translate("cosmetic.not_equipped"));
            }
        }
    }
}
