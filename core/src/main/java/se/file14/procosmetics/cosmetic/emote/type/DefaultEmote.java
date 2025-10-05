package se.file14.procosmetics.cosmetic.emote.type;

import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;

public class DefaultEmote implements EmoteBehavior {

    @Override
    public void onEquip(CosmeticContext<EmoteType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<EmoteType> context, int frame) {
    }

    @Override
    public void onUnequip(CosmeticContext<EmoteType> context) {
    }
}