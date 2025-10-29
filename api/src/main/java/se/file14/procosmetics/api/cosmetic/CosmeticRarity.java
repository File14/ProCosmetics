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
package se.file14.procosmetics.api.cosmetic;

import org.bukkit.FireworkEffect;
import se.file14.procosmetics.api.locale.Translator;

/**
 * Represents the rarity tier of a cosmetic.
 *
 * @see se.file14.procosmetics.api.cosmetic.registry.CosmeticRarityRegistry
 */
public interface CosmeticRarity {

    /**
     * Gets the unique key of this rarity.
     *
     * @return the rarity key
     */
    String getKey();

    /**
     * Gets the localized display name of this rarity.
     *
     * @param translator the {@link Translator} used to resolve localization
     * @return the localized rarity name
     */
    String getName(Translator translator);

    String getPrimaryColor();

    String getSecondaryColor();

    /**
     * Gets the number of detonations for the firework effect associated with this rarity.
     *
     * @return the number of firework detonations
     */
    int getDetonations();

    /**
     * Gets the interval between firework detonations in ticks.
     *
     * @return the interval between detonations, in ticks
     */
    int getTickInterval();

    /**
     * Gets the firework effect associated with this rarity.
     *
     * @return the {@link FireworkEffect} representing this rarity’s effect
     */
    FireworkEffect getFireworkEffect();
}
