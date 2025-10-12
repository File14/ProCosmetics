package se.file14.procosmetics.api.cosmetic.miniature;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.nms.NMSEntity;

public interface MiniatureBehavior extends CosmeticBehavior<MiniatureType> {

    void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity);

    void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity, int tick);
}