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
package se.file14.procosmetics.api.cosmetic.mount;

import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Defines the behavior for mount cosmetics.
 *
 * @see CosmeticBehavior
 * @see MountType
 */
public interface MountBehavior extends CosmeticBehavior<MountType> {

    /**
     * Sets up the mount entity when it is initialized.
     *
     * @param context   the context containing information about the mount cosmetic
     * @param entity    the Bukkit entity representing the mount
     * @param nmsEntity the underlying NMS entity representing the mount
     */
    void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity);

    /**
     * Called every tick to update the mount entity.
     *
     * @param context   the context containing information about the mount cosmetic
     * @param entity    the Bukkit entity representing the mount
     * @param nmsEntity the underlying NMS entity for low-level interactions
     */
    void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity);

    /**
     * Determines whether the mount should allow tossing items while active.
     * <p>
     * By default, this checks the mount category’s configuration value.
     *
     * @param context the context containing information about the mount cosmetic
     * @return {@code true} if tossing items is enabled, otherwise {@code false}
     */
    default boolean isTossItemsEnabled(CosmeticContext<MountType> context) {
        return context.getType().getCategory().getConfig().getBoolean("tossing_items");
    }
}
