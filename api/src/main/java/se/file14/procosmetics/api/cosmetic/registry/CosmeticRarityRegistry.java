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
package se.file14.procosmetics.api.cosmetic.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;

import java.util.List;

/**
 * Manages all registered {@link CosmeticRarity} instances.
 */
public interface CosmeticRarityRegistry {

    /**
     * Loads all rarities from the configuration.
     */
    @ApiStatus.Internal
    void load();

    /**
     * Registers a cosmetic rarity.
     *
     * @param rarity the rarity to register
     */
    void register(CosmeticRarity rarity);

    /**
     * Gets a rarity by its key.
     *
     * @param key the rarity key
     * @return the rarity, or null if not found
     */
    @Nullable
    CosmeticRarity get(String key);

    /**
     * Gets a rarity by its key, returning a fallback rarity if not found.
     *
     * @param key the rarity key
     * @return the rarity, or the fallback rarity if not found
     */
    CosmeticRarity getSafely(String key);

    /**
     * Gets the fallback rarity used when a rarity is not found.
     *
     * @return the fallback rarity
     */
    CosmeticRarity getFallbackRarity();

    /**
     * Gets the list of current rarities in sorted from lowest to highest priority.
     *
     * @return the rarities
     */
    List<CosmeticRarity> getRarities();
}
