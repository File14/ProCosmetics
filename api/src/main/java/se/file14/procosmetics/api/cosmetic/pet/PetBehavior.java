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
package se.file14.procosmetics.api.cosmetic.pet;

import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

/**
 * Defines the behavior for pet cosmetics.
 *
 * @see CosmeticBehavior
 * @see PetType
 */
public interface PetBehavior extends CosmeticBehavior<PetType> {

    /**
     * Called when the pet entity is initialized.
     * <p>
     * This method is used to configure entity properties.
     *
     * @param context the context containing information about the pet cosmetic
     * @param entity  the Bukkit entity representing the pet
     */
    void onSetupEntity(CosmeticContext<PetType> context, Entity entity);

    /**
     * Called every tick to update the pet entity.
     *
     * @param context the context containing information about the pet cosmetic
     * @param entity  the Bukkit entity representing the pet
     */
    void onUpdate(CosmeticContext<PetType> context, Entity entity);
}
