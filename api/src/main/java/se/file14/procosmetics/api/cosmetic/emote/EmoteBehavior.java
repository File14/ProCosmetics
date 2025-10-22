package se.file14.procosmetics.api.cosmetic.emote;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for emote cosmetics.
 *
 * @see CosmeticBehavior
 * @see EmoteType
 */
public interface EmoteBehavior extends CosmeticBehavior<EmoteType> {

    /**
     * Called every tick to update the emote animation.
     *
     * @param context the context containing information about the emote,
     *                including the player and cosmetic type
     * @param frame   the current animation frame number
     */
    void onUpdate(CosmeticContext<EmoteType> context, int frame);
}
