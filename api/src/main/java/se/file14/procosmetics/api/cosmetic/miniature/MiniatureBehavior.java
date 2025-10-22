package se.file14.procosmetics.api.cosmetic.miniature;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Defines the behavior for miniature cosmetics.
 *
 * @see CosmeticBehavior
 * @see MiniatureType
 */
public interface MiniatureBehavior extends CosmeticBehavior<MiniatureType> {

    /**
     * Called when the mount entity is initialized.
     *
     * @param context   the context containing information about the pet cosmetic
     * @param nmsEntity the underlying NMS entity representing the miniature
     */
    void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity);

    /**
     * Called on every update tick for the miniature entity.
     * <p>
     * This method can be used to update animations, positions, particle effects,
     * or any logic that needs to run continuously while the miniature is active.
     *
     * @param context   the context containing information about the miniature cosmetic
     * @param nmsEntity the underlying NMS entity representing the miniature
     * @param tick      the current tick count
     */
    void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity, int tick);
}
