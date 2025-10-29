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
package se.file14.procosmetics.api.cosmetic.status;

import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a status cosmetic instance associated with a user.
 * Status cosmetics display dynamic text above the player,
 * such as custom titles, statistics, or other information that can update in real-time.
 */
public interface Status extends Cosmetic<StatusType, StatusBehavior> {

    /**
     * Gets the NMS entity used to display the status text.
     *
     * @return the NMS entity for this status display
     */
    NMSEntity getNMSEntity();
}
