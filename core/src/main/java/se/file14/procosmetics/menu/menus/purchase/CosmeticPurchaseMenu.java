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
package se.file14.procosmetics.menu.menus.purchase;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.event.PlayerPurchaseCosmeticEvent;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.logging.Level;

public class CosmeticPurchaseMenu extends MenuImpl {

    private static final int COOLDOWN = 20;

    private final CosmeticType<?, ?> cosmeticType;
    private final Config config;

    public CosmeticPurchaseMenu(ProCosmetics plugin, User user, CosmeticType<?, ?> cosmeticType) {
        super(plugin, user,
                user.translate("menu.purchase.cosmetic.title"),
                plugin.getConfigManager().getMainConfig().getInt("menu.purchase.cosmetic.rows")
        );
        this.cosmeticType = cosmeticType;
        this.config = plugin.getConfigManager().getMainConfig();
    }

    @Override
    protected void addItems() {
        // Add the cosmetic item to display
        CosmeticCategory<?, ?, ?> category = cosmeticType.getCategory();
        ItemBuilder cosmeticItem = new ItemBuilderImpl(cosmeticType.getItemStack());
        String name = cosmeticType.getName(user);
        TagResolver tagResolver = TagResolver.resolver(
                cosmeticType.getResolvers(user),
                Placeholder.unparsed("name", name)
        );
        cosmeticItem.setDisplayName(user.translate(
                "menu." + category.getKey() + "." + cosmeticType.getKey() + ".name",
                tagResolver
        ));
        cosmeticItem.setLore(user.translateList(
                "menu." + category.getKey() + "." + cosmeticType.getKey() + ".desc",
                tagResolver
        ));

        setItem(13, cosmeticItem.getItemStack(), event -> {
        });

        ItemBuilderImpl acceptPurchase = new ItemBuilderImpl(config, "menu.purchase.cosmetic.items.accept");
        acceptPurchase.setDisplayName(user.translate(
                "menu.purchase.cosmetic.accept.name",
                tagResolver)
        );
        acceptPurchase.setLore(user.translateList(
                "menu.purchase.cosmetic.accept.desc",
                tagResolver
        ));
        Server server = plugin.getJavaPlugin().getServer();
        player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

        // Accept purchase button
        setItem(acceptPurchase.getSlot(), acceptPurchase.getItemStack(), event -> {
            if (player.hasCooldown(acceptPurchase.getItemStack())) {
                return;
            }
            close();
            player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

            EconomyProvider economy = plugin.getEconomyManager().getEconomyProvider();
            int cost = cosmeticType.getCost();

            if (!economy.hasCoins(user, cost)) {
                economy.sendInsufficientCoinsMessage(user, cost);
                playDenySound();
                return;
            }

            economy.removeCoinsAsync(user, cost).thenAcceptAsync(result -> {
                if (result.booleanValue()) {
                    grantCosmeticPermission(plugin, player, cosmeticType);
                    server.getPluginManager().callEvent(new PlayerPurchaseCosmeticEvent(plugin, user, player, cosmeticType));
                    // TODO: The permission might not yet have been added to the player causing it to not equip
                    cosmeticType.equip(user, false, true);
                    plugin.getJavaPlugin().getLogger().log(Level.INFO, "[COSMETIC] " + user + " bought " + cosmeticType.getKey() + " for " + cost + ".");
                    playSuccessSound();
                } else {
                    economy.sendInsufficientCoinsMessage(user, cost);
                    playDenySound();
                }
            }, plugin.getSyncExecutor());
        });

        // Deny purchase button
        ItemBuilderImpl denyPurchase = new ItemBuilderImpl(config, "menu.purchase.cosmetic.items.deny");
        denyPurchase.setDisplayName(user.translate(
                "menu.purchase.cosmetic.deny.name",
                tagResolver
        ));
        denyPurchase.setLore(user.translateList(
                "menu.purchase.cosmetic.deny.desc",
                tagResolver
        ));

        setItem(denyPurchase.getSlot(), denyPurchase.getItemStack(), event -> {
            playClickSound();
            close();
        });
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        if (!config.getBoolean("menu.purchase.cosmetic.items.fill_empty_slots.enabled")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.purchase.cosmetic.items.fill_empty_slots").getItemStack();
    }

    public static void grantCosmeticPermission(ProCosmetics plugin, Player player, CosmeticType<?, ?> cosmeticType) {
        Server server = plugin.getJavaPlugin().getServer();

        server.dispatchCommand(server.getConsoleSender(),
                plugin.getConfigManager().getMainConfig().getString("settings.permission_add_command")
                        .replace("<player>", player.getName()) // Keep this to be consistent with translation keys
                        .replace("<player_name>", player.getName())
                        .replace("<player_uuid>", player.getUniqueId().toString())
                        .replace("<cosmetic_key>", cosmeticType.getKey())
                        .replace("<cosmetic_permission>", cosmeticType.getPermission())
        );
    }
}
