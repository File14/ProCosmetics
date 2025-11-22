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
package se.file14.procosmetics.api.cosmetic.morph;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Defines the behavior for morph cosmetics.
 *
 * @see CosmeticBehavior
 * @see MorphType
 */
public interface MorphBehavior extends CosmeticBehavior<MorphType> {

    /**
     * Sets up the morph entity when it is initialized.
     *
     * @param context   the context containing information about the morph cosmetic
     * @param nmsEntity the underlying NMS entity representing the morph
     */
    void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity);

    /**
     * Called when the player interacts (left or right click) while morphed.
     * <p>
     * This allows morphs to define special abilities or actions when the player interacts.
     *
     * @param context   the context containing information about the morph cosmetic
     * @param event     the {@link PlayerInteractEvent} that triggered the interaction
     * @param nmsEntity the NMS entity representing the active morph
     * @return an {@link InteractionResult} describing the outcome of the interaction
     */
    InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity);

    /**
     * Called when the player toggles sneak while morphed.
     * <p>
     * This can be used to trigger morph abilities that activate through sneaking.
     *
     * @param context   the context containing information about the morph cosmetic
     * @param event     the {@link PlayerToggleSneakEvent} that triggered the toggle
     * @param nmsEntity the NMS entity representing the active morph
     * @return an {@link InteractionResult} describing the outcome of the interaction
     */
    default InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        return InteractionResult.noAction();
    }

    /**
     * Called every tick while the morph is equipped.
     *
     * @param context   the context containing information about the morph cosmetic
     * @param nmsEntity the underlying NMS entity representing the morph
     */
    void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity);

    /**
     * Determines whether this morph supports attack animations.
     *
     * @return {@code true} if the morph supports attack animations
     */
    default boolean hasAttackAnimation() {
        return false;
    }

    /**
     * Determines whether this morph supports item-hold animations.
     *
     * @return {@code true} if the morph supports item-hold animations
     */
    default boolean hasItemHoldAnimation() {
        return false;
    }

    /**
     * Represents the result of a morph ability interaction.
     */
    class InteractionResult {
        private final boolean applyCooldown;

        private InteractionResult(boolean applyCooldown) {
            this.applyCooldown = applyCooldown;
        }

        /**
         * Creates a result indicating the ability was used successfully.
         * Applies cooldown.
         *
         * @return a successful interaction result with cooldown
         */
        public static InteractionResult success() {
            return new InteractionResult(true);
        }

        /**
         * Creates a result indicating the ability failed.
         * Doesn't apply cooldown.
         *
         * @return a failed interaction result without cooldown
         */
        public static InteractionResult fail() {
            return new InteractionResult(false);
        }

        /**
         * Creates a result indicating no action was taken.
         * Doesn't apply cooldown.
         *
         * @return a no-action interaction result without cooldown
         */
        public static InteractionResult noAction() {
            return new InteractionResult(false);
        }

        /**
         * Creates a custom result with specific cooldown behavior.
         *
         * @param applyCooldown whether to apply cooldown
         * @return a custom interaction result with the specified cooldown behavior
         */
        public static InteractionResult of(boolean applyCooldown) {
            return new InteractionResult(applyCooldown);
        }

        /**
         * Checks whether the morph ability should apply a cooldown after use.
         *
         * @return {@code true} if cooldown should be applied
         */
        public boolean shouldApplyCooldown() {
            return applyCooldown;
        }

        /**
         * Returns a new result with cooldown application modified.
         *
         * @param cooldown whether to apply cooldown
         * @return a new interaction result with the modified cooldown behavior
         */
        public InteractionResult withApplyCooldown(boolean cooldown) {
            return new InteractionResult(cooldown);
        }
    }
}
