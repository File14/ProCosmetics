package se.file14.procosmetics.cosmetic.miniature.type;

import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Parrot implements MiniatureBehavior {

    @Override
    public void onEquip(CosmeticContext<MiniatureType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity entity) {
    }

    @Override
    public void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity entity, int tick) {
    }

    @Override
    public void onUnequip(CosmeticContext<MiniatureType> context) {
    }
}