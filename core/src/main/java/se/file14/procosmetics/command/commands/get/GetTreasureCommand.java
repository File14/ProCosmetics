package se.file14.procosmetics.command.commands.get;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class GetTreasureCommand extends SubCommand<CommandSender> {

    public GetTreasureCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.get.treasure", true);
        addFlats("get", "treasure");
        addArgument(String.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "treasure", sender -> plugin.getTreasureChestManager().getTreasureChests().stream().map(TreasureChest::getKey).collect(Collectors.toList()));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        String name = parseArgument(args, 2);
        TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(parseArgument(args, 3));

        if (treasureChest == null) {
            audience(sender).sendMessage(translator.translate("treasure.not_found"));
            return;
        }
        plugin.getUserManager().getAsync(name).thenAccept(user -> {
            if (user == null) {
                audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
                return;
            }
            audience(sender).sendMessage(translator.translate(
                    "command.get.ammo",
                    Placeholder.unparsed("player", user.getName()),
                    Placeholder.unparsed("amount", String.valueOf(user.getTreasureChests(treasureChest))),
                    Placeholder.unparsed("treasure_chest", treasureChest.getName(translator))
            ));
        });
    }
}