package se.file14.procosmetics.api.cosmetic.pet;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

import javax.annotation.Nullable;

/**
 * Represents a type of pet cosmetic.
 */
public interface PetType extends CosmeticType<PetType, PetBehavior> {

    /**
     * Gets the entity type used for this pet.
     *
     * @return the entity type that represents this pet
     */
    EntityType getEntityType();

    /**
     * Checks if this pet uses the baby variant of the entity.
     *
     * @return true if the pet is a baby entity, false otherwise
     */
    boolean isBaby();

    /**
     * Gets the scale multiplier for this pet's size.
     * A scale of 1.0 represents normal size.
     *
     * @return the scale multiplier for the pet
     */
    double getScale();

    /**
     * Gets the sound played when this pet is spawned.
     *
     * @return the spawn sound, or null if no sound should be played
     */
    @Nullable
    Sound getSpawnSound();

    /**
     * Gets the item that can be tossed to this pet for interaction.
     * When a player tosses this item, the pet may react or perform special behaviors.
     *
     * @return the tossable item, or null if the pet should not toss any item
     */
    @Nullable
    ItemStack getTossItem();

    /**
     * Builder interface for constructing pet type instances.
     */
    interface Builder extends CosmeticType.Builder<PetType, PetBehavior, Builder> {

        /**
         * Sets the entity type to be used for this pet.
         *
         * @param entityType the entity type for the pet
         * @return this builder for method chaining
         */
        Builder entityType(EntityType entityType);

        /**
         * Sets whether this pet should use the baby variant of the entity.
         *
         * @param baby true to use baby variant, false for adult variant
         * @return this builder for method chaining
         */
        Builder baby(boolean baby);

        /**
         * Sets the scale multiplier for this pet's size.
         *
         * @param scale the scale multiplier (1.0 = normal size)
         * @return this builder for method chaining
         */
        Builder scale(double scale);

        /**
         * Sets the sound played when this pet is spawned.
         *
         * @param spawnSound the spawn sound, or null for no sound
         * @return this builder for method chaining
         */
        Builder spawnSound(@Nullable Sound spawnSound);

        /**
         * Sets the item that can be tossed to this pet for interaction.
         *
         * @param tossItem the tossable item, or null to disable item tossing
         * @return this builder for method chaining
         */
        Builder tossItem(@Nullable ItemStack tossItem);

        /**
         * Builds and returns the configured pet type instance.
         *
         * @return the built pet type
         */
        @Override
        PetType build();
    }
}
