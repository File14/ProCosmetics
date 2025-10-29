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
package se.file14.procosmetics.nms.entitytype;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityTypeRegistry {

    private static final Map<EntityType, CachedEntityType> ENTITY_TYPE_CACHE = new HashMap<>();

    public static CachedEntityType getCachedEntityType(EntityType entityType) {
        return ENTITY_TYPE_CACHE.computeIfAbsent(entityType, CachedEntityType::new);
    }

    public static Object getEntityTypeObject(EntityType entityType) {
        CachedEntityType cached = getCachedEntityType(entityType);
        return cached.getEntityTypeObject();
    }

    public static boolean isCached(EntityType entityType) {
        return ENTITY_TYPE_CACHE.containsKey(entityType);
    }

    public static void clearCache() {
        ENTITY_TYPE_CACHE.clear();
    }
}
