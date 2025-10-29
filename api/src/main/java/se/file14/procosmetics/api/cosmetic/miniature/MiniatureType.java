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
package se.file14.procosmetics.api.cosmetic.miniature;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of miniature cosmetic.
 */
public interface MiniatureType extends CosmeticType<MiniatureType, MiniatureBehavior> {

    /**
     * Checks if this miniature's base entity is invisible.
     * When true, only the equipped items or models on the miniature are visible,
     * making the base armor stand invisible.
     *
     * @return true if the base entity is invisible, false otherwise
     */
    boolean hasInvisibility();

    /**
     * Checks if this miniature displays arms.
     * Relevant for armor stand entities where arms can be shown or hidden.
     *
     * @return true if the miniature has visible arms, false otherwise
     */
    boolean hasArms();

    /**
     * Builder interface for constructing miniature type instances.
     */
    interface Builder extends CosmeticType.Builder<MiniatureType, MiniatureBehavior, Builder> {

        /**
         * Sets whether the miniature's base entity is invisible.
         *
         * @param invisible true to make the base entity invisible, false for visible
         * @return this builder for method chaining
         */
        Builder invisible(boolean invisible);

        /**
         * Sets whether the miniature displays arms.
         *
         * @param arms true to show arms, false to hide arms
         * @return this builder for method chaining
         */
        Builder arms(boolean arms);

        /**
         * Builds and returns the configured miniature type instance.
         *
         * @return the built miniature type
         */
        @Override
        MiniatureType build();
    }
}
