package se.file14.procosmetics.command.commands.procosmetics.add;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class AddCosmeticCommand extends SubCommand<CommandSender> {

    public AddCosmeticCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.add.cosmetic", true);
        addFlats("add", "cosmetic");
        addArgument(Player.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "category", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
        addArgument(String.class, "cosmetic");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        Player target = parseArgument(args, 2);

        if (target == null) {
            audience(sender).sendMessage(translator.translate("generic.player_offline"));
            return;
        }
        User user = plugin.getUserManager().getConnected(target);

        if (user == null) {
            audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
            return;
        }
        CosmeticCategory<?, ?, ?> cosmeticCategory = plugin.getCategoryRegistries().getCategory(parseArgument(args, 3));

        if (cosmeticCategory == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.category_not_found"));
            return;
        }
        CosmeticType<?, ?> cosmeticType = cosmeticCategory.getCosmeticRegistry().getType(parseArgument(args, 4));

        if (cosmeticType == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.not_found"));
            return;
        }

        if (target.hasPermission(cosmeticType.getPermission())) {
            audience(sender).sendMessage(translator.translate(
                    "command.cosmetic.add.already_owned",
                    Placeholder.unparsed("player", target.getName())
            ));
            return;
        }
        plugin.getServer().dispatchCommand(
                plugin.getServer().getConsoleSender(),
                plugin.getConfigManager().getMainConfig().getString("settings.permission_add_command")
                        .replace("<player>", target.getName())
                        .replace("<permission>", cosmeticType.getPermission())
        );

        audience(sender).sendMessage(translator.translate(
                "command.add.cosmetic",
                Placeholder.unparsed("cosmetic", cosmeticType.getName(translator)),
                Placeholder.unparsed("player", target.getName())
        ));
    }
}