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
package se.file14.procosmetics.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.menu.ClickableItem;
import se.file14.procosmetics.api.menu.CosmeticMenu;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.menu.menus.MainMenu;
import se.file14.procosmetics.menu.menus.purchase.CosmeticPurchaseMenu;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.Comparator;
import java.util.List;

public class CosmeticMenuImpl<T extends CosmeticType<T, ?>> extends PaginatedMenu implements CosmeticMenu<T> {

    protected final CosmeticCategory<T, ?, ?> category;
    private final ItemBuilderImpl nextPageItem;
    private final ItemBuilderImpl previousPageItem;
    private ItemBuilderImpl goBackItem;
    private ItemBuilderImpl lockedItem;

    public CosmeticMenuImpl(ProCosmetics plugin, User user, CosmeticCategory<T, ?, ?> category) {
        super(plugin, user,
                user.translate("menu." + category.getKey() + ".title"),
                category.getConfig().getInt("menu.rows"),
                1 // Default padding
        );
        this.category = category;

        Config config = category.getConfig();

        // Set up next page item
        nextPageItem = new ItemBuilderImpl(config, "menu.items.next_page");
        setNextPageSlot(nextPageItem.getSlot());
        setNextPageItemStack(pageInfo ->
                nextPageItem.setDisplayName(user.translate("menu." + category.getKey() + ".next_page.name"))
                        .setLoreComponent(user.translateList(
                                "menu." + category.getKey() + ".next_page.desc",
                                Placeholder.unparsed("page", String.valueOf(page + 1)),
                                Placeholder.unparsed("pages", String.valueOf(pageInfo.getPageCount()))
                        ))
                        .getItemStack()
        );

        // Set up previous page item
        previousPageItem = new ItemBuilderImpl(config, "menu.items.previous_page");
        setPreviousPageSlot(previousPageItem.getSlot());
        setPreviousPageItemStack(pageInfo ->
                previousPageItem.setDisplayName(user.translate("menu." + category.getKey() + ".previous_page.name"))
                        .setLoreComponent(user.translateList(
                                "menu." + category.getKey() + ".previous_page.desc",
                                Placeholder.unparsed("page", String.valueOf(page - 1)),
                                Placeholder.unparsed("pages", String.valueOf(pageInfo.getPageCount()))
                        )).getItemStack()
        );

        if (config.getBoolean("menu.items.go_back.enable")) {
            this.goBackItem = new ItemBuilderImpl(category.getConfig(), "menu.items.go_back");
        }

        if (config.getBoolean("menu.items.locked_cosmetic.enable")) {
            this.lockedItem = new ItemBuilderImpl(category.getConfig(), "menu.items.locked_cosmetic");
        }
        populateCosmetics();
    }

    private void populateCosmetics() {
        @SuppressWarnings("unchecked")
        Cosmetic<T, ?> equippedCosmetic = (Cosmetic<T, ?>) user.getCosmetic(category);
        T equippedType = equippedCosmetic != null ? equippedCosmetic.getType() : null;

        for (T cosmeticType : category.getCosmeticRegistry().getEnabledTypes()) {
            ItemStack cosmeticItem = cosmeticType.getItemStack();
            TagResolver tagResolver = cosmeticType.getResolvers(user);
            Component name = user.translate(
                    "menu." + category.getKey() + "." + cosmeticType.getKey() + ".name",
                    Placeholder.unparsed("name", cosmeticType.getName(user)),
                    tagResolver
            );
            List<Component> lore = user.translateList(
                    "menu." + category.getKey() + "." + cosmeticType.getKey() + ".desc",
                    Placeholder.unparsed("name", cosmeticType.getName(user)),
                    tagResolver
            );
            ItemBuilderImpl finalItemBuilder = ItemBuilderImpl.of(cosmeticItem);
            ClickableItem clickHandler;

            // Cosmetic is equipped
            if (cosmeticType == equippedType && equippedCosmetic.isEquipped()) {
                finalItemBuilder.setDisplayName(user.translate(
                                "menu." + category.getKey() + ".equipped.name",
                                Placeholder.component("name", name),
                                tagResolver
                        ))
                        .setLoreComponent(lore)
                        .addLore(user.translateList("menu." + category.getKey() + ".equipped.desc"))
                        .setGlintOverride(true);

                clickHandler = event -> {
                    user.removeCosmetic(category, false, true);
                    player.closeInventory();
                    player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.5f, 0.8f);
                };
            } else if (cosmeticType.hasPermission(player)) {
                // Player owns the cosmetic but it's not equipped
                finalItemBuilder.setDisplayName(user.translate(
                                "menu." + category.getKey() + ".unequipped.name",
                                Placeholder.component("name", name),
                                tagResolver
                        ))
                        .setLoreComponent(lore)
                        .addLore(user.translateList("menu." + category.getKey() + ".unequipped.desc"));

                clickHandler = event -> cosmeticType.equip(user, false, true);
            } else {
                if (lockedItem != null) {
                    finalItemBuilder = ItemBuilderImpl.of(lockedItem.getItemStack());
                }
                String path;

                if (!cosmeticType.isPurchasable()) {
                    path = "purchase_disabled";
                } else if (cosmeticType.hasPurchasePermission(player)) {
                    path = "purchasable";
                } else {
                    path = "purchase_no_permission";
                }

                finalItemBuilder.setDisplayName(user.translate(
                                "menu." + category.getKey() + "." + path + ".name",
                                Placeholder.component("name", name),
                                cosmeticType.getResolvers(user))
                        )
                        .setLoreComponent(lore)
                        .addLore(user.translateList(
                                "menu." + category.getKey() + "." + path + ".desc",
                                cosmeticType.getResolvers(user)
                        ));

                clickHandler = event -> {
                    if (cosmeticType.isPurchasable() && cosmeticType.hasPurchasePermission(player)) {
                        new CosmeticPurchaseMenu(plugin, user, cosmeticType).open();
                        playClickSound();
                    } else {
                        playDenySound();
                    }
                };
            }
            // Add as paginated item
            addPaginatedItem(new PaginatedItem(finalItemBuilder.getItemStack(), clickHandler));
        }
        // Set up sorting if needed
        setSorting(getCosmeticSorting());
    }

    @Override
    protected void addNavigationItems() {
        super.addNavigationItems();

        // Add go back button
        if (goBackItem != null) {
            String path = "menu.items.go_back.on_click.";
            String command = category.getConfig().getString(path + "command")
                    .replace("<player>", player.getName())
                    .replace("<uuid>", player.getUniqueId().toString());

            goBackItem.setDisplayName(user.translate("menu." + category.getKey() + ".go_back.name"));
            goBackItem.setLoreComponent(user.translateList("menu." + category.getKey() + ".go_back.desc"));

            if (command.isEmpty()) {
                setItem(goBackItem.getSlot(), goBackItem.getItemStack(), event -> {
                    // Navigate to main menu or previous menu
                    if (getPreviousMenu() != null) {
                        getPreviousMenu().open();
                    } else {
                        new MainMenu(plugin, user).open();
                    }
                    playClickSound();
                });
            } else {
                boolean playerExecutor = category.getConfig().getBoolean(path + "execute_as_player");
                Server server = plugin.getJavaPlugin().getServer();
                CommandSender commandSender = playerExecutor ? player : server.getConsoleSender();

                setItem(goBackItem.getSlot(), goBackItem.getItemStack(), event -> {
                    server.dispatchCommand(commandSender, command);
                    playClickSound();
                });
            }
        }
    }

    @Override
    protected void addCustomItems() {
        // Override in subclasses to add custom items
    }

    protected Comparator<PaginatedItem> getCosmeticSorting() {
        // Default sorting by display name
        return Comparator.comparing(item -> {
            if (item.getItemStack().getItemMeta() != null && item.getItemStack().getItemMeta().getDisplayName() != null) {
                return item.getItemStack().getItemMeta().getDisplayName();
            }
            return item.getItemStack().getType().name();
        });
    }

    @Override
    public void open() {
        this.page = 1;
        super.open();
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        if (!category.getConfig().getBoolean("menu.items.fill_empty_slots.enable")) {
            return null;
        }
        return new ItemBuilderImpl(category.getConfig(), "menu.items.fill_empty_slots").getItemStack();
    }

    public CosmeticCategory<T, ?, ?> getCategory() {
        return category;
    }
}
