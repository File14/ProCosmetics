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
package se.file14.procosmetics.api.cosmetic.gadget;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.treasure.loot.number.IntProvider;
import se.file14.procosmetics.api.util.structure.StructureData;

import java.util.Map;

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
     * Gets the structure data for this gadget.
     * Some gadgets spawn structures when activated.
     *
     * @return the structure data, or null if this gadget doesn't use structures
     */
    @Nullable
    StructureData getStructure();

    /**
     * Gets the ammo rarity for this gadget.
     *
     * @return the ammo rarity
     */
    CosmeticRarity getAmmoRarity();

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
     * Gets the number of uses included when this gadget is purchased.
     * This determines how much ammunition the player receives per purchase.
     *
     * @return the number of uses per purchase
     */
    int getAmmoPurchaseAmount();

    /**
     * Gets the ammunition cost per use of this gadget.
     * Each activation of the gadget consumes this amount of ammunition.
     *
     * @return the ammunition cost per use
     */
    int getAmmoCost();

    /**
     * Gets the ammunition amount providers for each treasure chest type.
     * <p>
     * When this gadget is looted from a treasure chest, the corresponding IntProvider
     * determines how much ammo is awarded alongside it. Only treasure chest types present
     * in this map can contain this gadget as loot.
     *
     * @return map of chest type to ammo provider
     */
    Map<String, IntProvider> getAmmoLoot();

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
         * Sets the structure data for this gadget.
         *
         * @param structure the structure data, or null if not applicable
         * @return this builder for method chaining
         */
        Builder structure(StructureData structure);

        /**
         * Sets the ammo rarity for this gadget.
         *
         * @param rarity the ammo rarity
         * @return this builder for method chaining
         */
        Builder ammoRarity(CosmeticRarity rarity);

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
         * Sets the number of uses included per purchase.
         *
         * @param ammoPurchaseAmount the number of uses per purchase
         * @return this builder for method chaining
         */
        Builder ammoPurchaseAmount(int ammoPurchaseAmount);

        /**
         * Sets the ammunition cost per use.
         *
         * @param ammoCost the ammunition cost per use
         * @return this builder for method chaining
         */
        Builder ammoCost(int ammoCost);

        /**
         * Sets the ammo loot for treasure chests.
         * <p>
         * Only treasure chest types present in this map can contain this gadget as loot.
         * Each provider determines how much gadget ammo is awarded.
         *
         * @param ammoLoot map of chest type to ammo provider
         * @return this builder for method chaining
         */
        Builder ammoLoot(Map<String, IntProvider> ammoLoot);

        /**
         * Builds and returns the configured gadget type instance.
         *
         * @return the built gadget type
         */
        @Override
        GadgetType build();
    }
}
