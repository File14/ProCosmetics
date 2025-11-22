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
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
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
     * @param action          the interact action the player performed
     * @param clickedBlock    the block the player interacted with, or {@code null} if none
     * @param clickedPosition the clicked position within the block, or {@code null} if none
     * @return an {@link InteractionResult} indicating the outcome of the interaction
     */
    InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition);

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
    class InteractionResult {

        private final boolean cancelEvent;
        private final boolean consumeAmmo;
        private final boolean applyCooldown;

        private InteractionResult(boolean cancelEvent, boolean consumeAmmo, boolean applyCooldown) {
            this.cancelEvent = cancelEvent;
            this.consumeAmmo = consumeAmmo;
            this.applyCooldown = applyCooldown;
        }

        /**
         * Creates a result indicating successful interaction.
         * Cancels event, consumes ammo, and applies cooldown.
         *
         * @return a successful interaction result
         */
        public static InteractionResult success() {
            return new InteractionResult(true, true, true);
        }

        /**
         * Creates a result indicating failed interaction.
         * Cancels event but doesn't consume ammo or apply cooldown.
         *
         * @return a failed interaction result
         */
        public static InteractionResult fail() {
            return new InteractionResult(true, false, false);
        }

        /**
         * Creates a custom result with specific behaviors.
         *
         * @param cancelEvent   whether to cancel the original interaction event
         * @param consumeAmmo   whether to consume ammo
         * @param applyCooldown whether to apply cooldown
         * @return a custom interaction result
         */
        public static InteractionResult of(boolean cancelEvent, boolean consumeAmmo, boolean applyCooldown) {
            return new InteractionResult(cancelEvent, consumeAmmo, applyCooldown);
        }

        /**
         * Checks whether the original player interaction event should be cancelled.
         *
         * @return {@code true} if the event should be cancelled
         */
        public boolean shouldCancelEvent() {
            return cancelEvent;
        }

        /**
         * Checks whether the gadget should consume ammo after this interaction.
         *
         * @return {@code true} if ammo should be consumed
         */
        public boolean shouldConsumeAmmo() {
            return consumeAmmo;
        }

        /**
         * Checks whether the gadget should apply a cooldown after this interaction.
         *
         * @return {@code true} if cooldown should be applied
         */
        public boolean shouldApplyCooldown() {
            return applyCooldown;
        }

        /**
         * Returns a new result with event cancellation modified.
         *
         * @param cancel whether to cancel the event
         * @return a new interaction result with modified event cancellation
         */
        public InteractionResult withCancelEvent(boolean cancel) {
            return new InteractionResult(cancel, this.consumeAmmo, this.applyCooldown);
        }

        /**
         * Returns a new result with ammo consumption modified.
         *
         * @param consume whether to consume ammo
         * @return a new interaction result with modified ammo consumption
         */
        public InteractionResult withConsumeAmmo(boolean consume) {
            return new InteractionResult(this.cancelEvent, consume, this.applyCooldown);
        }

        /**
         * Returns a new result with cooldown application modified.
         *
         * @param cooldown whether to apply cooldown
         * @return a new interaction result with modified cooldown application
         */
        public InteractionResult withApplyCooldown(boolean cooldown) {
            return new InteractionResult(this.cancelEvent, this.consumeAmmo, cooldown);
        }
    }
}
