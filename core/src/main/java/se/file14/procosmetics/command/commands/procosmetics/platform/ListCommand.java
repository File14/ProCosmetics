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
package se.file14.procosmetics.command.commands.procosmetics.platform;

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
        super(plugin, "procosmetics.command.platform.list", true);
        addFlats("platform", "list");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);

        audience(sender).sendMessage(translator.translate(
                "command.platform.list.header",
                Placeholder.unparsed("amount", String.valueOf(plugin.getTreasureChestManager().getPlatforms().size()))
        ));
        TextComponent.Builder builder = Component.text();

        for (TreasureChestPlatformImpl platform : plugin.getTreasureChestManager().getPlatforms()) {
            Location center = platform.getCenter();

            builder.append(Component.newline(),
                    translator.translate(
                            "command.platform.list.entry",
                            Placeholder.unparsed("id", String.valueOf(platform.getId())),
                            Placeholder.unparsed("x", String.valueOf(center.getBlock())),
                            Placeholder.unparsed("y", String.valueOf(center.getBlockY())),
                            Placeholder.unparsed("z", String.valueOf(center.getBlockZ())),
                            Placeholder.unparsed("world", center.getWorld().getName())
                    ));
        }
        audience(sender).sendMessage(builder);
    }
}
