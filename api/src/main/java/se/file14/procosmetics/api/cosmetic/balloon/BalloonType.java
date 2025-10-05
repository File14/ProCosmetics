package se.file14.procosmetics.api.cosmetic.balloon;

import org.bukkit.entity.EntityType;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of balloon cosmetic.
 */
public interface BalloonType extends CosmeticType<BalloonType, BalloonBehavior> {

    /**
     * Gets the entity type used to represent this balloon.
     *
     * @return the entity type for the balloon
     */
    EntityType getEntityType();

    /**
     * Checks if this balloon uses the baby variant of the entity.
     *
     * @return true if the balloon is a baby entity, false otherwise
     */
    boolean isBaby();

    /**
     * Gets the scale multiplier for this balloon's size.
     * A scale of 1.0 represents normal size.
     *
     * @return the scale multiplier for the balloon
     */
    double getScale();

    /**
     * Builder interface for constructing balloon type instances.
     */
    interface Builder extends CosmeticType.Builder<BalloonType, BalloonBehavior, Builder> {

        /**
         * Sets the entity type to be used for this balloon.
         *
         * @param entityType the entity type for the balloon
         * @return this builder for method chaining
         */
        Builder entityType(EntityType entityType);

        /**
         * Sets whether this balloon should use the baby variant of the entity.
         *
         * @param baby true to use baby variant, false for adult variant
         * @return this builder for method chaining
         */
        Builder baby(boolean baby);

        /**
         * Sets the scale multiplier for this balloon's size.
         *
         * @param scale the scale multiplier (1.0 = normal size)
         * @return this builder for method chaining
         */
        Builder scale(double scale);

        /**
         * Builds and returns the configured balloon type instance.
         *
         * @return the built balloon type
         */
        @Override
        BalloonType build();
    }
}
