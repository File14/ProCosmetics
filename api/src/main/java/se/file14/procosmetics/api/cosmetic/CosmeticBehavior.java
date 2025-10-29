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

/**
 * Represents the behavior logic for a cosmetic type.
 *
 * @param <T> the cosmetic type associated with this behavior
 * @see CosmeticType
 * @see CosmeticContext
 */
public interface CosmeticBehavior<T extends CosmeticType<T, ?>> {

    /**
     * Called when the cosmetic is equipped by a user.
     *
     * @param context the context containing information about the cosmetic and user
     */
    void onEquip(CosmeticContext<T> context);

    /**
     * Called when the cosmetic is unequipped by a user.
     *
     * @param context the context containing information about the cosmetic and user
     */
    void onUnequip(CosmeticContext<T> context);
}
