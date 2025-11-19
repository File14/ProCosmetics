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
package se.file14.procosmetics.cosmetic.registry;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRarityRegistry;
import se.file14.procosmetics.cosmetic.CosmeticRarityImpl;
import se.file14.procosmetics.util.EnumUtil;

import javax.annotation.Nullable;
import java.util.*;

public class CosmeticRarityRegistryImpl implements CosmeticRarityRegistry {

    private final Map<String, CosmeticRarity> rarities = new HashMap<>();
    private final List<CosmeticRarity> sortedRarities = new ArrayList<>();
    private final ProCosmeticsPlugin plugin;
    private CosmeticRarity fallbackRarity;

    public CosmeticRarityRegistryImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    @Override
    public void load() {
        rarities.clear();
        fallbackRarity = null;

        Config config = plugin.getConfigManager().register("rarities");

        for (String key : config.getSectionKeys("rarities")) {
            String path = "rarities." + key + ".";
            int priority = config.getInt(path + "priority");
            String mainColor = config.getString(path + "colors.primary");
            String secondaryColor = config.getString(path + "colors.secondary");
            int detonations = config.getInt(path + "firework.detonations");
            int tickInterval = config.getInt(path + "firework.tick_interval");

            FireworkEffect fireworkEffect = FireworkEffect.builder()
                    .with(EnumUtil.getType(FireworkEffect.Type.class, config.getString(path + "firework.type")))
                    .withColor(Color.fromRGB(config.getInt(path + "firework.color")))
                    .withFade(Color.fromRGB(config.getInt(path + "firework.fade")))
                    .flicker(config.getBoolean(path + "firework.flicker"))
                    .trail(config.getBoolean(path + "firework.trail"))
                    .build();

            CosmeticRarityImpl rarity = new CosmeticRarityImpl(
                    key.toUpperCase(),
                    priority,
                    mainColor,
                    secondaryColor,
                    detonations,
                    tickInterval,
                    fireworkEffect
            );
            register(rarity);

            if (fallbackRarity == null) {
                fallbackRarity = rarity;
            }
        }

        if (fallbackRarity == null) {
            fallbackRarity = new CosmeticRarityImpl("default", 0, "", "", 0, 0, null);
        }
    }

    @Override
    public void register(CosmeticRarity rarity) {
        rarities.put(rarity.getKey().toLowerCase(), rarity);

        sortedRarities.remove(rarity);
        sortedRarities.add(rarity);
        sortedRarities.sort(Comparator.comparingInt(CosmeticRarity::getPriority));
    }

    @Override
    @Nullable
    public CosmeticRarity get(String key) {
        return rarities.get(key.toLowerCase());
    }

    @Override
    public CosmeticRarity getSafely(String key) {
        return rarities.getOrDefault(key.toLowerCase(), fallbackRarity);
    }

    @Override
    public CosmeticRarity getFallbackRarity() {
        return fallbackRarity;
    }

    @Override
    public List<CosmeticRarity> getRarities() {
        return sortedRarities;
    }
}
