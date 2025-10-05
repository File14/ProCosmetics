package se.file14.procosmetics.api.cosmetic.morph;

import org.bukkit.entity.EntityType;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of morph cosmetic.
 */
public interface MorphType extends CosmeticType<MorphType, MorphBehavior> {

    /**
     * Gets the cooldown period for this morph's special ability in seconds.
     * The player must wait this duration after using the ability before using it again.
     *
     * @return the cooldown duration in seconds
     */
    double getCooldown();

    /**
     * Gets the entity type that the player transforms into.
     *
     * @return the target entity type for this morph
     */
    EntityType getEntityType();

    /**
     * Checks if this morph has a special ability.
     * When true, the player can activate a unique ability associated with this morph.
     *
     * @return true if the morph has a special ability, false otherwise
     */
    boolean hasAbility();

    /**
     * Builder interface for constructing morph type instances.
     */
    interface Builder extends CosmeticType.Builder<MorphType, MorphBehavior, Builder> {

        /**
         * Sets the cooldown period for this morph's special ability.
         *
         * @param cooldown the cooldown duration in seconds
         * @return this builder for method chaining
         */
        Builder cooldown(double cooldown);

        /**
         * Sets the entity type that the player transforms into.
         *
         * @param entityType the target entity type
         * @return this builder for method chaining
         */
        Builder entityType(EntityType entityType);

        /**
         * Sets whether this morph has a special ability.
         *
         * @param ability true to enable special ability, false otherwise
         * @return this builder for method chaining
         */
        Builder ability(boolean ability);

        /**
         * Builds and returns the configured morph type instance.
         *
         * @return the built morph type
         */
        @Override
        MorphType build();
    }
}
