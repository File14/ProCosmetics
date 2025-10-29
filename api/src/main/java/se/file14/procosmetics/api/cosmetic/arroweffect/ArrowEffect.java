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

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.Cosmetic;

/**
 * Represents an arrow effect cosmetic instance associated with a user.
 * Arrow effects provide visual enhancements to projectiles fired by players,
 * such as particle trails during flight and upon impact.
 */
public interface ArrowEffect extends Cosmetic<ArrowEffectType, ArrowEffectBehavior> {

    /**
     * Gets the current projectile entity associated with this arrow effect.
     * This represents the arrow or other projectile that is currently being tracked.
     *
     * @return the projectile entity, or null if no projectile is currently active
     */
    @Nullable
    Entity getProjectile();
}
