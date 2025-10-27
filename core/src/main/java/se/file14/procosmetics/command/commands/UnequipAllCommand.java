package se.file14.procosmetics.command.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;

public class UnequipAllCommand extends SimpleCommand<CommandSender> {

    public UnequipAllCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "unequipall", "procosmetics.command.unequipall", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        User user = plugin.getUserManager().getConnected((Player) sender);

        if (user == null) {
            audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
            return;
        }
        user.clearAllCosmetics(false, true);
    }
}
