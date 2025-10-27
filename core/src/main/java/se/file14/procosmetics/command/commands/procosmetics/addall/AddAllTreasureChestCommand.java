package se.file14.procosmetics.command.commands.procosmetics.addall;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class AddAllTreasureChestCommand extends SubCommand<CommandSender> {

    public AddAllTreasureChestCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.addall.treasurechest", true);
        addFlats("addall", "treasurechest");
        addArgument(String.class, "treasure_chest", sender -> plugin.getTreasureChestManager().getTreasureChests().stream().map(TreasureChest::getKey).collect(Collectors.toList()));
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(parseArgument(args, 2));

        if (treasureChest == null) {
            audience(sender).sendMessage(translator.translate("treasure_chest.not_found"));
            return;
        }
        int amount = parseArgument(args, 3);
        int players = 0;

        for (User user : plugin.getUserManager().getAllConnected()) {
            plugin.getDatabase().addTreasureChestsAsync(user, treasureChest, amount);
            players++;
        }

        audience(sender).sendMessage(translator.translate(
                "command.addall.treasure_chest",
                Placeholder.unparsed("amount", String.valueOf(amount)),
                Placeholder.unparsed("treasure_chest", treasureChest.getName(translator)),
                Placeholder.unparsed("player_count", String.valueOf(players))
        ));
    }
}