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
package se.file14.procosmetics.util;

import org.bukkit.Bukkit;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.util.mapping.Mapping;
import se.file14.procosmetics.util.mapping.MappingType;
import se.file14.procosmetics.util.version.VersionUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final String serverVersion;
    public static final String VERSION_CLASS_PATH;

    static {
        if (Mapping.MAPPING_TYPE == MappingType.SPIGOT) {
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        } else {
            serverVersion = null;
        }
        VERSION_CLASS_PATH = ProCosmeticsPlugin.class.getPackage().getName() + "." + VersionUtil.VERSION.toString() + ".";
    }

    public static Object getHandle(@Nonnull Class<?> clazz, @Nonnull Object object) {
        try {
            return clazz.getMethod("getHandle").invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Object getHandle(@Nonnull Object object) {
        return getHandle(object.getClass(), object);
    }

    public static Class<?> getClass(@Nonnull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNMSClass(@Nonnull String className) {
        return getNMSClass(null, className);
    }

    public static Class<?> getNMSClass(@Nullable String preClassPath, @Nonnull String className) {
        String classPath = "net.minecraft.";

        if (preClassPath != null) {
            classPath += preClassPath + ".";
        }
        classPath += className;

        return getClass(classPath);
    }

    public static Class<?> getBukkitClass(@Nonnull String classPath) {
        String fullClassPath = "org.bukkit.craftbukkit.";

        if (serverVersion != null) {
            fullClassPath += serverVersion + "." + classPath;
        } else {
            fullClassPath += classPath;
        }
        return getClass(fullClassPath);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        if (name == null) {
            return null;
        }
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && (args.length == 0 || classList(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... args) {
        if (name == null) {
            return null;
        }
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name) && (args.length == 0 || classList(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static Field getDeclaredField(Class<?> clazz, String name) {
        if (name == null) {
            return null;
        }
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getDeclaredField(Class<?> clazz, String name, Class<?> type) {
        if (name == null) {
            return null;
        }
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(name) && field.getType().equals(type)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String name) {
        if (name == null) {
            return null;
        }
        try {
            Field field = clazz.getField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        if (clazz == null) {
            return null;
        }
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (matchesParameters(constructor.getParameterTypes(), parameterTypes)) {
                return constructor;
            }
        }
        return null;
    }

    private static boolean matchesParameters(Class<?>[] methodParams, Class<?>[] providedParams) {
        if (providedParams.length == 0) {
            return methodParams.length == 0;
        }
        if (methodParams.length != providedParams.length) {
            return false;
        }
        for (int i = 0; i < methodParams.length; i++) {
            if (!methodParams[i].equals(providedParams[i]) && !methodParams[i].isAssignableFrom(providedParams[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean classList(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;

        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
}
