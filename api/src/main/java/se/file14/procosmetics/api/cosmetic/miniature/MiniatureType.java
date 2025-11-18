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

import org.bukkit.entity.EntityType;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of miniature cosmetic.
 */
public interface MiniatureType extends CosmeticType<MiniatureType, MiniatureBehavior> {

    /**
     * Gets the entity type used for this miniature.
     *
     * @return the entity type that represents this miniature
     */
    EntityType getEntityType();

    /**
     * Checks if this miniature uses the baby variant of the entity.
     *
     * @return true if the miniature is a baby entity, false otherwise
     */
    boolean isBaby();

    /**
     * Gets the scale multiplier for this miniature's size.
     * A scale of 1.0 represents normal size.
     *
     * @return the scale multiplier for the miniature
     */
    double getScale();

    /**
     * Builder interface for constructing miniature type instances.
     */
    interface Builder extends CosmeticType.Builder<MiniatureType, MiniatureBehavior, Builder> {

        /**
         * Sets the entity type to be used for this miniature.
         *
         * @param entityType the entity type for the miniature
         * @return this builder for method chaining
         */
        MiniatureType.Builder entityType(EntityType entityType);

        /**
         * Sets whether this miniature should use the baby variant of the entity.
         *
         * @param baby true to use baby variant, false for adult variant
         * @return this builder for method chaining
         */
        MiniatureType.Builder baby(boolean baby);

        /**
         * Sets the scale multiplier for this miniature's size.
         *
         * @param scale the scale multiplier (1.0 = normal size)
         * @return this builder for method chaining
         */
        MiniatureType.Builder scale(double scale);

        /**
         * Builds and returns the configured miniature type instance.
         *
         * @return the built miniature type
         */
        @Override
        MiniatureType build();
    }
}
