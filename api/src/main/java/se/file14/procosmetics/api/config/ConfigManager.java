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

import org.jetbrains.annotations.Nullable;

/**
 * Manages configuration files for the plugin.
 * This interface handles registration, loading, saving, and reloading of
 * configuration files. Configs are automatically created from plugin resources
 * if they don't exist in the plugin's data folder.
 */
public interface ConfigManager {

    /**
     * Registers a new config file from the plugin resources.
     * If the config is already registered, returns the existing instance.
     *
     * @param resourcePath the path to the config file
     * @return the registered Config instance
     */
    Config register(String resourcePath);

    /**
     * Gets a registered config by name.
     *
     * @param name the name/path of the config
     * @return the Config instance, or null if not found
     */
    @Nullable
    Config getConfig(String name);

    /**
     * Reloads all registered configs from disk.
     */
    void reloadAll();

    /**
     * Saves all registered configs to disk.
     */
    void saveAll();

    /**
     * Gets the main configuration file (config.yml).
     *
     * @return the main Config instance
     */
    Config getMainConfig();
}
