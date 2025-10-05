package se.file14.procosmetics.command.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.argument.Argument;
import se.file14.procosmetics.command.commands.get.GetAmmoCommand;
import se.file14.procosmetics.command.commands.get.GetCoinsCommand;
import se.file14.procosmetics.command.commands.get.GetTreasureCommand;
import se.file14.procosmetics.command.commands.give.GiveAmmoCommand;
import se.file14.procosmetics.command.commands.give.GiveCoinsCommand;
import se.file14.procosmetics.command.commands.give.GiveCosmeticCommand;
import se.file14.procosmetics.command.commands.give.GiveTreasureCommand;
import se.file14.procosmetics.command.commands.giveall.GiveAllAmmoCommand;
import se.file14.procosmetics.command.commands.giveall.GiveAllCoinsCommand;
import se.file14.procosmetics.command.commands.giveall.GiveAllCosmeticCommand;
import se.file14.procosmetics.command.commands.giveall.GiveAllTreasureCommand;
import se.file14.procosmetics.command.commands.set.SetAmmoCommand;
import se.file14.procosmetics.command.commands.set.SetCoinsCommand;
import se.file14.procosmetics.command.commands.set.SetTreasureCommand;
import se.file14.procosmetics.command.commands.take.TakeAmmoCommand;
import se.file14.procosmetics.command.commands.take.TakeCoinsCommand;
import se.file14.procosmetics.command.commands.take.TakeTreasureCommand;
import se.file14.procosmetics.command.commands.treasurechest.AddCommand;
import se.file14.procosmetics.command.commands.treasurechest.DeleteCommand;
import se.file14.procosmetics.command.commands.treasurechest.ListCommand;
import se.file14.procosmetics.command.commands.treasurechest.TeleportCommand;

public class ProCosmeticsCommand extends SimpleCommand<CommandSender> {

    public ProCosmeticsCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics", "procosmetics.command");
        setSubCommands(
                new GetAmmoCommand(plugin),
                new GetCoinsCommand(plugin),
                new GetTreasureCommand(plugin),

                new GiveAmmoCommand(plugin),
                new GiveCoinsCommand(plugin),
                new GiveCosmeticCommand(plugin),
                new GiveTreasureCommand(plugin),

                new GiveAllAmmoCommand(plugin),
                new GiveAllCoinsCommand(plugin),
                new GiveAllCosmeticCommand(plugin),
                new GiveAllTreasureCommand(plugin),

                new SetAmmoCommand(plugin),
                new SetCoinsCommand(plugin),
                new SetTreasureCommand(plugin),

                new TakeAmmoCommand(plugin),
                new TakeCoinsCommand(plugin),
                new TakeTreasureCommand(plugin),

                // TREASURE CHEST
                new AddCommand(plugin),
                new DeleteCommand(plugin),
                new ListCommand(plugin),
                new TeleportCommand(plugin),

                // COMMON
                new EquipPlayerCommand(plugin),
                new InfoCommand(plugin),
                new ItemCommand(plugin),
                new MenuCommand(plugin),
                new ReloadCommand(plugin),
                new StructureCommand(plugin),
                new UnequipAllPlayersCommand(plugin),
                new UnequipPlayerCommand(plugin)
        );
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        StringBuilder stringBuilder = new StringBuilder(ChatColor.AQUA + plugin.getDescription().getName() + " Commands");

        getSubCommands().forEach(subCommand -> {
            stringBuilder.append("\n");
            stringBuilder.append(ChatColor.YELLOW);
            stringBuilder.append("/pc ");

            for (Argument<?> argument : subCommand.getArguments()) {
                stringBuilder.append(argument.getType() == null ? argument.getArgument() + " " : "<" + argument.getArgument() + "> ");
            }
        });
        sender.sendMessage(stringBuilder.toString());
    }
}