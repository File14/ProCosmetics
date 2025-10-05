package se.file14.procosmetics.command.commands.set;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class SetTreasureCommand extends SubCommand<CommandSender> {

    public SetTreasureCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.set.treasure", true);
        addFlats("set", "treasure");
        addArgument(String.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "treasure", sender -> plugin.getTreasureChestManager().getTreasureChests().stream().map(TreasureChest::getKey).collect(Collectors.toList()));
        addArgument(Integer.class, "amount");
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
        int amount = parseArgument(args, 4);

        plugin.getUserManager().getAsync(name).thenAccept(user -> {
            if (user == null) {
                audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
                return;
            }
            plugin.getDatabase().setTreasureKeysAsync(user, treasureChest, amount).thenAccept(result -> {
                if (result.firstBoolean()) {
                    audience(sender).sendMessage(translator.translate(
                            "command.set.treasure",
                            Placeholder.unparsed("amount", String.valueOf(amount)),
                            Placeholder.unparsed("treasure_chest", treasureChest.getName(translator)),
                            Placeholder.unparsed("player", user.getName()),
                            Placeholder.unparsed("current", String.valueOf(user.getTreasureChests(treasureChest)))
                    ));
                } else {
                    audience(sender).sendMessage(translator.translate("generic.error.database"));
                }
            });
        });
    }
}