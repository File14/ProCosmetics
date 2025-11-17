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
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.event.PlayerPurchaseTreasureChestEventImpl;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.menu.menus.TreasureChestMenu;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.logging.Level;

public class TreasurePurchaseMenu extends MenuImpl {

    private static final int COOLDOWN = 20;

    private final TreasureChest treasureChest;
    private final Config config;
    private int amount = 1;

    public TreasurePurchaseMenu(ProCosmetics plugin, User user, TreasureChest treasureChest) {
        super(plugin, user,
                user.translate("menu.purchase.treasure_chest.title"),
                plugin.getConfigManager().getMainConfig().getInt("menu.purchase.treasure_chest.rows")
        );
        this.treasureChest = treasureChest;
        this.config = plugin.getConfigManager().getMainConfig();
    }

    @Override
    protected void addItems() {
        // Treasure item
        ItemBuilder treasureChestItem = new ItemBuilderImpl(treasureChest.getItemStack());
        String name = treasureChest.getName(user);
        TagResolver tagResolver = treasureChest.getResolvers(user);

        treasureChestItem.setDisplayName(user.translate(
                "menu.purchase.treasure_chest.item.name",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        treasureChestItem.setLore(user.translateList(
                "menu.purchase.treasure_chest.item.desc",
                Placeholder.unparsed("name", name),
                Placeholder.unparsed("amount", String.valueOf(amount)),
                tagResolver
        ));
        treasureChestItem.setAmount(amount);

        setItem(13, treasureChestItem.getItemStack(), event -> {
            if (event.isLeftClick()) {
                if (amount < 64) {
                    amount++;
                    playClickSound();
                    addItems();
                } else {
                    playDenySound();
                }
            } else {
                if (amount > 1) {
                    amount--;
                    playClickSound();
                    addItems();
                } else {
                    playDenySound();
                }
            }
        });
        int cost = treasureChest.getCost() * amount;

        ItemBuilder acceptPurchase = new ItemBuilderImpl(config, "menu.purchase.treasure_chest.items.accept");
        acceptPurchase.setDisplayName(user.translate(
                "menu.purchase.treasure_chest.accept.name",
                Placeholder.unparsed("name", name),
                Placeholder.unparsed("cost", String.valueOf(cost)),
                Placeholder.unparsed("amount", String.valueOf(amount))
        ));
        acceptPurchase.setLore(user.translateList(
                "menu.purchase.treasure_chest.accept.desc",
                Placeholder.unparsed("name", name),
                Placeholder.unparsed("cost", String.valueOf(cost)),
                Placeholder.unparsed("amount", String.valueOf(amount))
        ));
        player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

        // Accept purchase button
        setItem(acceptPurchase.getSlot(), acceptPurchase.getItemStack(), event -> {
            if (player.hasCooldown(acceptPurchase.getItemStack())) {
                return;
            }
            close();
            player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

            EconomyProvider economy = plugin.getEconomyManager().getEconomyProvider();

            if (!economy.hasCoins(user, cost)) {
                economy.sendInsufficientCoinsMessage(user, cost);
                playDenySound();
                return;
            }
            economy.removeCoinsAsync(user, cost).thenAcceptAsync(result -> {
                if (result.booleanValue()) {
                    plugin.getDatabase().addTreasureChestsAsync(user, treasureChest, amount).thenAcceptAsync(result2 -> {
                        if (result2.leftBoolean()) {
                            playSuccessSound();
                            plugin.getJavaPlugin().getServer().getPluginManager().callEvent(new PlayerPurchaseTreasureChestEventImpl(plugin, user, player, treasureChest, amount));
                            plugin.getJavaPlugin().getLogger().log(Level.INFO, "[TREASURE CHEST] " + user + " bought " + amount + " " + treasureChest.getKey() + " for " + cost + ".");
                            new TreasureChestMenu(plugin, user).open();
                        } else {
                            // Failed, refund the coins
                            economy.addCoinsAsync(user, cost);
                            user.sendMessage(user.translate("generic.error.database"));
                            playDenySound();
                        }
                    }, plugin.getSyncExecutor());
                } else {
                    economy.sendInsufficientCoinsMessage(user, cost);
                    playDenySound();
                }
            }, plugin.getSyncExecutor());
        });

        // Deny purchase button
        ItemBuilder denyPurchase = new ItemBuilderImpl(config, "menu.purchase.treasure_chest.items.deny");
        denyPurchase.setDisplayName(user.translate("menu.purchase.treasure_chest.deny.name"));
        denyPurchase.setLore(user.translateList(
                "menu.purchase.treasure_chest.deny.desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));

        setItem(denyPurchase.getSlot(), denyPurchase.getItemStack(), event -> {
            playClickSound();
            close();
        });
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getConfigManager().getMainConfig();

        if (!config.getBoolean("menu.purchase.treasure_chest.items.fill_empty_slots.enabled")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.purchase.treasure_chest.items.fill_empty_slots").getItemStack();
    }
}
