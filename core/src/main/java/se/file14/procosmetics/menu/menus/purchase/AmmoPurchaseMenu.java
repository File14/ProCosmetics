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
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.cosmetic.gadget.GadgetImpl;
import se.file14.procosmetics.event.PlayerPurchaseGadgetAmmoEventImpl;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.logging.Level;

public class AmmoPurchaseMenu extends MenuImpl {

    private static final int COOLDOWN = 20;

    private final GadgetType gadgetType;
    private final Config config;

    public AmmoPurchaseMenu(ProCosmetics plugin, User user, GadgetType gadgetType) {
        super(plugin, user,
                user.translate("menu.purchase.gadget_ammo.title"),
                plugin.getConfigManager().getMainConfig().getInt("menu.purchase.gadget_ammo.rows")
        );
        this.gadgetType = gadgetType;
        this.config = plugin.getConfigManager().getMainConfig();
    }

    @Override
    protected void addItems() {
        // Gadget item to display
        CosmeticCategory<?, ?, ?> category = gadgetType.getCategory();
        ItemBuilder cosmeticItem = new ItemBuilderImpl(gadgetType.getItemStack());
        String name = gadgetType.getName(user);
        TagResolver tagResolver = gadgetType.getResolvers(user);
        cosmeticItem.setDisplayName(user.translate(
                "menu." + category.getKey() + "." + gadgetType.getKey() + ".name",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        cosmeticItem.setLore(user.translateList(
                "menu." + category.getKey() + "." + gadgetType.getKey() + ".desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));

        setItem(13, cosmeticItem.getItemStack(), event -> {
        });

        ItemBuilder acceptPurchase = new ItemBuilderImpl(config, "menu.purchase.gadget_ammo.items.accept");
        acceptPurchase.setDisplayName(user.translate("menu.purchase.gadget_ammo.accept.name"));
        acceptPurchase.setLore(user.translateList(
                "menu.purchase.gadget_ammo.accept.desc",
                tagResolver
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
            int cost = gadgetType.getAmmoCost();

            if (!economy.hasCoins(user, cost)) {
                economy.sendInsufficientCoinsMessage(user, cost);
                playDenySound();
                return;
            }
            economy.removeCoinsAsync(user, cost).thenAcceptAsync(result -> {
                if (result.booleanValue()) {
                    plugin.getDatabase().addGadgetAmmoAsync(user, gadgetType, gadgetType.getPurchaseAmount()).thenAcceptAsync(result2 -> {
                        if (result2.leftBoolean()) {
                            plugin.getJavaPlugin().getServer().getPluginManager().callEvent(new PlayerPurchaseGadgetAmmoEventImpl(plugin, user, player, gadgetType));
                            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                            GadgetImpl gadget = (GadgetImpl) user.getCosmetic(plugin.getCategoryRegistries().gadgets());

                            // Update the item to show new ammo
                            if (gadget != null && gadget.isEquipped()) {
                                gadget.setGadgetItemInInventory();
                            }
                            plugin.getJavaPlugin().getLogger().log(Level.INFO, "[AMMO] " + user + " bought " + gadgetType.getPurchaseAmount() + " " + gadgetType.getKey() + " ammo for " + cost + ".");
                        } else {
                            // Failed, refund the coins
                            economy.addCoinsAsync(user, cost);
                            user.sendMessage(user.translate("generic.error.database"));
                        }
                    }, plugin.getSyncExecutor());
                } else {
                    economy.sendInsufficientCoinsMessage(user, cost);
                    playDenySound();
                }
            }, plugin.getSyncExecutor());
        });

        // Deny purchase button
        ItemBuilderImpl denyPurchase = new ItemBuilderImpl(config, "menu.purchase.gadget_ammo.items.deny");
        denyPurchase.setDisplayName(user.translate("menu.purchase.gadget_ammo.deny.name"));
        denyPurchase.setLore(user.translateList(
                "menu.purchase.gadget_ammo.deny.desc",
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

        if (!config.getBoolean("menu.purchase.gadget_ammo.items.fill_empty_slots.enable")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.purchase.gadget_ammo.items.fill_empty_slots").getItemStack();
    }
}
