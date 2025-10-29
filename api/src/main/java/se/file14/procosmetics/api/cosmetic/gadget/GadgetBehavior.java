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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for gadget cosmetics.
 *
 * @see CosmeticBehavior
 * @see GadgetType
 */
public interface GadgetBehavior extends CosmeticBehavior<GadgetType> {

    /**
     * Called when a player interacts with their equipped gadget.
     *
     * @param context         the context containing information about the gadget and user
     * @param clickedBlock    the block the player interacted with, or {@code null} if none
     * @param clickedPosition the clicked position within the block, or {@code null} if none
     * @return an {@link InteractionResult} indicating the outcome of the interaction
     */
    InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition);

    /**
     * Called every tick while the gadget is equipped.
     *
     * @param context the context containing information about the gadget and user
     */
    void onUpdate(CosmeticContext<GadgetType> context);

    /**
     * Checks whether the gadget requires the player to be standing on the ground to be used.
     *
     * @return {@code true} if the player must be on the ground to use the gadget
     */
    boolean requiresGroundOnUse();

    /**
     * Checks whether there is enough space at the specified location to use the gadget.
     *
     * @param location the location to check for space
     * @return {@code true} if there is enough space to use the gadget
     */
    boolean isEnoughSpaceToUse(Location location);

    /**
     * Checks whether the gadget should automatically unequip when the player teleports.
     *
     * @return {@code true} if the gadget should unequip on teleport
     */
    boolean shouldUnequipOnTeleport();

    /**
     * Represents the result of a gadget interaction.
     */
    enum InteractionResult {
        /**
         * Interaction was successful, consume ammo and apply cooldown.
         */
        SUCCESS(true, true),

        /**
         * Interaction was successful but don't consume ammo (e.g., on cooldown check).
         */
        SUCCESS_NO_AMMO(true, false),

        /**
         * Interaction was successful, consume ammo and apply cooldown.
         */
        SUCCESS_NO_EVENT_CANCEL(false, true, true),

        /**
         * Interaction failed, don't consume ammo or apply cooldown.
         */
        FAILED(true, false),

        /**
         * Interaction failed and should not cancel the original event.
         */
        FAILED_NO_EVENT_CANCEL(false, false, false),

        /**
         * Interaction was successful, apply cooldown but don't consume ammo.
         */
        SUCCESS_NO_AMMO_WITH_COOLDOWN(true, false, true),

        /**
         * Interaction was successful, consume ammo but don't apply cooldown.
         */
        SUCCESS_NO_COOLDOWN(true, true, false);

        private final boolean cancelEvent;
        private final boolean consumeAmmo;
        private final boolean applyCooldown;

        @ApiStatus.Internal
        InteractionResult(boolean cancelEvent, boolean consumeAmmo, boolean applyCooldown) {
            this.cancelEvent = cancelEvent;
            this.consumeAmmo = consumeAmmo;
            this.applyCooldown = applyCooldown;
        }

        @ApiStatus.Internal
        InteractionResult(boolean cancelEvent, boolean consumeAmmo) {
            this(cancelEvent, consumeAmmo, consumeAmmo); // Default: cooldown follows ammo consumption
        }

        /**
         * Checks whether the original player interaction event should be cancelled.
         *
         * @return {@code true} if the event should be cancelled
         */
        @ApiStatus.Internal
        public boolean shouldCancelEvent() {
            return cancelEvent;
        }

        /**
         * Checks whether the gadget should consume ammo after this interaction.
         *
         * @return {@code true} if ammo should be consumed
         */
        @ApiStatus.Internal
        public boolean shouldConsumeAmmo() {
            return consumeAmmo;
        }

        /**
         * Checks whether the gadget should apply a cooldown after this interaction.
         *
         * @return {@code true} if cooldown should be applied
         */
        @ApiStatus.Internal
        public boolean shouldApplyCooldown() {
            return applyCooldown;
        }
    }
}
