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
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.util.ReflectionUtil;
import se.file14.procosmetics.util.mapping.MappingRegistry;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CachedEntityType {

    private static final String PREFIX = "[CACHE ENTITY] ";

    private Object entityTypeObject;

    CachedEntityType(EntityType entityType) {
        Logger logger = ProCosmeticsPlugin.getPlugin().getLogger();

        if (entityType == null) {
            logger.log(Level.WARNING, PREFIX + " EntityType is null.");
            return;
        }

        try {
            Class<?> entityTypesClass = ReflectionUtil.getNMSClass("world.entity", MappingRegistry.getMappedFieldName(MappingRegistry.ENTITY_TYPES));

            if (entityTypesClass == null) {
                logger.log(Level.WARNING, PREFIX + " EntityTypesClass is null.");
                return;
            }
            String mappedMethodName = MappingRegistry.getMappedFieldName(MappingRegistry.ENTITY_TYPE_BY_STRING);

            if (mappedMethodName == null) {
                logger.log(Level.WARNING, PREFIX + " Mapped method name is null.");
                return;
            }
            Method method = ReflectionUtil.getMethod(entityTypesClass, mappedMethodName, String.class);

            if (method == null) {
                logger.log(Level.WARNING, PREFIX + " Method is null.");
                return;
            }
            Object result = method.invoke(null, entityType.getTranslationKey().toLowerCase().replace("entity.minecraft.", ""));

            if (result == null) {
                logger.log(Level.WARNING, PREFIX + " Method invocation returned null.");
                return;
            }
            Optional<?> optional = (Optional<?>) result;

            if (optional.isEmpty()) {
                logger.log(Level.WARNING, PREFIX + " Optional is empty.");
                return;
            }
            entityTypeObject = optional.get();
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.log(Level.SEVERE, "Failed to cache entity type " + entityType.name() + " via reflection.", e);
        }
    }

    @Nullable
    public Object getEntityTypeObject() {
        return entityTypeObject;
    }
}
