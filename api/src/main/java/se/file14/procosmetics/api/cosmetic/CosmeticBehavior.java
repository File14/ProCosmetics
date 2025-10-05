package se.file14.procosmetics.api.cosmetic;

public interface CosmeticBehavior<T extends CosmeticType<T, ?>> {

    void onEquip(CosmeticContext<T> context);

    void onUnequip(CosmeticContext<T> context);
}
