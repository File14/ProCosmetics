package se.file14.procosmetics.api.cosmetic.pet;

import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for pet cosmetics.
 *
 * @see CosmeticBehavior
 * @see PetType
 */
public interface PetBehavior extends CosmeticBehavior<PetType> {

    /**
     * Called when the pet entity is initialized.
     * <p>
     * This method is used to configure entity properties.
     *
     * @param context the context containing information about the pet cosmetic
     * @param entity  the Bukkit entity representing the pet
     */
    void onSetupEntity(CosmeticContext<PetType> context, Entity entity);

    /**
     * Called every tick to update the pet entity.
     *
     * @param context the context containing information about the pet cosmetic
     * @param entity  the Bukkit entity representing the pet
     */
    void onUpdate(CosmeticContext<PetType> context, Entity entity);
}
