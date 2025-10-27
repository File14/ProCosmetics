package se.file14.procosmetics.command.commands.procosmetics.platform;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class TeleportCommand extends SubCommand<CommandSender> {

    public TeleportCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.platform.teleport", false);
        addFlats("platform", "teleport");
        addArgument(Integer.class, "id", sender -> plugin.getTreasureChestManager().getPlatforms().stream().map(platform -> String.valueOf(platform.getId())).collect(Collectors.toList()));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        int id = parseArgument(args, 2);
        Player player = (Player) sender;
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
            return;
        }
        TreasureChestPlatform platform = plugin.getTreasureChestManager().getPlatform(id);

        if (platform == null) {
            user.sendMessage(user.translate("command.platform.teleport.not_found"));
            return;
        }
        player.teleport(platform.getCenter().clone().add(0.5d, 1.5d, 0.5d));
        user.sendMessage(user.translate(
                "command.platform.teleport.success",
                Placeholder.unparsed("id", String.valueOf(platform.getId()))
        ));
    }
}
