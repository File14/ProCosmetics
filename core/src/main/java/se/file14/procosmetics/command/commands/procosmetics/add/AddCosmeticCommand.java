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
package se.file14.procosmetics.command.commands.procosmetics.add;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.menu.menus.purchase.CosmeticPurchaseMenu;

import java.util.stream.Collectors;

public class AddCosmeticCommand extends SubCommand<CommandSender> {

    public AddCosmeticCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.add.cosmetic", true);
        addFlats("add", "cosmetic");
        addArgument(Player.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "category", sender -> plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList()));
        addArgument(String.class, "cosmetic");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        Player target = parseArgument(args, 2);

        if (target == null) {
            audience(sender).sendMessage(translator.translate("generic.player_offline"));
            return;
        }
        User user = plugin.getUserManager().getConnected(target);

        if (user == null) {
            audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
            return;
        }
        CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(parseArgument(args, 3));

        if (category == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.category_not_found"));
            return;
        }
        CosmeticType<?, ?> cosmeticType = category.getCosmeticRegistry().getType(parseArgument(args, 4));

        if (cosmeticType == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.not_found"));
            return;
        }

        if (target.hasPermission(cosmeticType.getPermission())) {
            audience(sender).sendMessage(translator.translate(
                    "command.cosmetic.add.already_owned",
                    Placeholder.unparsed("player", target.getName())
            ));
            return;
        }
        CosmeticPurchaseMenu.grantCosmeticPermission(plugin, target, cosmeticType);

        audience(sender).sendMessage(translator.translate(
                "command.add.cosmetic",
                Placeholder.unparsed("cosmetic", cosmeticType.getName(translator)),
                Placeholder.unparsed("player", target.getName())
        ));
    }
}
