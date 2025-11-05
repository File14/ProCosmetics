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
package se.file14.procosmetics.api.treasure.loot;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

import java.util.List;

/**
 * Represents a loot table that rewards cosmetic items.
 */
public interface CosmeticLoot extends LootTable<CosmeticType<?, ?>> {

    /**
     * Gets the list of cosmetic types that can be rewarded from this loot table.
     *
     * @return the list of available cosmetic types
     */
    List<CosmeticType<?, ?>> getCosmeticTypes();
}
