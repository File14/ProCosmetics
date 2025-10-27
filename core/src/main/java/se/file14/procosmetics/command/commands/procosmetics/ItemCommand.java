package se.file14.procosmetics.command.commands.procosmetics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SubCommand;

public class ItemCommand extends SubCommand<CommandSender> {

    public ItemCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.item", false);
        addFlat("item");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            audience(player).sendMessage(Component.text("You are not holding in any item in your main hand!", NamedTextColor.RED));
            return;
        }
        String componentString = itemStack.getItemMeta().getAsComponentString();

        if (componentString.equals("[]")) {
            componentString = "";
        }
        NamespacedKey namespacedKey = itemStack.getType().getKey();
        String input = namespacedKey.getNamespace() + ":" + namespacedKey.getKey() + componentString;

        audience(player).sendMessage(Component.text().append(
                Component.text("Selected item:", NamedTextColor.GREEN),
                Component.newline(),
                Component.text(input)
                        .clickEvent(ClickEvent.copyToClipboard(input))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to copy!", NamedTextColor.YELLOW)))
        ));
    }
}
