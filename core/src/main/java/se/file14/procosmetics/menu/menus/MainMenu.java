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
package se.file14.procosmetics.menu.menus;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.menu.Menu;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.cosmetic.registry.CosmeticCategoryImpl;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class MainMenu extends MenuImpl {

    public MainMenu(ProCosmetics plugin, User user) {
        super(plugin, user, user.translate("menu.main.title"),
                plugin.getConfigManager().getMainConfig().getInt("menu.main.rows")
        );
    }

    @Override
    protected void addItems() {
        Player player = getPlayer();
        Config config = plugin.getConfigManager().getMainConfig();

        if (config.getBoolean("menu.main.items.unequip_all.enable")) {
            ItemBuilder itemBuilder = new ItemBuilderImpl(config, "menu.main.items.unequip_all");
            itemBuilder.setDisplayName(user.translate("menu.main.unequip_all.name"));
            itemBuilder.setLoreComponent(user.translateList("menu.main.unequip_all.desc"));

            setItem(itemBuilder.getSlot(), itemBuilder.getItemStack(), event -> {
                        user.clearAllCosmetics(false, true);
                        player.closeInventory();
                    }
            );
        }

        if (config.getBoolean("menu.main.items.coins.enable")) {
            int coins = plugin.getEconomyManager().getEconomyProvider().getCoins(user);

            ItemBuilder itemBuilder = new ItemBuilderImpl(config, "menu.main.items.coins");
            itemBuilder.setDisplayName(user.translate(
                    "menu.main.coins.name",
                    Placeholder.unparsed("coins", String.valueOf(coins))));
            itemBuilder.setLoreComponent(user.translateList(
                    "menu.main.coins.desc",
                    Placeholder.unparsed("coins", String.valueOf(coins)))
            );

            setItem(itemBuilder.getSlot(), itemBuilder.getItemStack(), event -> {
                user.sendMessage(user.translate("menu.main.coins.click_prompt"));
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.0f);
                player.closeInventory();
            });
        }

        for (CosmeticCategory<?, ?, ?> category : plugin.getCategoryRegistries().getCategories()) {
            if (!category.isEnabled()) {
                continue;
            }
            int amount = category.getCosmeticRegistry().getEnabledTypes().size();

            // TODO: Remove cast in the future
            ItemBuilderImpl menuItem = ((CosmeticCategoryImpl<?, ?, ?>) category).getMenuItem();
            menuItem.setDisplayName(user.translate("menu.main." + category.getKey() + ".name"));
            menuItem.setLoreComponent(user.translateList(
                    "menu.main." + category.getKey() + ".desc",
                    Placeholder.unparsed("current", String.valueOf(category.getUnlockedCosmetics(player))),
                    Placeholder.unparsed("maximum", String.valueOf(amount))));

            setItem(menuItem.getSlot(), menuItem.getItemStack(), event -> {
                        if (event.isRightClick()) {
                            user.removeCosmetic(category, false, true);
                            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.5f, 0.8f);
                        } else {
                            Menu menu = category.createMenu(plugin, user);
                            menu.setPreviousMenu(this);
                            menu.open();
                            playClickSound();
                        }
                    }
            );
        }
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getConfigManager().getMainConfig();

        if (!config.getBoolean("menu.main.items.fill_empty_slots.enable")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.main.items.fill_empty_slots").getItemStack();
    }
}
