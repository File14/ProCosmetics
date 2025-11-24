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
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class InfoCommand extends SubCommand<CommandSender> {

    public InfoCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.info", true);
        addFlat("info");
        addArgument(Player.class, "player", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        Player target = parseArgument(args, 1);

        if (target == null) {
            audience(sender).sendMessage(translator.translate("generic.player_offline"));
            return;
        }
        User user = plugin.getUserManager().getConnected(target);

        if (user == null) {
            audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
            return;
        }
        TextComponent.Builder messageBuilder = Component.text();

        // Header
        messageBuilder.append(translator.translate(
                "command.info.header",
                Placeholder.unparsed("player", user.getName())
        ), Component.newline());

        // Database ID
        messageBuilder.append(translator.translate(
                "command.info.id",
                Placeholder.parsed("id", String.valueOf(user.getDatabaseId()))
        ), Component.newline());

        // UUID
        messageBuilder.append(translator.translate(
                "command.info.uuid",
                Placeholder.parsed("uuid", user.getUniqueId().toString())
        ), Component.newline());

        // Coins
        messageBuilder.append(translator.translate(
                "command.info.coins",
                Placeholder.unparsed("amount", String.valueOf(plugin.getEconomyManager().getEconomyProvider().getCoins(user)))
        ), Component.newline());

        // Self Morph View
        String morphStatus = user.hasSelfViewMorph() ? "command.info.status.enable" : "command.info.status.disable";
        messageBuilder.append(translator.translate(
                "command.info.self_view.morph",
                Placeholder.component("status", translator.translate(morphStatus))
        ), Component.newline());

        // Self Status View
        String statusStatus = user.hasSelfViewStatus() ? "command.info.status.enable" : "command.info.status.disable";
        messageBuilder.append(translator.translate(
                "command.info.self_view.status",
                Placeholder.component("status", translator.translate(statusStatus))
        ), Component.newline());

        // Treasure chests
        if (!user.getTreasureChests().isEmpty()) {
            messageBuilder.append(translator.translate("command.info.treasure_chests.header"),
                    Component.newline(),
                    Component.join(JoinConfiguration.newlines(),
                            user.getTreasureChests().entrySet().stream()
                                    .map(entry -> translator.translate(
                                            "command.info.treasure_chests.entry",
                                            Placeholder.unparsed("treasure_chest", entry.getKey().getName(translator)),
                                            Placeholder.unparsed("amount", String.valueOf(entry.getValue()))
                                    ))
                                    .collect(Collectors.toList())
                    ),
                    Component.newline()
            );
        }

        // Equipped Cosmetics
        if (!user.getCosmetics().isEmpty()) {
            messageBuilder.append(translator.translate("command.info.equipped_cosmetics.header"),
                    Component.newline(),
                    Component.join(JoinConfiguration.newlines(), user.getCosmetics().entrySet().stream()
                            .map(entry -> translator.translate(
                                    "command.info.equipped_cosmetics.entry",
                                    Placeholder.unparsed("category", entry.getKey().getKey()),
                                    Placeholder.unparsed("cosmetic", entry.getValue().getType().getName(translator))
                            ))
                            .collect(Collectors.toList())
                    ),
                    Component.newline()
            );
        }

        // Gadget Ammo
        if (!user.getAmmo().isEmpty()) {
            messageBuilder.append(translator.translate("command.info.ammo.header"),
                    Component.newline(),
                    Component.join(JoinConfiguration.newlines(),
                            user.getAmmo().entrySet().stream()
                                    .map(entry -> translator.translate(
                                            "command.info.ammo.entry",
                                            Placeholder.unparsed("gadget", entry.getKey().getName(translator)),
                                            Placeholder.unparsed("amount", String.valueOf(entry.getValue()))
                                    ))
                                    .collect(Collectors.toList())
                    ));
        }
        audience(sender).sendMessage(messageBuilder.build());
    }
}
