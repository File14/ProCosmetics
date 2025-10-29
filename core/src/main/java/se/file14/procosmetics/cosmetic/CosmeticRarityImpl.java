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
package se.file14.procosmetics.cosmetic;

import org.bukkit.FireworkEffect;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.locale.Translator;

public class CosmeticRarityImpl implements CosmeticRarity {

    private final String key;
    private final int priority;
    private final String mainColor;
    private final String secondaryColor;
    private final int detonations;
    private final int tickInterval;
    private final FireworkEffect fireworkEffect;

    public CosmeticRarityImpl(String key,
                              int priority,
                              String mainColor,
                              String secondaryColor,
                              int detonations,
                              int tickInterval,
                              FireworkEffect fireworkEffect) {
        this.key = key.toLowerCase();
        this.priority = priority;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.detonations = detonations;
        this.tickInterval = tickInterval;
        this.fireworkEffect = fireworkEffect;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName(Translator translator) {
        return translator.translateRaw("rarity." + key);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getPrimaryColor() {
        return mainColor;
    }

    @Override
    public String getSecondaryColor() {
        return secondaryColor;
    }

    @Override
    public int getDetonations() {
        return detonations;
    }

    @Override
    public int getTickInterval() {
        return tickInterval;
    }

    @Override
    public FireworkEffect getFireworkEffect() {
        return fireworkEffect;
    }
}
