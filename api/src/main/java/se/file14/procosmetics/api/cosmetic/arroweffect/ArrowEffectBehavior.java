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
package se.file14.procosmetics.api.cosmetic.arroweffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for arrow effect cosmetics.
 *
 * @see CosmeticBehavior
 * @see ArrowEffectType
 */
public interface ArrowEffectBehavior extends CosmeticBehavior<ArrowEffectType> {

    /**
     * Called every tick when the arrow is in flight.
     *
     * @param context  the context containing information about the arrow effect
     * @param location the current location of the arrow
     */
    void onUpdate(CosmeticContext<ArrowEffectType> context, Location location);

    /**
     * Called when the arrow hits a target or surface.
     *
     * @param context  the context containing information about the arrow effect
     * @param location the location where the arrow hit
     */
    void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location);
}
