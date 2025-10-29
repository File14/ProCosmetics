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
package se.file14.procosmetics.api.cosmetic.arroweffect;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of arrow effect cosmetic.
 */
public interface ArrowEffectType extends CosmeticType<ArrowEffectType, ArrowEffectBehavior> {

    /**
     * Builder interface for constructing arrow effect type instances.
     */
    interface Builder extends CosmeticType.Builder<ArrowEffectType, ArrowEffectBehavior, Builder> {

        /**
         * Builds and returns the configured arrow effect type instance.
         *
         * @return the built arrow effect type
         */
        @Override
        ArrowEffectType build();
    }
}
