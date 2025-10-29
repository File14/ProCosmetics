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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Represents a configuration file with typed accessors.
 * This interface provides convenient methods for retrieving configuration values
 * with default fallbacks.
 *
 * <p>All getter methods return safe defaults if keys are not found, preventing
 * null pointer exceptions while optionally logging missing keys for debugging.</p>
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
     * @return The boolean value, or false if not found
     */
    boolean getBoolean(String key);

    /**
     * Gets a string value from the configuration.
     *
     * @param key The configuration key
     * @return The processed string value, or "missing-string" if not found
     */
    String getString(String key);

    /**
     * Gets a string value from the configuration.
     *
     * @param key        The configuration key
     * @param logMissing Whether to log missing keys
     * @return The processed string value, or "missing-string" if not found
     */
    String getString(String key, boolean logMissing);

    /**
     * Gets an integer value from the configuration.
     *
     * @param key The configuration key
     * @return The integer value, or 1 if not found
     */
    int getInt(String key);

    /**
     * Gets an integer value from the configuration.
     *
     * @param key        The configuration key
     * @param logMissing Whether to log missing keys
     * @return The integer value, or 1 if not found
     */
    int getInt(String key, boolean logMissing);

    /**
     * Gets a double value from the configuration.
     *
     * @param key The configuration key
     * @return The double value, or 1.0 if not found
     */
    double getDouble(String key);

    /**
     * Gets a double value from the configuration
     *
     * @param key        The configuration key
     * @param logMissing Whether to log missing keys
     * @return The double value, or 1.0 if not found
     */
    double getDouble(String key, boolean logMissing);

    /**
     * Gets a list of strings from the configuration.
     *
     * @param key The configuration key
     * @return The string list, or empty list if not found
     */
    List<String> getStringList(String key);

    /**
     * Gets a list of strings from the configuration.
     *
     * @param key        The configuration key
     * @param logMissing Whether to log missing keys
     * @return The string list, or empty list if not found
     */
    List<String> getStringList(String key, boolean logMissing);

    /**
     * Gets a generic list from the configuration.
     *
     * @param key The configuration key
     * @param <T> The type of the list elements
     * @return The generic list, or empty list if not found
     */
    <T> List<T> getGenericList(String key);

    /**
     * Gets a generic value from the configuration.
     *
     * @param key          The configuration key
     * @param defaultValue The default value to return if key is not found
     * @param logMissing   Whether to log missing keys
     * @param <T>          The type of the value
     * @return The value, or defaultValue if not found
     */
    <T> T getGeneric(String key, T defaultValue, boolean logMissing);

    /**
     * Gets a list of maps from the configuration.
     *
     * @param key The configuration key
     * @return The list of maps, or empty list if not found
     */
    List<Map<?, ?>> getMapList(String key);

    /**
     * Gets a list of maps from the configuration.
     *
     * @param key        The configuration key
     * @param logMissing Whether to log missing keys
     * @return The list of maps, or empty list if not found
     */
    List<Map<?, ?>> getMapList(String key, boolean logMissing);

    /**
     * Gets a configuration section.
     *
     * @param key The configuration key
     * @return The configuration section, or null if not found
     */
    ConfigurationSection getConfigurationSection(String key);

    /**
     * Gets the underlying YamlConfiguration.
     *
     * @return The YamlConfiguration instance
     */
    YamlConfiguration getConfiguration();

    /**
     * Gets the file.
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
