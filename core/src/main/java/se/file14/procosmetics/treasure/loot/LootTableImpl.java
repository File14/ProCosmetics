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
package se.file14.procosmetics.treasure.loot;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.treasure.loot.LootTable;

import java.util.*;
import java.util.stream.Collectors;

public class LootTableImpl implements LootTable {

    protected static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    protected static final Random RANDOM = new Random();

    private final List<LootEntry> entries;
    private final Map<CosmeticRarity, Integer> rarityWeights;
    private final int totalWeight;
    private final Map<LootCategory, List<LootEntry>> entriesByCategory;
    private final Map<CosmeticRarity, List<LootEntry>> entriesByRarity;

    public LootTableImpl(List<LootEntry> entries, Map<CosmeticRarity, Integer> rarityWeights) {
        this.entries = new ArrayList<>(entries);
        this.rarityWeights = new HashMap<>(rarityWeights);
        this.totalWeight = rarityWeights.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        this.entriesByCategory = entries.stream()
                .collect(Collectors.groupingBy(
                        LootEntry::getCategory,
                        HashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                List::copyOf
                        )
                ));

        this.entriesByRarity = entries.stream()
                .collect(Collectors.groupingBy(
                        LootEntry::getRarity,
                        HashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                List::copyOf
                        )
                ));
    }

    @Override
    @Nullable
    public LootEntry rollLoot() {
        if (entries.isEmpty() || totalWeight == 0) {
            return null;
        }
        // Roll a random rarity based on weights
        CosmeticRarity selectedRarity = null;
        int randomValue = RANDOM.nextInt(totalWeight);
        int currentWeight = 0;

        for (Map.Entry<CosmeticRarity, Integer> entry : rarityWeights.entrySet()) {
            currentWeight += entry.getValue();

            if (randomValue < currentWeight) {
                selectedRarity = entry.getKey();
                break;
            }
        }
        if (selectedRarity == null) {
            return null;
        }
        List<LootEntry> rarityEntries = entriesByRarity.get(selectedRarity);

        if (rarityEntries == null || rarityEntries.isEmpty()) {
            return null;
        }
        return rarityEntries.get(RANDOM.nextInt(rarityEntries.size()));
    }

    @Override
    public double getEntryChance(LootEntry entry) {
        List<LootEntry> rarityEntries = entriesByRarity.get(entry.getRarity());

        if (rarityEntries == null || rarityEntries.isEmpty()) {
            return 0.0d;
        }
        return getRarityChance(entry.getRarity()) / rarityEntries.size();
    }

    @Override
    public double getRarityChance(CosmeticRarity rarity) {
        Integer weight = rarityWeights.get(rarity);

        if (weight == null || totalWeight == 0) {
            return 0.0d;
        }
        return ((double) weight / totalWeight) * 100.0d;
    }

    @Override
    public List<LootEntry> getEntries() {
        return entries;
    }

    @Override
    public Map<LootCategory, List<LootEntry>> getEntriesByCategory() {
        return entriesByCategory;
    }

    @Override
    public int getTotalWeight() {
        return totalWeight;
    }
}
