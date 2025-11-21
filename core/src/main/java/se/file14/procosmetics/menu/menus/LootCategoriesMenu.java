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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.menu.ClickableItem;
import se.file14.procosmetics.api.menu.Menu;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.PaginatedItem;
import se.file14.procosmetics.menu.PaginatedMenu;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LootCategoriesMenu extends PaginatedMenu<LootCategoriesMenu.CategoryPaginatedItem> {

    private final TreasureChest treasureChest;
    private final ItemBuilderImpl nextPageItem;
    private final ItemBuilderImpl previousPageItem;
    private ItemBuilderImpl goBackItem;
    private ItemBuilderImpl informationItem;
    private final DecimalFormat decimalFormat;

    public LootCategoriesMenu(ProCosmetics plugin, User user, TreasureChest treasureChest) {
        super(plugin, user,
                user.translate("menu.treasure_chest.loot_categories.title",
                        Placeholder.unparsed("name", treasureChest.getName(user)),
                        treasureChest.getResolvers(user)
                ),
                plugin.getTreasureChestManager().getTreasureChestsConfig().getInt("loot_categories.rows"),
                1);
        this.treasureChest = treasureChest;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(user.translateRaw("generic.decimal_separator").charAt(0));
        decimalFormat = new DecimalFormat("0.00", symbols);

        Config config = plugin.getTreasureChestManager().getTreasureChestsConfig();

        // Next page item
        nextPageItem = new ItemBuilderImpl(config, "loot_categories.items.next_page");
        setNextPageSlot(nextPageItem.getSlot());
        setNextPageItemStack(pageInfo ->
                nextPageItem.setDisplayName(user.translate("menu.treasure_chest.loot_categories.next_page.name"))
                        .setLore(user.translateList(
                                "menu.treasure_chest.loot_categories.next_page.desc",
                                Placeholder.unparsed("page", String.valueOf(page + 1)),
                                Placeholder.unparsed("pages", String.valueOf(pageInfo.getPageCount()))
                        ))
                        .getItemStack()
        );

        // Previous page item
        previousPageItem = new ItemBuilderImpl(config, "loot_categories.items.previous_page");
        setPreviousPageSlot(previousPageItem.getSlot());
        setPreviousPageItemStack(pageInfo ->
                previousPageItem.setDisplayName(user.translate("menu.treasure_chest.loot_categories.previous_page.name"))
                        .setLore(user.translateList(
                                "menu.treasure_chest.loot_categories.previous_page.desc",
                                Placeholder.unparsed("page", String.valueOf(page - 1)),
                                Placeholder.unparsed("pages", String.valueOf(pageInfo.getPageCount()))
                        )).getItemStack()
        );

        // Go back button
        if (config.getBoolean("loot_categories.items.go_back.enabled")) {
            this.goBackItem = new ItemBuilderImpl(config, "loot_categories.items.go_back");
        }

        if (config.getBoolean("loot_categories.items.information.enabled")) {
            this.informationItem = new ItemBuilderImpl(config, "loot_categories.items.information");
        }
        populateCategories();

        setSorting(Comparator.comparing(item -> item.category.getName(user), String.CASE_INSENSITIVE_ORDER));
    }

    private void populateCategories() {
        Map<LootCategory, List<LootEntry>> categoriesMap = treasureChest.getLootTable().getEntriesByCategory();

        for (Map.Entry<LootCategory, List<LootEntry>> entry : categoriesMap.entrySet()) {
            LootCategory category = entry.getKey();
            List<LootEntry> entries = entry.getValue();

            addPaginatedItem(new CategoryPaginatedItem(category, entries));
        }
    }

    @Override
    protected void addNavigationItems() {
        super.addNavigationItems();

        // Add go back button
        if (goBackItem != null && getPreviousMenu() != null) {
            goBackItem.setDisplayName(user.translate("menu.treasure_chest.loot_categories.go_back.name"));
            goBackItem.setLore(user.translateList(
                    "menu.treasure_chest.loot_categories.go_back.desc",
                    Placeholder.component("menu", getPreviousMenu().getTitle())
            ));

            setItem(goBackItem.getSlot(), goBackItem.getItemStack(), event -> {
                getPreviousMenu().open();
                playClickSound();
            });
        }

        if (informationItem != null) {
            informationItem.setDisplayName(user.translate("menu.treasure_chest.loot_categories.information.name"));
            informationItem.setLore(user.translateList(
                    "menu.treasure_chest.loot_categories.information.desc",
                    Placeholder.unparsed("chest_count", String.valueOf(treasureChest.getChestsToOpen()))
            ));

            for (CosmeticRarity rarity : plugin.getCosmeticRarityRegistry().getRarities()) {
                double chance = treasureChest.getLootTable().getRarityChance(rarity);
                informationItem.addLore(user.translateList(
                        "menu.treasure_chest.loot_categories.information.desc.entry",
                        Placeholder.unparsed("chance", decimalFormat.format(chance)),
                        rarity.getResolvers(user)
                ));
            }
            setItem(informationItem.getSlot(), informationItem.getItemStack(), event -> {
            });
        }
    }

    @Override
    public @Nullable ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getTreasureChestManager().getTreasureChestsConfig();
        if (!config.getBoolean("loot_categories.items.fill_empty_slots.enabled")) {
            return null;
        }
        return new ItemBuilderImpl(config, "loot_categories.items.fill_empty_slots").getItemStack();
    }

    public class CategoryPaginatedItem extends PaginatedItem {

        private final LootCategory category;
        private final List<LootEntry> entries;

        public CategoryPaginatedItem(LootCategory category, List<LootEntry> entries) {
            super(null, null);
            this.category = category;
            this.entries = entries;
        }

        @Override
        public ItemStack getItemStack() {
            ItemBuilder itemBuilder = ItemBuilderImpl.of(category.getItemBuilder().getItemStack());

            itemBuilder.setDisplayName(user.translate(
                    "menu.treasure_chest.loot_categories.category.name",
                    Placeholder.component("name", category.getResolvedName(user))
            ));

            itemBuilder.setLore(user.translateList(
                    "menu.treasure_chest.loot_categories.category.desc",
                    Placeholder.unparsed("amount", String.valueOf(entries.size()))
            ));
            return itemBuilder.getItemStack();
        }

        @Override
        public ClickableItem getClickHandler() {
            if (plugin.getTreasureChestManager().getTreasureChestsConfig().getBoolean("loot_entries.enabled")) {
                return event -> {
                    Menu menu = new LootEntriesMenu(plugin, user, treasureChest, category, entries, decimalFormat);
                    menu.setPreviousMenu(LootCategoriesMenu.this);
                    menu.open();
                    playClickSound();
                };
            } else {
                return event -> {
                };
            }
        }
    }
}
