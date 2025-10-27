package se.file14.procosmetics.command.commands.procosmetics;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

public class UnequipAllPlayersCommand extends SubCommand<CommandSender> {

    public UnequipAllPlayersCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.unequip.all-players", true);
        addFlat("unequipallplayers");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);

        for (User user : plugin.getUserManager().getAllConnected()) {
            user.clearAllCosmetics(false, true);
        }
        audience(sender).sendMessage(translator.translate(
                "command.unequipall",
                Placeholder.unparsed("player_count", String.valueOf(plugin.getUserManager().getAllConnected().size()))
        ));
    }
}
