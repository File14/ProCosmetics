package se.file14.procosmetics.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.Map;
import java.util.stream.Collectors;

public class InfoCommand extends SubCommand<CommandSender> {

    public InfoCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.info", true);
        addFlat("info");
        addArgument(Player.class, "player", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
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
        TextComponent.Builder messageBuilder = Component.text();

        // Header
        messageBuilder.append(translator.translate(
                "command.info.header",
                Placeholder.unparsed("player", user.getName())
        ), Component.newline());

        // Database ID
        messageBuilder.append(translator.translate(
                "command.info.id",
                Placeholder.parsed("id", String.valueOf(user.getDatabaseId()))
        ), Component.newline());

        // UUID
        messageBuilder.append(translator.translate(
                "command.info.uuid",
                Placeholder.parsed("uuid", user.getUniqueId().toString())
        ), Component.newline());

        // Coins
        messageBuilder.append(translator.translate(
                "command.info.coins",
                Placeholder.unparsed("amount", String.valueOf(plugin.getEconomyManager().getEconomyProvider().getCoins(user)))
        ), Component.newline());

        // Self Morph View
        String morphStatus = user.hasSelfViewMorph() ? "command.info.status.enable" : "command.info.status.disable";
        messageBuilder.append(translator.translate(
                "command.info.self_view.morph",
                Placeholder.component("status", translator.translate(morphStatus))
        ), Component.newline());

        // Self Status View
        String statusStatus = user.hasSelfViewStatus() ? "command.info.status.enable" : "command.info.status.disable";
        messageBuilder.append(translator.translate(
                "command.info.self_view.status",
                Placeholder.component("status", translator.translate(statusStatus))
        ), Component.newline());

        // Treasures
        if (!user.getTreasureChests().isEmpty()) {
            messageBuilder.append(translator.translate("command.info.treasure_chests.header"), Component.newline());

            user.getTreasureChests().forEach((treasure, amount) -> {
                messageBuilder.append(translator.translate(
                        "command.info.treasure_chests.entry",
                        Placeholder.unparsed("treasure_chest", treasure.getName(translator)),
                        Placeholder.unparsed("amount", String.valueOf(amount))
                ), Component.newline());
            });
        }

        // Equipped Cosmetics
        if (!user.getCosmetics().isEmpty()) {
            messageBuilder.append(translator.translate("command.info.equipped_cosmetics.header"), Component.newline());

            user.getCosmetics().forEach((cosmeticCategory, cosmetic) -> {
                messageBuilder.append(translator.translate(
                        "command.info.equipped_cosmetics.entry",
                        Placeholder.unparsed("category", cosmeticCategory.getKey()),
                        Placeholder.unparsed("cosmetic", cosmetic.getType().getName(translator))
                ), Component.newline());
            });
        }

        // Gadget Ammo
        if (!user.getAmmo().isEmpty()) {
            messageBuilder.append(translator.translate("command.info.ammo.header"), Component.newline());

            for (Map.Entry<GadgetType, Integer> entry : user.getAmmo().entrySet()) {
                messageBuilder.append(translator.translate(
                        "command.info.ammo.entry",
                        Placeholder.unparsed("gadget", entry.getKey().getKey()),
                        Placeholder.unparsed("amount", String.valueOf(entry.getValue()))
                ), Component.newline());
            }
        }
        audience(sender).sendMessage(messageBuilder.build());
    }
}
