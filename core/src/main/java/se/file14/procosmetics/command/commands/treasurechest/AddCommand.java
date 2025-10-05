package se.file14.procosmetics.command.commands.treasurechest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.NamedStructureData;
import se.file14.procosmetics.util.structure.StructureDataImpl;
import se.file14.procosmetics.util.structure.StructureReader;

public class AddCommand extends SubCommand<CommandSender> {

    public AddCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.treasurechest", false);
        addFlats("treasurechest", "add");
        addArgument(String.class, "layout");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            audience(sender).sendMessage(translator(sender).translate("generic.error.player_data"));
            return;
        }
        Location location = player.getLocation();
        location.setY(location.getBlockY());
        LocationUtil.center(location);

        if (location.getBlock().getType() != Material.CHEST) {
            user.sendMessage(user.translate("command.treasurechest.add.not_on_chest"));
            return;
        }

        if (plugin.getTreasureChestManager().getPlatform(location) != null) {
            user.sendMessage(user.translate("command.treasurechest.add.already_exists"));
            return;
        }

        if (MathUtil.getIn3DRadius(location, 5).stream().anyMatch(MetadataUtil::isCustomBlock)) {
            user.sendMessage(user.translate("command.treasurechest.add.too_close"));
            return;
        }
        String layout = parseArgument(args, 2);
        StructureDataImpl structureData = StructureReader.loadStructure(layout);

        if (structureData == null) {
            user.sendMessage(user.translate("command.treasurechest.add.layout_not_found"));
            return;
        }
        NamedStructureData namedStructureData = new NamedStructureData(layout, structureData);
        plugin.getTreasureChestManager().createPlatform(location, namedStructureData);

        user.sendMessage(user.translate("command.treasurechest.add.success"));
        player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
}