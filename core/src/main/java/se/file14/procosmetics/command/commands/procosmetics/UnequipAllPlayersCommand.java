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

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

public class UnequipAllPlayersCommand extends SubCommand<CommandSender> {

    public UnequipAllPlayersCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.unequip.all-players", true);
        addFlat("unequipallplayers");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);

        for (User user : plugin.getUserManager().getAllConnected()) {
            user.clearAllCosmetics(false, true);
        }
        audience(sender).sendMessage(translator.translate(
                "command.unequipall",
                Placeholder.unparsed("player_count", String.valueOf(plugin.getUserManager().getAllConnected().size()))
        ));
    }
}
