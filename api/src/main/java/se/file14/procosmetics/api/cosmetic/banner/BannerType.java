package se.file14.procosmetics.api.cosmetic.banner;

import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.util.AnimationFrame;

import javax.annotation.Nullable;
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
