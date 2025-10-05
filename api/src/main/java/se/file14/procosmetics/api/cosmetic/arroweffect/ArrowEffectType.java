package se.file14.procosmetics.api.cosmetic.arroweffect;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of arrow effect cosmetic.
 */
public interface ArrowEffectType extends CosmeticType<ArrowEffectType, ArrowEffectBehavior> {

    /**
     * Builder interface for constructing arrow effect type instances.
     */
    interface Builder extends CosmeticType.Builder<ArrowEffectType, ArrowEffectBehavior, Builder> {

        /**
         * Builds and returns the configured arrow effect type instance.
         *
         * @return the built arrow effect type
         */
        @Override
        ArrowEffectType build();
    }
}