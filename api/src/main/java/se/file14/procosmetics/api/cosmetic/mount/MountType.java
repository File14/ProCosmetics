package se.file14.procosmetics.api.cosmetic.mount;

import org.bukkit.entity.EntityType;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of mount cosmetic.
 */
public interface MountType extends CosmeticType<MountType, MountBehavior> {

    /**
     * Gets the entity type used for this mount.
     *
     * @return the entity type that represents this mount
     */
    EntityType getEntityType();

    /**
     * Builder interface for constructing mount type instances.
     */
    interface Builder extends CosmeticType.Builder<MountType, MountBehavior, Builder> {

        /**
         * Sets the entity type to be used for this mount.
         *
         * @param entityType the entity type for the mount
         * @return this builder for method chaining
         */
        Builder entityType(EntityType entityType);

        /**
         * Builds and returns the configured mount type instance.
         *
         * @return the built mount type
         */
        @Override
        MountType build();
    }
}
