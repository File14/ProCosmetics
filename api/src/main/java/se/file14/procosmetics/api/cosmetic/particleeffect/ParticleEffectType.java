/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
