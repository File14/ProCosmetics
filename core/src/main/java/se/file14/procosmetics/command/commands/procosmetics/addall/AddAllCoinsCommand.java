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
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

public class AddAllCoinsCommand extends SubCommand<CommandSender> {

    public AddAllCoinsCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.addall.coins", true);
        addFlats("allall", "coins");
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        int amount = parseArgument(args, 2);
        int players = 0;

        for (User user : plugin.getUserManager().getAllConnected()) {
            plugin.getEconomyManager().getEconomyProvider().addCoinsAsync(user, amount);
            players++;
        }

        audience(sender).sendMessage(translator.translate(
                "command.addall.coins",
                Placeholder.unparsed("amount", String.valueOf(amount)),
                Placeholder.unparsed("player_count", String.valueOf(players))
        ));
    }
}
