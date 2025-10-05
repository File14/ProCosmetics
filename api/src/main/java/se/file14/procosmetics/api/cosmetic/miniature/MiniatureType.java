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
