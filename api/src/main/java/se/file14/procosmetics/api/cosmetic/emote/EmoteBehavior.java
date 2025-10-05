package se.file14.procosmetics.api.cosmetic.emote;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface EmoteBehavior extends CosmeticBehavior<EmoteType> {

    void onUpdate(CosmeticContext<EmoteType> context, int frame);

}