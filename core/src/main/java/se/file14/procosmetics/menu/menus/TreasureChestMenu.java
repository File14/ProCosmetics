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
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.menu.Menu;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.menu.menus.purchase.TreasurePurchaseMenu;
import se.file14.procosmetics.treasure.animation.type.Common;
import se.file14.procosmetics.treasure.animation.type.Legendary;
import se.file14.procosmetics.treasure.animation.type.Rare;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class TreasureChestMenu extends MenuImpl {

    public TreasureChestMenu(ProCosmetics plugin, User user) {
        super(plugin, user, user.translate("menu.treasure_chests.title"),
                plugin.getTreasureChestManager().getTreasureChestsConfig().getInt("menu.rows")
        );
    }

    @Override
    protected void addItems() {
        for (TreasureChest treasureChest : plugin.getTreasureChestManager().getTreasureChests()) {
            if (!treasureChest.isEnabled()) {
                continue;
            }
            int keys = user.getTreasureChests(treasureChest);
            String name = treasureChest.getName(user);
            ItemBuilder itemBuilder = treasureChest.getItemBuilder();
            TagResolver tagResolver = treasureChest.getResolvers(user);
            String path;

            if (!treasureChest.isPurchasable()) {
                path = "purchase_disabled";
            } else if (treasureChest.hasPurchasePermission(player)) {
                path = "purchasable";
            } else {
                path = "purchase_no_permission";
            }

            itemBuilder.setDisplayName(user.translate(
                    "menu.treasure_chests." + treasureChest.getKey() + "." + path + ".name",
                    Placeholder.unparsed("name", name),
                    tagResolver
            ));
            itemBuilder.setLore(user.translateList(
                    "menu.treasure_chests." + treasureChest.getKey() + "." + path + ".desc",
                    Placeholder.unparsed("name", name),
                    tagResolver
            ));

            setItem(itemBuilder.getSlot(), itemBuilder.getItemStack(), event -> {
                        if (event.isShiftClick() && plugin.getTreasureChestManager().getTreasureChestsConfig().getBoolean("loot_categories.enabled")) {
                            Menu menu = new LootCategoriesMenu(plugin, user, treasureChest);
                            menu.setPreviousMenu(this);
                            menu.open();
                            playClickSound();
                            return;
                        }

                        if (keys < 1 || event.isRightClick()) {
                            if (treasureChest.isPurchasable() && treasureChest.hasPurchasePermission(player)) {
                                new TreasurePurchaseMenu(plugin, user, treasureChest).open();
                                playClickSound();
                            } else {
                                playDenySound();
                            }
                        } else {
                            player.closeInventory();
                            TreasureChestPlatform platform = user.getCurrentPlatform();

                            if (platform == null) {
                                player.closeInventory();
                                return;
                            }

                            if (platform.isInUse()) {
                                user.sendMessage(user.translate("treasure_chest.already_in_use"));
                                playDenySound();
                                return;
                            }
                            platform.setUser(user);

                            plugin.getDatabase().removeTreasureChestsAsync(user, treasureChest, 1).thenAcceptAsync(result -> {
                                if (result.leftBoolean()) {
                                    // TODO: Rework in the future
                                    switch (treasureChest.getChestAnimationType()) {
                                        case COMMON: {
                                            new Common(plugin, platform, treasureChest, user);
                                            break;
                                        }
                                        case RARE: {
                                            new Rare(plugin, platform, treasureChest, user);
                                            break;
                                        }
                                        case LEGENDARY: {
                                            new Legendary(plugin, platform, treasureChest, user);
                                            break;
                                        }
                                    }
                                } else {
                                    user.sendMessage(user.translate("generic.error.database"));
                                    platform.setUser(null);
                                    playDenySound();
                                }
                            }, plugin.getSyncExecutor());
                        }
                    }
            );
        }
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getTreasureChestManager().getTreasureChestsConfig();

        if (!config.getBoolean("menu.items.fill_empty_slots.enabled")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.items.fill_empty_slots").getItemStack();
    }
}
