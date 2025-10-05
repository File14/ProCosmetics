package se.file14.procosmetics.command.commands;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SubCommand;

public class ReloadCommand extends SubCommand<CommandSender> {

    public ReloadCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.reload", true);
        addFlat("reload");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        long before = System.currentTimeMillis();
        audience(sender).sendMessage(Component.text("Reloading ", NamedTextColor.YELLOW)
                .append(Component.text(plugin.getDescription().getName(), NamedTextColor.GOLD))
                .append(Component.text("...", NamedTextColor.YELLOW)));

        plugin.onDisable();
        plugin.onLoad();
        plugin.onEnable();

        long took = System.currentTimeMillis() - before;
        audience(sender).sendMessage(Component.text().append(Component.text(plugin.getDescription().getName(), NamedTextColor.GREEN),
                Component.text(" has been reloaded and it took ", NamedTextColor.GREEN),
                Component.text(took + "ms", NamedTextColor.GOLD),
                Component.text("! ", NamedTextColor.GREEN),
                Component.text("If your changes do not take effect, restart the server!", NamedTextColor.YELLOW))
        );
    }
}
