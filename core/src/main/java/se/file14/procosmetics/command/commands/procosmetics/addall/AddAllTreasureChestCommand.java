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
package se.file14.procosmetics.command.commands.procosmetics.addall;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class AddAllTreasureChestCommand extends SubCommand<CommandSender> {

    public AddAllTreasureChestCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.addall.treasurechest", true);
        addFlats("addall", "treasurechest");
        addArgument(String.class, "treasure_chest", sender -> plugin.getTreasureChestManager().getTreasureChests().stream().map(TreasureChest::getKey).collect(Collectors.toList()));
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(parseArgument(args, 2));

        if (treasureChest == null) {
            audience(sender).sendMessage(translator.translate("treasure_chest.not_found"));
            return;
        }
        int amount = parseArgument(args, 3);
        int players = 0;

        for (User user : plugin.getUserManager().getAllConnected()) {
            plugin.getDatabase().addTreasureChestsAsync(user, treasureChest, amount);
            players++;
        }

        audience(sender).sendMessage(translator.translate(
                "command.addall.treasure_chest",
                Placeholder.unparsed("amount", String.valueOf(amount)),
                Placeholder.unparsed("treasure_chest", treasureChest.getName(translator)),
                Placeholder.unparsed("player_count", String.valueOf(players))
        ));
    }
}
