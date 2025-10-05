package se.file14.procosmetics.api.config;

import javax.annotation.Nullable;

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
