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
package se.file14.procosmetics.treasure;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.animation.AnimationType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.treasure.loot.*;
import se.file14.procosmetics.util.EnumUtil;
import se.file14.procosmetics.util.item.ItemBuilderImpl;
import se.file14.procosmetics.util.structure.StructureDataImpl;
import se.file14.procosmetics.util.structure.StructureReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class TreasureChestImpl implements TreasureChest {

    private static final Random RANDOM = new Random();

    private final String key;
    private final boolean enabled;
    private final boolean purchasable;
    private final String purchasePermission;
    private final int cost;
    private final int chestsToOpen;
    private final AnimationType chestAnimationType;
    private final List<StructureData> structures = new ArrayList<>();
    private final boolean openingBroadcast;
    private final ItemBuilder itemBuilder;

    private int totalWeight;
    private final List<LootTableImpl<?>> lootEntries = new ArrayList<>();

    public TreasureChestImpl(ProCosmeticsPlugin plugin, String key) {
        this.key = key;

        Config config = plugin.getConfigManager().getConfig("treasure_chests");

        String path = "treasure_chests." + key;
        enabled = config.getBoolean(path + ".enable");
        purchasable = config.getBoolean(path + ".purchasable");
        purchasePermission = "procosmetics.purchase.treasure_chest." + key;
        cost = config.getInt(path + ".cost");
        chestsToOpen = config.getInt(path + ".chests_to_open");
        chestAnimationType = EnumUtil.getType(AnimationType.class, config.getString(path + ".chest_animation"));

        for (String layout : config.getStringList(path + ".animation_layouts")) {
            StructureDataImpl structureData = StructureReader.loadStructure(layout);

            if (structureData == null) {
                plugin.getLogger().log(Level.WARNING, "Failed to load animation layout " + layout + ". Skipping");
                continue;
            }
            structures.add(structureData);
        }
        openingBroadcast = config.getBoolean(path + ".opening_broadcast");
        itemBuilder = new ItemBuilderImpl(config, path);

        // GADGET AMMO
        List<GadgetType> ammo = new ArrayList<>();

        for (GadgetType ammoType : plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getEnabledTypes()) {
            if (ammoType.isFindable()) {
                ammo.add(ammoType);
            }
        }
        String ammoPath = path + ".loot.ammo.";
        int weight = config.getInt(ammoPath + "weight");
        if (weight > 0) {
            totalWeight += weight;
            lootEntries.add(new AmmoLootImpl("ammo",
                    weight,
                    config.getInt(ammoPath + "minimum_amount"),
                    config.getInt(ammoPath + "maximum_amount"),
                    ammo)
            );
        }

        // MONEY
        String moneyPath = path + ".loot.money.";
        weight = config.getInt(moneyPath + "weight");
        if (weight > 0) {
            totalWeight += weight;
            lootEntries.add(new MoneyLootImpl("money",
                    weight,
                    config.getInt(moneyPath + "minimum_amount"),
                    config.getInt(moneyPath + "maximum_amount"))
            );
        }

        // COSMETICS
        for (CosmeticCategory<?, ?, ?> cosmeticCategory : plugin.getCategoryRegistries().getCategories()) {
            String cosmeticPath = path + ".loot." + cosmeticCategory.getKey() + ".";
            weight = config.getInt(cosmeticPath + "weight");

            if (weight > 0) {
                List<CosmeticType<?, ?>> cosmeticTypes = new ArrayList<>();

                for (CosmeticType<?, ?> cosmeticType : cosmeticCategory.getCosmeticRegistry().getEnabledTypes()) {
                    if (cosmeticType.isFindable()) {
                        cosmeticTypes.add(cosmeticType);
                    }
                }

                if (!cosmeticTypes.isEmpty()) {
                    totalWeight += weight;
                    lootEntries.add(new CosmeticLootImpl(cosmeticCategory.getKey(), weight, cosmeticTypes));
                }
            }
        }
        // CUSTOM LOOT
        String customLootPath = path + ".loot.custom";
        if (config.getConfigurationSection(customLootPath) != null) {
            for (String customKey : config.getConfigurationSection(customLootPath).getKeys(false)) {
                String path1 = customLootPath + "." + customKey + ".";
                CosmeticRarity rarity = plugin.getCosmeticRarityRegistry().getSafely(config.getString(path1 + "rarity"));
                weight = config.getInt(path1 + "weight");

                if (weight > 0 && config.getBoolean(path1 + "enable")) {
                    totalWeight += weight;
                    ItemBuilderImpl customItemBuilder = new ItemBuilderImpl(config, path1);

                    lootEntries.add(new CustomLootImpl(
                            customKey,
                            weight,
                            customItemBuilder.getItemStack(),
                            rarity,
                            config.getStringList(path1 + "commands")
                    ));
                }
            }
        }
        List<String> chances = new ArrayList<>();

        for (LootTableImpl<?> lootTable : lootEntries) {
            if (lootTable.getWeight() == 0) {
                continue;
            }
            double percentage = (double) lootTable.getWeight() / totalWeight * 100;
            //chances.add(ChatColor.GRAY + String.format("%s: %.2f%%", lootTable.getMenuCategory(), percentage));
        }
    }

    @Override
    @Nullable
    public LootTableImpl<?> getRandomLootTable() {
        int randomValue = RANDOM.nextInt(totalWeight);
        int currentWeight = 0;

        for (LootTableImpl<?> item : lootEntries) {
            currentWeight += item.getWeight();

            if (randomValue < currentWeight) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean hasPurchasePermission(Player player) {
        return player.hasPermission(purchasePermission);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName(Translator translator) {
        return translator.translateRaw("treasure_chest." + key);
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.resolver(
                Placeholder.unparsed("current", String.valueOf(user.getTreasureChests(this))),
                Placeholder.unparsed("cost", String.valueOf(cost))
        );
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isPurchasable() {
        return purchasable;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getChestsToOpen() {
        return chestsToOpen;
    }

    @Override
    public AnimationType getChestAnimationType() {
        return chestAnimationType;
    }

    @Override
    public List<StructureData> getStructures() {
        return structures;
    }

    @Override
    public boolean isOpeningBroadcast() {
        return openingBroadcast;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public ItemStack getItemStack() {
        return itemBuilder.getItemStack();
    }
}
