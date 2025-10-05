package se.file14.procosmetics.command.commands.giveall;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

public class GiveAllCoinsCommand extends SubCommand<CommandSender> {

    public GiveAllCoinsCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.giveall.coins", true);
        addFlats("giveall", "coins");
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        int amount = parseArgument(args, 2);
        int players = 0;

        for (User user : plugin.getUserManager().getAllConnected()) {
            plugin.getEconomyManager().getEconomyProvider().addCoinsAsync(user, amount);
            players++;
        }

        audience(sender).sendMessage(translator.translate(
                "command.giveall.coins",
                Placeholder.unparsed("amount", String.valueOf(amount)),
                Placeholder.unparsed("player_count", String.valueOf(players))
        ));
    }
}