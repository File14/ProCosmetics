package se.file14.procosmetics.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.util.version.VersionUtil;

public class UnsupportedCommand extends SimpleCommand<CommandSender> {

    public UnsupportedCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics", "procosmetics.command");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        audience(sender).sendMessage(Component.text(VersionUtil.getUnsupportedMessage(), NamedTextColor.RED));
    }
}