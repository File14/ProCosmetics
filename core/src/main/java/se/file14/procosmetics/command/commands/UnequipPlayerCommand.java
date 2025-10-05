package se.file14.procosmetics.command.commands;


import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class UnequipPlayerCommand extends SubCommand<CommandSender> {

    public UnequipPlayerCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.unequip.player", true);
        addFlat("unequip");
        addArgument(Player.class, "player", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "type", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        Player target = parseArgument(args, 1);

        if (target == null) {
            audience(sender).sendMessage(translator.translate("generic.player_offline"));
            return;
        }
        User user = plugin.getUserManager().getConnected(target);

        if (user == null) {
            audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
            return;
        }
        String type = parseArgument(args, 2);

        if (type.equalsIgnoreCase("all")) {
            user.clearAllCosmetics(false, true);
            audience(sender).sendMessage(translator.translate("command.unequipplayer.all"));
            return;
        }
        CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(type);

        if (category == null) {
            audience(sender).sendMessage(translator.translate("category.not_found"));
            return;
        }
        user.removeCosmetic(category, false, true);
        audience(sender).sendMessage(translator.translate(
                "command.unequipplayer.category",
                Placeholder.unparsed("category", category.getKey()),
                Placeholder.unparsed("player", target.getName())
        ));
    }
}
