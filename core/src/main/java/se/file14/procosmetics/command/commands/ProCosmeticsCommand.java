/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.argument.Argument;
import se.file14.procosmetics.command.commands.procosmetics.*;
import se.file14.procosmetics.command.commands.procosmetics.EquipCommand;
import se.file14.procosmetics.command.commands.procosmetics.UnequipCommand;
import se.file14.procosmetics.command.commands.procosmetics.add.AddAmmoCommand;
import se.file14.procosmetics.command.commands.procosmetics.add.AddCoinsCommand;
import se.file14.procosmetics.command.commands.procosmetics.add.AddCosmeticCommand;
import se.file14.procosmetics.command.commands.procosmetics.add.AddTreasureChestCommand;
import se.file14.procosmetics.command.commands.procosmetics.addall.AddAllAmmoCommand;
import se.file14.procosmetics.command.commands.procosmetics.addall.AddAllCoinsCommand;
import se.file14.procosmetics.command.commands.procosmetics.addall.AddAllCosmeticCommand;
import se.file14.procosmetics.command.commands.procosmetics.addall.AddAllTreasureChestCommand;
import se.file14.procosmetics.command.commands.procosmetics.get.GetAmmoCommand;
import se.file14.procosmetics.command.commands.procosmetics.get.GetCoinsCommand;
import se.file14.procosmetics.command.commands.procosmetics.get.GetTreasureChestCommand;
import se.file14.procosmetics.command.commands.procosmetics.platform.CreateCommand;
import se.file14.procosmetics.command.commands.procosmetics.platform.DeleteCommand;
import se.file14.procosmetics.command.commands.procosmetics.platform.ListCommand;
import se.file14.procosmetics.command.commands.procosmetics.platform.TeleportCommand;
import se.file14.procosmetics.command.commands.procosmetics.remove.RemoveAmmoCommand;
import se.file14.procosmetics.command.commands.procosmetics.remove.RemoveCoinsCommand;
import se.file14.procosmetics.command.commands.procosmetics.remove.RemoveTreasureChestCommand;
import se.file14.procosmetics.command.commands.procosmetics.set.SetAmmoCommand;
import se.file14.procosmetics.command.commands.procosmetics.set.SetCoinsCommand;
import se.file14.procosmetics.command.commands.procosmetics.set.SetTreasureChestCommand;

public class ProCosmeticsCommand extends SimpleCommand<CommandSender> {

    public ProCosmeticsCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics", "procosmetics.command");
        setSubCommands(
                new AddAmmoCommand(plugin),
                new AddCoinsCommand(plugin),
                new AddCosmeticCommand(plugin),
                new AddTreasureChestCommand(plugin),

                new GetAmmoCommand(plugin),
                new GetCoinsCommand(plugin),
                new GetTreasureChestCommand(plugin),

                new AddAllAmmoCommand(plugin),
                new AddAllCoinsCommand(plugin),
                new AddAllCosmeticCommand(plugin),
                new AddAllTreasureChestCommand(plugin),

                new SetAmmoCommand(plugin),
                new SetCoinsCommand(plugin),
                new SetTreasureChestCommand(plugin),

                new RemoveAmmoCommand(plugin),
                new RemoveCoinsCommand(plugin),
                new RemoveTreasureChestCommand(plugin),

                new EquipCommand(plugin),
                new InfoCommand(plugin),
                new ItemCommand(plugin),
                new MenuCommand(plugin),
                new ReloadCommand(plugin),
                new StructureCommand(plugin),
                new UnequipAllPlayersCommand(plugin),
                new UnequipCommand(plugin),

                // Treasure Chest Platform
                new CreateCommand(plugin),
                new DeleteCommand(plugin),
                new ListCommand(plugin),
                new TeleportCommand(plugin)
        );
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text(plugin.getDescription().getName() + " Commands", NamedTextColor.AQUA));

        getSubCommands().forEach(subCommand -> {
            builder.append(Component.newline());

            for (Argument<?> argument : subCommand.getArguments()) {
                builder.append(Component.text("/pc " + (argument.getType() == null ? argument.getArgument() + " " : "<" + argument.getArgument() + "> "), NamedTextColor.YELLOW));
            }
        });
        audience(sender).sendMessage(builder);
    }
}
