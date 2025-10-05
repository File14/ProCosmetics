package se.file14.procosmetics.command.commands.external;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.menu.menus.MainMenu;

public class CosmeticsCommand extends SimpleCommand<CommandSender> {

    public CosmeticsCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "cosmetics", "procosmetics.command.cosmetics", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        User user = plugin.getUserManager().getConnected((Player) sender);

        if (user == null) {
            audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
            return;
        }
        new MainMenu(plugin, user).open();
    }
}
