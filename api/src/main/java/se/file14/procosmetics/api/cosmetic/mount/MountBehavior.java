package se.file14.procosmetics.api.cosmetic.mount;

import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.nms.NMSEntity;

public interface MountBehavior extends CosmeticBehavior<MountType> {

    void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity);

    void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity);

    default boolean isTossItemsEnabled(CosmeticContext<MountType> context) {
        return context.getType().getCategory().getConfig().getBoolean("tossing_items");
    }
}