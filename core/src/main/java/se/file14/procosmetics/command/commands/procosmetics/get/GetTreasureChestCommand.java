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
package se.file14.procosmetics.command.commands.procosmetics.get;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class GetTreasureChestCommand extends SubCommand<CommandSender> {

    public GetTreasureChestCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.get.treasurechest", true);
        addFlats("get", "treasurechest");
        addArgument(String.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "treasure_chest", sender -> plugin.getTreasureChestManager().getTreasureChests().stream().map(TreasureChest::getKey).collect(Collectors.toList()));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        String name = parseArgument(args, 2);
        TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(parseArgument(args, 3));

        if (treasureChest == null) {
            audience(sender).sendMessage(translator.translate("treasure.not_found"));
            return;
        }
        plugin.getUserManager().getAsync(name).thenAccept(user -> {
            if (user == null) {
                audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
                return;
            }
            audience(sender).sendMessage(translator.translate(
                    "command.get.treasure_chest",
                    Placeholder.unparsed("player", user.getName()),
                    Placeholder.unparsed("amount", String.valueOf(user.getTreasureChests(treasureChest))),
                    Placeholder.unparsed("treasure_chest", treasureChest.getName(translator))
            ));
        });
    }
}
