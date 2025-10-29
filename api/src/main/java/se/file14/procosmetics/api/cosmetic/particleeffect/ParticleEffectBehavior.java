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

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for particle effect cosmetics.
 *
 * @see CosmeticBehavior
 * @see ParticleEffectType
 */
public interface ParticleEffectBehavior extends CosmeticBehavior<ParticleEffectType> {

    /**
     * Called to update and display the particle effect.
     * <p>
     * This method is responsible for spawning or managing particle visuals at the given location.
     *
     * @param context  the context containing information about the particle effect cosmetic
     * @param location the location where the particle effect should be displayed
     */
    void onUpdate(CosmeticContext<ParticleEffectType> context, Location location);
}
