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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryAction;
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

public class CosmeticMenuImpl<T extends CosmeticType<T, ?>> extends PaginatedMenu<CosmeticMenuImpl<T>.CosmeticPaginatedItem> implements CosmeticMenu<T> {

    protected final CosmeticCategory<T, ?, ?> category;
    private final ItemBuilderImpl nextPageItem;
    private final ItemBuilderImpl previousPageItem;
    private ItemBuilderImpl goBackItem;
    private ItemBuilderImpl lockedItem;
    private ItemBuilderImpl sortingItem;
    private Sorting sorting;

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

        if (config.getBoolean("menu.items.sorting.enable")) {
            this.sortingItem = new ItemBuilderImpl(category.getConfig(), "menu.items.sorting");
        }

        if (config.getBoolean("menu.items.locked_cosmetic.enable")) {
            this.lockedItem = new ItemBuilderImpl(category.getConfig(), "menu.items.locked_cosmetic");
        }

        populateCosmetics();
        setSorting(Sorting.get(0));
    }

    private void populateCosmetics() {
        @SuppressWarnings("unchecked")
        Cosmetic<T, ?> equippedCosmetic = (Cosmetic<T, ?>) user.getCosmetic(category);
        T equippedType = equippedCosmetic != null ? equippedCosmetic.getType() : null;

        for (T cosmeticType : category.getCosmeticRegistry().getEnabledTypes()) {
            boolean isLocked = !cosmeticType.hasPermission(player);
            boolean isEquipped = cosmeticType == equippedType && equippedCosmetic != null && equippedCosmetic.isEquipped();

            addPaginatedItem(new CosmeticPaginatedItem(cosmeticType, isLocked, isEquipped));
        }
    }

    @Override
    protected void addNavigationItems() {
        super.addNavigationItems();

        // Add sorting button
        if (sortingItem != null) {
            setItem(sortingItem.getSlot(), getSortingItem(), event -> {
                if (event.getAction() != InventoryAction.NOTHING) {
                    setSorting(event.isLeftClick() ? sorting.next() : sorting.previous());
                    playClickSound();
                }
            });
        }

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

    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
        super.setSorting(sorting.getComparator());

        if (isValid()) {
            refresh();
        }
    }

    private ItemStack getSortingItem() {
        sortingItem.setDisplayName(user.translate("menu." + category.getKey() + ".sort.name"));
        sortingItem.setLoreComponent(List.of());

        for (Sorting s : Sorting.VALUES) {
            if (this.sorting == s) {
                sortingItem.addLore(Component.text("⏵ ", NamedTextColor.GREEN)
                        .append(user.translate("menu." + category.getKey() + ".sorting." + s.getKey())));
            } else {
                sortingItem.addLore(Component.text("  ", NamedTextColor.GRAY)
                        .append(user.translate("menu." + category.getKey() + ".sorting." + s.getKey())));
            }
        }
        sortingItem.addLore(user.translateList("menu." + category.getKey() + ".sort.desc"));
        return sortingItem.getItemStack();
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

    public class CosmeticPaginatedItem extends PaginatedItem {

        private final T cosmeticType;
        private final boolean locked;
        private final boolean equipped;

        public CosmeticPaginatedItem(T cosmeticType, boolean locked, boolean equipped) {
            super(null, null);
            this.cosmeticType = cosmeticType;
            this.locked = locked;
            this.equipped = equipped;
        }

        @Override
        public ItemStack getItemStack() {
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
            ItemBuilderImpl itemBuilder = ItemBuilderImpl.of(cosmeticItem);

            if (equipped) {
                itemBuilder.setDisplayName(user.translate(
                                "menu." + category.getKey() + ".equipped.name",
                                Placeholder.component("name", name),
                                tagResolver
                        ))
                        .setLoreComponent(lore)
                        .addLore(user.translateList("menu." + category.getKey() + ".equipped.desc"))
                        .setGlintOverride(true);
            } else if (!locked) {
                itemBuilder.setDisplayName(user.translate(
                                "menu." + category.getKey() + ".unequipped.name",
                                Placeholder.component("name", name),
                                tagResolver
                        ))
                        .setLoreComponent(lore)
                        .addLore(user.translateList("menu." + category.getKey() + ".unequipped.desc"));
            } else {
                if (lockedItem != null) {
                    itemBuilder = ItemBuilderImpl.of(lockedItem.getItemStack());
                }
                String path;

                if (!cosmeticType.isPurchasable()) {
                    path = "purchase_disabled";
                } else if (cosmeticType.hasPurchasePermission(player)) {
                    path = "purchasable";
                } else {
                    path = "purchase_no_permission";
                }

                itemBuilder.setDisplayName(user.translate(
                                "menu." + category.getKey() + "." + path + ".name",
                                Placeholder.component("name", name),
                                tagResolver
                        ))
                        .setLoreComponent(lore)
                        .addLore(user.translateList(
                                "menu." + category.getKey() + "." + path + ".desc",
                                tagResolver
                        ));
            }

            return itemBuilder.getItemStack();
        }

        @Override
        public ClickableItem getClickHandler() {
            if (equipped) {
                return event -> {
                    user.removeCosmetic(category, false, true);
                    player.closeInventory();
                    player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.5f, 0.8f);
                };
            } else if (!locked) {
                return event -> cosmeticType.equip(user, false, true);
            } else {
                return event -> {
                    if (cosmeticType.isPurchasable() && cosmeticType.hasPurchasePermission(player)) {
                        new CosmeticPurchaseMenu(plugin, user, cosmeticType).open();
                        playClickSound();
                    } else {
                        playDenySound();
                    }
                };
            }
        }

        public T getCosmeticType() {
            return cosmeticType;
        }

        public boolean isLocked() {
            return locked;
        }

        public String getName() {
            return cosmeticType.getName(user);
        }
    }

    public enum Sorting {
        ALPHABET("alphabetical"),
        UNLOCKED("unlocked"),
        RARITY("rarity"),
        COST("cost");

        public static final List<Sorting> VALUES = List.of(values());

        private final String key;

        Sorting(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public <T extends CosmeticType<T, ?>> Comparator<CosmeticMenuImpl<T>.CosmeticPaginatedItem> getComparator() {
            return switch (this) {
                case ALPHABET -> Comparator.comparing(
                        (CosmeticMenuImpl<T>.CosmeticPaginatedItem item) -> item.getName(),
                        String.CASE_INSENSITIVE_ORDER
                );
                case UNLOCKED -> Comparator
                        .comparing((CosmeticMenuImpl<T>.CosmeticPaginatedItem item) -> item.isLocked())
                        .thenComparing(item -> item.getName(), String.CASE_INSENSITIVE_ORDER);
                case RARITY -> Comparator
                        .comparing((CosmeticMenuImpl<T>.CosmeticPaginatedItem item) -> item.getCosmeticType().getRarity())
                        .reversed()
                        .thenComparing(item -> item.getName(), String.CASE_INSENSITIVE_ORDER);
                case COST -> Comparator
                        .<CosmeticMenuImpl<T>.CosmeticPaginatedItem>comparingInt(item -> item.getCosmeticType().getCost())
                        .reversed()
                        .thenComparing(item -> item.getName(), String.CASE_INSENSITIVE_ORDER);
            };
        }

        public Sorting next() {
            return VALUES.get((ordinal() + 1) % VALUES.size());
        }

        public Sorting previous() {
            return VALUES.get((VALUES.size() + ordinal() - 1) % VALUES.size());
        }

        public static Sorting get(int index) {
            return VALUES.get(index % VALUES.size());
        }
    }
}
