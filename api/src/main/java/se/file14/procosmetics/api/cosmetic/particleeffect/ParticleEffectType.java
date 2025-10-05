package se.file14.procosmetics.api.cosmetic.particleeffect;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of particle effect cosmetic.
 */
public interface ParticleEffectType extends CosmeticType<ParticleEffectType, ParticleEffectBehavior> {

    /**
     * Gets the delay between particle effect repetitions in ticks.
     *
     * @return the repeat delay in server ticks
     */
    int getRepeatDelay();

    /**
     * Builder interface for constructing particle effect type instances.
     */
    interface Builder extends CosmeticType.Builder<ParticleEffectType, ParticleEffectBehavior, Builder> {

        /**
         * Sets the delay between particle effect repetitions.
         *
         * @param repeatDelay the repeat delay in server ticks
         * @return this builder for method chaining
         */
        Builder repeatDelay(int repeatDelay);

        /**
         * Builds and returns the configured particle effect type instance.
         *
         * @return the built particle effect type
         */
        @Override
        ParticleEffectType build();
    }
}
