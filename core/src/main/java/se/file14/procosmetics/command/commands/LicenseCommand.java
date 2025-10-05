package se.file14.procosmetics.command.commands;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SubCommand;

public class LicenseCommand extends SubCommand<CommandSender> {

    public LicenseCommand(ProCosmeticsPlugin plugin) {
        super(plugin);
        addFlat("license");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        audience(sender).sendMessage(Component.text(plugin.getDescription().getName() + " is licensed under: https://www.spigotmc.org/members/" + plugin.getValue(), NamedTextColor.GREEN));
    }
}