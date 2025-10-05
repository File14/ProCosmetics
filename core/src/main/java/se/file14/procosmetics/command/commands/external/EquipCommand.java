package se.file14.procosmetics.command.commands.external;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class EquipCommand extends SimpleCommand<CommandSender> {

    public EquipCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "equip", "procosmetics.command.equip", false);
        setSubCommands(new ArgumentSubCommand(plugin));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // NOTE: Don't translate this, when we switch to brigadier command lib it solves itself
        audience(sender).sendMessage(Component.text("/equip <type> <cosmetic>", NamedTextColor.RED));
    }

    private static class ArgumentSubCommand extends SubCommand<CommandSender> {

        public ArgumentSubCommand(ProCosmeticsPlugin plugin) {
            super(plugin, "procosmetics.command.equip", false);

            addArgument(String.class, "type", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
            addArgument(String.class, "cosmetic");
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
            String cosmeticName = parseArgument(args, 1);
            CosmeticType<?, ?> cosmeticType = category.getCosmeticRegistry().getEnabledType(cosmeticName);

            if (cosmeticType == null) {
                user.sendMessage(user.translate("cosmetic.not_found"));
                return;
            }
            Cosmetic<?, ?> cosmetic = user.getCosmetic(category);

            if (cosmetic != null && cosmetic.getType().equals(cosmeticType)) {
                user.sendMessage(user.translate("cosmetic.already_equipped"));
                return;
            }
            cosmeticType.equip(user, false, true);
        }
    }
}