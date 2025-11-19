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
import se.file14.procosmetics.api.menu.ClickableItem;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.PaginatedItem;
import se.file14.procosmetics.menu.PaginatedMenu;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

public class LootEntriesMenu extends PaginatedMenu<LootEntriesMenu.EntryPaginatedItem> {

    private final TreasureChest treasureChest;
    private final LootCategory category;
    private final ItemBuilderImpl nextPageItem;
    private final ItemBuilderImpl previousPageItem;
    private ItemBuilderImpl goBackItem;
    private final DecimalFormat decimalFormat;

    public LootEntriesMenu(ProCosmetics plugin, User user, TreasureChest treasureChest, LootCategory category, List<LootEntry> entries, DecimalFormat decimalFormat) {
        super(plugin, user,
                user.translate("menu.treasure_chest.loot_entries.title",
                        Placeholder.component("category", category.getResolvedName(user)),
                        Placeholder.unparsed("name", treasureChest.getName(user)),
                        treasureChest.getResolvers(user)
                ),
                plugin.getTreasureChestManager().getTreasureChestsConfig().getInt("loot_entries.rows"),
                1);
        this.treasureChest = treasureChest;
        this.category = category;
        this.decimalFormat = decimalFormat;

        Config config = plugin.getTreasureChestManager().getTreasureChestsConfig();

        // Next page item
        nextPageItem = new ItemBuilderImpl(config, "loot_entries.items.next_page");
        setNextPageSlot(nextPageItem.getSlot());
        setNextPageItemStack(pageInfo ->
                nextPageItem.setDisplayName(user.translate("menu.treasure_chest.loot_entries.next_page.name"))
                        .setLore(user.translateList(
                                "menu.treasure_chest.loot_entries.next_page.desc",
                                Placeholder.unparsed("page", String.valueOf(page + 1)),
                                Placeholder.unparsed("pages", String.valueOf(pageInfo.getPageCount()))
                        ))
                        .getItemStack()
        );

        // Previous page item
        previousPageItem = new ItemBuilderImpl(config, "loot_entries.items.previous_page");
        setPreviousPageSlot(previousPageItem.getSlot());
        setPreviousPageItemStack(pageInfo ->
                previousPageItem.setDisplayName(user.translate("menu.treasure_chest.loot_entries.previous_page.name"))
                        .setLore(user.translateList(
                                "menu.treasure_chest.loot_entries.previous_page.desc",
                                Placeholder.unparsed("page", String.valueOf(page - 1)),
                                Placeholder.unparsed("pages", String.valueOf(pageInfo.getPageCount()))
                        )).getItemStack()
        );

        // Go back button
        if (config.getBoolean("loot_entries.items.go_back.enabled")) {
            this.goBackItem = new ItemBuilderImpl(config, "loot_entries.items.go_back");
        }
        populateEntries(entries);

        setSorting(Comparator.comparing(item -> item.entry.getName(user), String.CASE_INSENSITIVE_ORDER));
    }

    private void populateEntries(List<LootEntry> entries) {
        for (LootEntry entry : entries) {
            double chance = treasureChest.getLootTable().getEntryChance(entry);
            addPaginatedItem(new EntryPaginatedItem(entry, chance));
        }
    }

    @Override
    protected void addNavigationItems() {
        super.addNavigationItems();

        // Add go back button
        if (goBackItem != null && getPreviousMenu() != null) {
            goBackItem.setDisplayName(user.translate("menu.treasure_chest.loot_entries.go_back.name"));
            goBackItem.setLore(user.translateList(
                    "menu.treasure_chest.loot_entries.go_back.desc",
                    Placeholder.component("menu", getPreviousMenu().getTitle())
            ));

            setItem(goBackItem.getSlot(), goBackItem.getItemStack(), event -> {
                getPreviousMenu().open();
                playClickSound();
            });
        }
    }

    @Override
    public @Nullable ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getTreasureChestManager().getTreasureChestsConfig();
        if (!config.getBoolean("loot_entries.items.fill_empty_slots.enabled")) {
            return null;
        }
        return new ItemBuilderImpl(config, "loot_entries.items.fill_empty_slots").getItemStack();
    }

    public class EntryPaginatedItem extends PaginatedItem {

        private final LootEntry entry;
        private final double chance;

        public EntryPaginatedItem(LootEntry entry, double chance) {
            super(null, null);
            this.entry = entry;
            this.chance = chance;
        }

        @Override
        public ItemStack getItemStack() {
            ItemBuilder itemBuilder = ItemBuilderImpl.of(entry.getItemStack());

            itemBuilder.setDisplayName(user.translate(
                    "menu.treasure_chest.loot_entries.entry.name",
                    Placeholder.component("name", entry.getResolvedName(user)),
                    entry.getResolvers(user)
            ));

            itemBuilder.setLore(user.translateList(
                    "menu.treasure_chest.loot_entries.entry.desc",
                    Placeholder.unparsed("chance", decimalFormat.format(chance)),
                    entry.getResolvers(user)
            ));
            return itemBuilder.getItemStack();
        }

        @Override
        public ClickableItem getClickHandler() {
            return event -> {
            };
        }
    }
}
