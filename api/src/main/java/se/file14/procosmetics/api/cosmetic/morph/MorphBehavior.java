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
import org.jetbrains.annotations.ApiStatus;
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
        return InteractionResult.NO_ACTION;
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
     * <p>
     * Defines whether the interaction succeeded, failed, or took no action,
     * and whether a cooldown should be applied.
     */
    enum InteractionResult {
        /**
         * Ability was used successfully, apply cooldown.
         */
        SUCCESS(true),

        /**
         * Ability failed, don't apply cooldown.
         */
        FAILED(false),

        /**
         * No action taken (e.g., wrong event type).
         */
        NO_ACTION(false);

        private final boolean applyCooldown;

        @ApiStatus.Internal
        InteractionResult(boolean applyCooldown) {
            this.applyCooldown = applyCooldown;
        }

        /**
         * Checks whether the morph ability should apply a cooldown after use.
         *
         * @return {@code true} if cooldown should be applied
         */
        public boolean shouldApplyCooldown() {
            return applyCooldown;
        }
    }
}
