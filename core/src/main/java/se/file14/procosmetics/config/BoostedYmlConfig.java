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
package se.file14.procosmetics.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class BoostedYmlConfig implements Config {

    private final ProCosmeticsPlugin plugin;
    private final String resourcePath;
    private final File file;
    private YamlDocument document;

    public BoostedYmlConfig(ProCosmeticsPlugin plugin, String resourcePath) {
        this.plugin = plugin;
        this.resourcePath = resourcePath;

        // Ensure .yml extension
        String fileName = resourcePath.endsWith(".yml") ? resourcePath : resourcePath + ".yml";
        this.file = new File(plugin.getDataFolder(), fileName);

        load();
    }

    private void load() {
        try {
            // Ensure parent directories exist
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // Get resource from plugin jar
            String resourceName = resourcePath.endsWith(".yml") ? resourcePath : resourcePath + ".yml";

            try (InputStream defaultResource = plugin.getResource(resourceName)) {
                if (defaultResource == null) {
                    plugin.getLogger().log(Level.SEVERE, "Default resource not found: " + resourceName);
                    return;
                }
                GeneralSettings settings = GeneralSettings.builder()
                        .setUseDefaults(true)
                        .setDefaultBoolean(false)
                        .setDefaultString("missing-string")
                        .setDefaultNumber(0)
                        .build();

                // Create document
                document = YamlDocument.create(
                        file,
                        defaultResource,
                        settings,
                        LoaderSettings.builder()
                                .setAutoUpdate(true)
                                .build(),
                        DumperSettings.DEFAULT,
                        UpdaterSettings.builder()
                                .setVersioning(new BasicVersioning("config_version"))
                                .build()
                );
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load configuration: " + resourcePath + ".", e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return document.contains(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return document.getBoolean(key);
    }

    @Override
    public String getString(String key) {
        return document.getString(key);
    }

    @Override
    public int getInt(String key) {
        return document.getInt(key);
    }

    @Override
    public double getDouble(String key) {
        return document.getDouble(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return document.getStringList(key);
    }

    @Override
    public List<?> getGenericList(String key) {
        return document.getList(key);
    }

    @Override
    public List<Map<?, ?>> getMapList(String key) {
        return document.getMapList(key);
    }

    @Override
    public Set<String> getSectionKeys(String key) {
        return document.getSection(key).getRoutesAsStrings(false);
    }

    @Override
    public Map<String, Object> getSectionValues(String key) {
        return document.getSection(key).getStringRouteMappedValues(false);
    }

    @Override
    public void set(String key, Object value) {
        document.set(key, value);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public int getVersion() {
        return document.getInt("config_version", 1);
    }

    @Override
    public void save() {
        try {
            if (document != null) {
                document.save();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save configuration: " + resourcePath, e);
        }
    }

    @Override
    public void reload() {
        try {
            if (document != null) {
                document.reload();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to reload configuration: " + resourcePath, e);
        }
    }

    public YamlDocument getDocument() {
        return document;
    }
}
