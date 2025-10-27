package se.file14.procosmetics.command.commands.procosmetics.platform;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.treasure.TreasureChestPlatformImpl;

public class DeleteCommand extends SubCommand<CommandSender> {

    public DeleteCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.platform.delete", false);
        addFlats("platform", "delete");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = plugin.getUserManager().getConnected((Player) sender);

        if (user == null) {
            audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
            return;
        }
        Location location = player.getLocation();
        Location treasureChestLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        TreasureChestPlatformImpl platform = plugin.getTreasureChestManager().getPlatform(treasureChestLocation);

        if (platform == null) {
            user.sendMessage(user.translate("command.platform.delete.not_found"));
            return;
        }

        if (platform.isInUse()) {
            user.sendMessage(user.translate("command.platform.delete.in_use"));
            return;
        }
        plugin.getTreasureChestManager().deletePlatform(platform);
        user.sendMessage(user.translate("command.platform.delete.success"));

        location.getWorld().spawnParticle(Particle.EXPLOSION, treasureChestLocation, 0);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 0.0f);
    }
}