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
package se.file14.procosmetics.treasure.loot.number;

import se.file14.procosmetics.api.treasure.loot.number.RangedIntProvider;

import java.security.SecureRandom;

public class RangedIntProviderImpl extends IntProviderImpl implements RangedIntProvider {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final int min;
    private final int max;

    public RangedIntProviderImpl(int min, int max) {
        this.min = Math.max(Math.min(min, max), 1);
        this.max = Math.max(Math.max(min, max), 1);
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int get() {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
