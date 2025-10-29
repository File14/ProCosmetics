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
package se.file14.procosmetics.api.cosmetic.balloon;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a balloon cosmetic instance associated with a user.
 * Balloons are floating entities that follow the player, connected
 * by a leash, providing a decorative companion effect.
 */
public interface Balloon extends Cosmetic<BalloonType, BalloonBehavior> {

    /**
     * Gets the entity tracker responsible for managing this balloon's visibility
     * and updates to nearby players.
     *
     * @return the entity tracker for this balloon
     */
    EntityTracker getTracker();

    /**
     * Gets the NMS entity representing the balloon itself.
     *
     * @return the NMS entity for the balloon
     */
    NMSEntity getNMSEntity();

    /**
     * Gets the NMS entity used as the leash anchor point for non-living-entities.
     *
     * @return the leash entity, or null if not used
     */
    @Nullable
    NMSEntity getLeashEntity();
}
