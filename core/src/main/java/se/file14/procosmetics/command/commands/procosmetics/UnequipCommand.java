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
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class UnequipCommand extends SubCommand<CommandSender> {

    public UnequipCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.unequip.other", true);
        addFlat("unequip");
        addArgument(Player.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "category", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
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
        String type = parseArgument(args, 2);

        if (type.equalsIgnoreCase("all")) {
            user.clearAllCosmetics(false, true);
            audience(sender).sendMessage(translator.translate("command.unequipplayer.all"));
            return;
        }
        CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(type);

        if (category == null) {
            audience(sender).sendMessage(translator.translate("category.not_found"));
            return;
        }
        user.removeCosmetic(category, false, true);
        audience(sender).sendMessage(translator.translate(
                "command.unequipplayer.category",
                Placeholder.unparsed("category", category.getKey()),
                Placeholder.unparsed("player", target.getName())
        ));
    }
}
