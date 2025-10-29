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
package se.file14.procosmetics.api.cosmetic.banner;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.util.AnimationFrame;

import java.util.List;

/**
 * Represents a type of banner cosmetic.
 */
public interface BannerType extends CosmeticType<BannerType, BannerBehavior> {

    /**
     * Checks if this banner has an animation.
     *
     * @return true if the banner has animation, false otherwise
     */
    default boolean hasAnimation() {
        return getFrames() != null && !getFrames().isEmpty() && getTickInterval() > 0;
    }

    /**
     * Gets the tick interval between animation frames.
     * This determines how long each frame is displayed before transitioning to the next.
     *
     * @return the tick interval in server ticks
     */
    long getTickInterval();

    /**
     * Gets the list of animation frames for this banner.
     * Each frame represents a different visual state in the banner's animation sequence.
     *
     * @return the list of animation frames, or null if the banner has no animation
     */
    @Nullable
    List<AnimationFrame> getFrames();

    /**
     * Builder interface for constructing banner type instances.
     */
    interface Builder extends CosmeticType.Builder<BannerType, BannerBehavior, Builder> {

        /**
         * Sets the tick interval between animation frames.
         *
         * @param tickInterval the interval in server ticks
         * @return this builder for method chaining
         */
        BannerType.Builder tickInterval(long tickInterval);

        /**
         * Sets the complete list of animation frames for this banner.
         *
         * @param frames the list of animation frames
         * @return this builder for method chaining
         */
        BannerType.Builder frames(List<AnimationFrame> frames);

        /**
         * Adds a single animation frame to this banner.
         * Frames are played in the order they are added.
         *
         * @param frame the animation frame to add
         * @return this builder for method chaining
         */
        BannerType.Builder addFrame(AnimationFrame frame);

        /**
         * Builds and returns the configured banner type instance.
         *
         * @return the built banner type
         */
        @Override
        BannerType build();
    }
}
