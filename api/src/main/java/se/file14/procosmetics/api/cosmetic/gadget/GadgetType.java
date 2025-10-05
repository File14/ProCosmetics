package se.file14.procosmetics.api.cosmetic.gadget;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.util.structure.StructureData;

/**
 * Represents a type of gadget cosmetic.
 */
public interface GadgetType extends CosmeticType<GadgetType, GadgetBehavior> {

    /**
     * Gets the cooldown period for this gadget in seconds.
     * The player must wait this duration after using the gadget before using it again.
     *
     * @return the cooldown duration in seconds
     */
    double getCooldown();

    /**
     * Gets the duration this gadget remains active in seconds.
     * This applies to gadgets with sustained effects or temporary spawns.
     *
     * @return the active duration in seconds
     */
    double getDuration();

    /**
     * Gets the duration this gadget remains active in server ticks.
     *
     * @return the active duration in ticks
     */
    long getDurationInTicks();

    /**
     * Gets the number of uses included when this gadget is purchased.
     * This determines how much ammunition the player receives per purchase.
     *
     * @return the number of uses per purchase
     */
    int getPurchaseAmount();

    /**
     * Gets the ammunition cost per use of this gadget.
     * Each activation of the gadget consumes this amount of ammunition.
     *
     * @return the ammunition cost per use
     */
    int getAmmoCost();

    /**
     * Checks if this gadget has infinite ammunition.
     * Gadgets with infinity ammo can be used unlimited times without consuming ammunition.
     *
     * @return true if the gadget has infinite ammo, false otherwise
     */
    boolean hasInfinityAmmo();

    /**
     * Checks if additional ammunition for this gadget can be purchased.
     * When true, players can buy more uses of this gadget.
     *
     * @return true if ammo is purchasable, false otherwise
     */
    boolean hasPurchasableAmmo();

    /**
     * Gets the structure data for this gadget.
     * Some gadgets spawn structures when activated.
     *
     * @return the structure data, or null if this gadget doesn't use structures
     */
    @Nullable
    StructureData getStructure();

    /**
     * Builder interface for constructing gadget type instances.
     */
    interface Builder extends CosmeticType.Builder<GadgetType, GadgetBehavior, Builder> {

        /**
         * Sets the cooldown period for this gadget.
         *
         * @param cooldown the cooldown duration in seconds
         * @return this builder for method chaining
         */
        Builder cooldown(double cooldown);

        /**
         * Sets the duration this gadget remains active.
         *
         * @param duration the active duration in seconds
         * @return this builder for method chaining
         */
        Builder duration(double duration);

        /**
         * Sets the number of uses included per purchase.
         *
         * @param purchaseAmount the number of uses per purchase
         * @return this builder for method chaining
         */
        Builder purchaseAmount(int purchaseAmount);

        /**
         * Sets the ammunition cost per use.
         *
         * @param ammoCost the ammunition cost per use
         * @return this builder for method chaining
         */
        Builder ammoCost(int ammoCost);

        /**
         * Sets whether this gadget has infinite ammunition.
         *
         * @param infinityAmmo true for infinite ammo, false for limited ammo
         * @return this builder for method chaining
         */
        Builder infinityAmmo(boolean infinityAmmo);

        /**
         * Sets whether additional ammunition can be purchased.
         *
         * @param purchasableAmmo true to allow ammo purchases, false otherwise
         * @return this builder for method chaining
         */
        Builder purchasableAmmo(boolean purchasableAmmo);

        /**
         * Sets the structure data for this gadget.
         *
         * @param structure the structure data, or null if not applicable
         * @return this builder for method chaining
         */
        Builder structure(StructureData structure);

        /**
         * Builds and returns the configured gadget type instance.
         *
         * @return the built gadget type
         */
        @Override
        GadgetType build();
    }
}
