package se.file14.procosmetics.command.commands.take;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class TakeCoinsCommand extends SubCommand<CommandSender> {

    public TakeCoinsCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.take-coins", true);
        addFlats("take", "coins");
        addArgument(String.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        String name = parseArgument(args, 2);
        int amount = parseArgument(args, 3);

        plugin.getUserManager().getAsync(name).thenAccept(user -> {
            if (user == null) {
                audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
                return;
            }
            plugin.getEconomyManager().getEconomyProvider().removeCoinsAsync(user, amount).thenAccept(result -> {
                if (result.booleanValue()) {
                    audience(sender).sendMessage(translator.translate(
                            "command.take.coins",
                            Placeholder.unparsed("amount", String.valueOf(amount)),
                            Placeholder.unparsed("player", user.getName()),
                            Placeholder.unparsed("coins", String.valueOf(user.getCoins()))
                    ));
                } else {
                    audience(sender).sendMessage(translator.translate("generic.error.database"));
                }
            });
        });
    }
}