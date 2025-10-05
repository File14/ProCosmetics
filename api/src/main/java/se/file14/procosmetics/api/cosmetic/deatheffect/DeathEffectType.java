package se.file14.procosmetics.api.cosmetic.deatheffect;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

public interface DeathEffectType extends CosmeticType<DeathEffectType, DeathEffectBehavior> {

    /**
     * Builder interface for constructing death effect type instances.
     */
    interface Builder extends CosmeticType.Builder<DeathEffectType, DeathEffectBehavior, Builder> {

        /**
         * Builds and returns the configured death effect type instance.
         *
         * @return the built death effect type
         */
        @Override
        DeathEffectType build();
    }
}
