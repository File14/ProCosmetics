package se.file14.procosmetics.command.commands.giveall;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class GiveAllCosmeticCommand extends SubCommand<CommandSender> {

    public GiveAllCosmeticCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.giveall.cosmetic", true);
        addFlats("giveall", "cosmetic");
        addArgument(String.class, "type", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
        addArgument(String.class, "cosmetic");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(parseArgument(args, 2));

        if (category == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.category_not_found"));
            return;
        }
        CosmeticType<?, ?> cosmeticType = category.getCosmeticRegistry().getType(parseArgument(args, 3));

        if (cosmeticType == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.not_found"));
            return;
        }
        int players = 0;
        String command = plugin.getConfigManager().getMainConfig().getString("settings.permission_add_command");

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission(cosmeticType.getPermission())) {
                audience(sender).sendMessage(translator.translate(
                        "command.give.cosmetic.already_owned",
                        Placeholder.unparsed("player", player.getName())
                ));
                players++;
            }
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    command.replace("<player>", player.getName())
                            .replace("<permission>", cosmeticType.getPermission())
            );
        }

        audience(sender).sendMessage(translator.translate(
                "command.giveall.cosmetic",
                Placeholder.unparsed("cosmetic", cosmeticType.getName(translator)),
                Placeholder.unparsed("player_count", String.valueOf(players))
        ));
    }
}