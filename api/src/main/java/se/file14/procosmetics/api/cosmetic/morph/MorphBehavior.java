package se.file14.procosmetics.api.cosmetic.morph;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.nms.NMSEntity;

public interface MorphBehavior extends CosmeticBehavior<MorphType> {

    void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity);

    /**
     * Called when the player interacts (left/right click)
     *
     * @param context The cosmetic context
     * @param event   The interaction event
     * @return The result of the interaction
     */
    InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity);

    /**
     * Called when the player toggles sneak
     *
     * @param context The cosmetic context
     * @param event   The sneak event
     * @return The result of the interaction
     */
    default InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        return InteractionResult.NO_ACTION;
    }

    /**
     * Called every tick while the morph is equipped
     */
    void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity);

    /**
     * Whether this morph has attack animations
     */
    default boolean hasAttackAnimation() {
        return false;
    }

    /**
     * Whether this morph has item hold animations
     */
    default boolean hasItemHoldAnimation() {
        return false;
    }

    /**
     * Result of a morph ability interaction
     */
    enum InteractionResult {
        /**
         * Ability was used successfully, apply cooldown
         */
        SUCCESS(true),

        /**
         * Ability failed, don't apply cooldown
         */
        FAILED(false),

        /**
         * No action taken (e.g., wrong event type)
         */
        NO_ACTION(false);

        private final boolean applyCooldown;

        InteractionResult(boolean applyCooldown) {
            this.applyCooldown = applyCooldown;
        }

        public boolean shouldApplyCooldown() {
            return applyCooldown;
        }
    }
}