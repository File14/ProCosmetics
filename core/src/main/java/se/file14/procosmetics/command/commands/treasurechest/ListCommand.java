package se.file14.procosmetics.command.commands.treasurechest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.treasure.TreasureChestPlatformImpl;

public class ListCommand extends SubCommand<CommandSender> {

    public ListCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.treasurechest", true);
        addFlats("treasurechest", "list");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);

        audience(sender).sendMessage(translator.translate(
                "command.treasurechest.list.header",
                Placeholder.unparsed("amount", String.valueOf(plugin.getTreasureChestManager().getPlatforms().size()))
        ));
        TextComponent.Builder builder = Component.text();

        for (TreasureChestPlatformImpl platform : plugin.getTreasureChestManager().getPlatforms()) {
            Location center = platform.getCenter();

            builder.append(Component.newline(),
                    translator.translate(
                            "command.treasurechest.list.entry",
                            Placeholder.unparsed("id", String.valueOf(platform.getId())),
                            Placeholder.unparsed("x", String.valueOf(center.getBlock())),
                            Placeholder.unparsed("y", String.valueOf(center.getBlockY())),
                            Placeholder.unparsed("z", String.valueOf(center.getBlockZ())),
                            Placeholder.unparsed("world", center.getWorld().getName())
                    ));
        }
        audience(sender).sendMessage(builder.build());
    }
}