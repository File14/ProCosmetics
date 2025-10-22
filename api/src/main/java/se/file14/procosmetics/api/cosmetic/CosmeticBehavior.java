package se.file14.procosmetics.api.cosmetic;

/**
 * Represents the behavior logic for a cosmetic type.
 *
 * @param <T> the cosmetic type associated with this behavior
 * @see CosmeticType
 * @see CosmeticContext
 */
public interface CosmeticBehavior<T extends CosmeticType<T, ?>> {

    /**
     * Called when the cosmetic is equipped by a user.
     *
     * @param context the context containing information about the cosmetic and user
     */
    void onEquip(CosmeticContext<T> context);

    /**
     * Called when the cosmetic is unequipped by a user.
     *
     * @param context the context containing information about the cosmetic and user
     */
    void onUnequip(CosmeticContext<T> context);
}
