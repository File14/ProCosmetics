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
package se.file14.procosmetics.api.treasure.animation;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;

import java.util.Collection;

/**
 * Registry for treasure chest animation factories.
 * Stores factory functions that can create animation instances per player.
 */
public interface TreasureChestAnimationRegistry {

    /**
     * Functional interface for creating animation instances.
     */
    @FunctionalInterface
    interface AnimationFactory {
        /**
         * Creates a new animation instance.
         *
         * @param plugin        the ProCosmetics plugin instance
         * @param platform      the treasure chest platform
         * @param treasureChest the treasure chest being opened
         * @param user          the user opening the chest
         * @return a new animation instance
         */
        TreasureChestAnimation create(ProCosmetics plugin, TreasureChestPlatform platform,
                                      TreasureChest treasureChest, User user);
    }

    /**
     * Registers a treasure chest animation factory with a key.
     *
     * @param key     the unique key for this animation type
     * @param factory the factory function that creates animation instances
     */
    void register(String key, AnimationFactory factory);

    /**
     * Gets an animation factory by its key.
     *
     * @param key the animation key
     * @return the animation factory, or null if not found
     */
    @Nullable
    AnimationFactory get(String key);

    /**
     * Gets all registered animation factories.
     *
     * @return the collection of animation factories
     */
    Collection<AnimationFactory> getAnimations();
}
