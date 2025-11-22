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
package se.file14.procosmetics.api.util.structure.type;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.util.structure.Structure;

/**
 * A structure composed of block display entities attached to a parent entity.
 */
public interface ParentBlockDisplayStructure extends Structure<NMSEntity> {

    /**
     * Spawns the structure at the given location, attaching all block displays to a parent entity.
     *
     * @param location     the location to spawn the structure
     * @param parent       the entity to attach the block displays to as passengers
     * @param heightOffset the vertical offset to apply to the block displays relative to the parent
     * @return the angle used for rotation during spawning
     */
    double spawn(Location location, Entity parent, float heightOffset);
}
