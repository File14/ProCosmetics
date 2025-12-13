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
package se.file14.procosmetics.api.config;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a configuration file with typed accessors.
 */
public interface Config {

    /**
     * Checks if a configuration key exists.
     *
     * @param key The configuration key to check
     * @return true if the key exists, false otherwise
     */
    boolean hasKey(String key);

    /**
     * Gets a boolean value from the configuration.
     *
     * @param key The configuration key
     * @return The boolean value from config or false if not found
     */
    boolean getBoolean(String key);

    /**
     * Gets a string value from the configuration.
     *
     * @param key The configuration key
     * @return The processed string value from config or "missing-string" if not found
     */
    String getString(String key);

    /**
     * Gets an integer value from the configuration.
     *
     * @param key The configuration key
     * @return The integer value from config or 1 if not found
     */
    int getInt(String key);

    /**
     * Gets a double value from the configuration.
     *
     * @param key The configuration key
     * @return The double value from config or 1.0 if not found
     */
    double getDouble(String key);

    /**
     * Gets a list of strings from the configuration.
     *
     * @param key The configuration key
     * @return The string list from config or empty list if not found
     */
    List<String> getStringList(String key);

    /**
     * Gets a generic list from the configuration.
     *
     * @param key The configuration key
     * @return The generic list from config or default config if not found
     */
    List<?> getGenericList(String key);

    /**
     * Gets a list of maps from the configuration.
     *
     * @param key The configuration key
     * @return The list of maps from config or empty list if not found
     */
    List<Map<?, ?>> getMapList(String key);

    /**
     * Gets the keys of a configuration section.
     *
     * @param key The configuration key
     * @return The configuration section keys from config or empty set if not found
     */
    Set<String> getSectionKeys(String key);

    /**
     * Gets the values of a configuration section as a map.
     *
     * @param key The configuration key
     * @return The configuration section values from config or empty map if not found
     */
    Map<String, Object> getSectionValues(String key);

    /**
     * Sets a configuration value.
     *
     * @param key   The configuration key
     * @param value The value to set
     */
    void set(String key, Object value);

    /**
     * Removes a configuration value.
     *
     * @param key the configuration key to remove
     * @return true if the key existed and was removed, false otherwise
     */
    boolean remove(String key);

    /**
     * Gets the configuration file.
     *
     * @return The file of the config instance
     */
    File getFile();

    /**
     * Gets the configuration version.
     *
     * @return The version number
     */
    int getVersion();

    /**
     * Saves the configuration to file.
     */
    void save();

    /**
     * Reloads the configuration from the file.
     */
    void reload();
}
