package se.file14.procosmetics.command.commands.procosmetics;


import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class EquipCommand extends SubCommand<CommandSender> {

    public EquipCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.equip.other", true);
        addFlat("equip");
        addArgument(Player.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(CosmeticCategory.class, "category", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
        addArgument(String.class, "cosmetic");
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
        CosmeticCategory<?, ?, ?> category = parseArgument(args, 2);

        if (category == null) {
            audience(sender).sendMessage(translator.translate("category.not_found"));
            return;
        }
        CosmeticType<?, ?> cosmeticType = category.getCosmeticRegistry().getType(parseArgument(args, 3));

        if (cosmeticType == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.not_found"));
            return;
        }
        Cosmetic<?, ?> cosmetic = user.getCosmetic(category);

        if (cosmetic != null && cosmetic.getType().equals(cosmeticType)) {
            audience(sender).sendMessage(translator.translate(
                    "command.equipplayer.already_equipped",
                    Placeholder.unparsed("player", target.getName())
            ));
            return;
        }
        cosmeticType.equip(user, false, true);
        audience(sender).sendMessage(translator.translate(
                "command.equipplayer.success",
                Placeholder.unparsed("player", target.getName())
        ));
    }
}