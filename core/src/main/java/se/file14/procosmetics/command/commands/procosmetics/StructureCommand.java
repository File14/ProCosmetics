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
package se.file14.procosmetics.command.commands.procosmetics;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.util.structure.StructureDataImpl;
import se.file14.procosmetics.util.structure.StructureReader;

import java.util.Map;
import java.util.WeakHashMap;

public class StructureCommand extends SubCommand<CommandSender> {

    private static final double MAX_DISTANCE_SQUARED = 32.0f * 32.0f;

    private final Map<Player, StructureEditor> editors = new WeakHashMap<>();

    public StructureCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.structure", false);
        addFlat("structure");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        Player player = (Player) sender;
        User user = plugin.getUserManager().getConnected((Player) sender);

        if (user == null) {
            audience(sender).sendMessage(translator.translate("generic.error.player_data"));
            return;
        }
        StructureEditor editor = editors.get(player);

        if (editor == null) {
            user.sendMessage(Component.text("Structure creator started! ", NamedTextColor.YELLOW)
                    .append(Component.text("Type this command again when you are standing on the first position!", NamedTextColor.WHITE))
            );
            editors.put(player, new StructureEditor());
            return;
        }
        Location location1 = editor.location1;

        if (location1 == null) {
            editor.location1 = player.getLocation();
            user.sendMessage(Component.text("Position 1 set!", NamedTextColor.GREEN)
                    .append(Component.text(" Type this command again when you are standing on the second position!", NamedTextColor.WHITE))
            );
            return;
        }
        Location location2 = editor.location2;

        if (location2 == null) {
            editor.location2 = player.getLocation();
            user.sendMessage(Component.text("Position 2 set!", NamedTextColor.GREEN)
                    .append(Component.text(" Type this command again when you are standing in front/inside/behind the structure!", NamedTextColor.WHITE))
            );
            return;
        }

        if (!location1.getWorld().equals(location2.getWorld())) {
            user.sendMessage(Component.text("The positions are not in the same world!", NamedTextColor.RED));
            editors.remove(player);
            return;
        }

        if (location1.distanceSquared(location2) > MAX_DISTANCE_SQUARED) {
            user.sendMessage(Component.text("The positions are too far away from each other", NamedTextColor.RED));
            editors.remove(player);
            return;
        }
        StructureDataImpl data = StructureReader.createStructureData(player.getLocation(), location1, location2);
        StructureReader.writeStructure(
                data,
                plugin.getDataFolder().toPath().resolve("structures").resolve(sender.getName() + "-created.json").toFile()
        );
        user.sendMessage(Component.text("Exported the structure! You exited the creator mode!", NamedTextColor.GREEN));
        editors.remove(player);
    }

    private static final class StructureEditor {

        private Location location1;
        private Location location2;
    }
}
