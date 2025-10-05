package se.file14.procosmetics.api.cosmetic.gadget;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface GadgetBehavior extends CosmeticBehavior<GadgetType> {

    InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition);

    void onUpdate(CosmeticContext<GadgetType> context);

    boolean requiresGroundOnUse();

    boolean isEnoughSpaceToUse(Location location);

    boolean shouldUnequipOnTeleport();

    /**
     * Result of a gadget interaction
     */
    enum InteractionResult {
        /**
         * Interaction was successful, consume ammo and apply cooldown
         */
        SUCCESS(true, true),

        /**
         * Interaction was successful but don't consume ammo (e.g., on cooldown check)
         */
        SUCCESS_NO_AMMO(true, false),

        /**
         * Interaction was successful, consume ammo and apply cooldown
         */
        SUCCESS_NO_EVENT_CANCEL(false, true, true),

        /**
         * Interaction failed, don't consume ammo or apply cooldown
         */
        FAILED(true, false),

        FAILED_NO_EVENT_CANCEL(false, false, false),

        /**
         * Interaction was successful, apply cooldown but don't consume ammo
         */
        SUCCESS_NO_AMMO_WITH_COOLDOWN(true, false, true),

        /**
         * Interaction was successful, consume ammo but don't apply cooldown
         */
        SUCCESS_NO_COOLDOWN(true, true, false);

        private final boolean cancelEvent;
        private final boolean consumeAmmo;
        private final boolean applyCooldown;

        InteractionResult(boolean cancelEvent, boolean consumeAmmo) {
            this(cancelEvent, consumeAmmo, consumeAmmo); // Default: cooldown follows ammo consumption
        }

        InteractionResult(boolean cancelEvent, boolean consumeAmmo, boolean applyCooldown) {
            this.cancelEvent = cancelEvent;
            this.consumeAmmo = consumeAmmo;
            this.applyCooldown = applyCooldown;
        }

        public boolean shouldCancelEvent() {
            return cancelEvent;
        }

        public boolean shouldConsumeAmmo() {
            return consumeAmmo;
        }

        public boolean shouldApplyCooldown() {
            return applyCooldown;
        }
    }
}