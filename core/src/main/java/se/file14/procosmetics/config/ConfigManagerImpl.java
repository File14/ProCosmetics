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

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class ConfigManagerImpl implements ConfigManager {

    private final ProCosmeticsPlugin plugin;
    private final Map<String, Config> configs = new HashMap<>();
    private Config mainConfig;

    public ConfigManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        configs.clear();
        mainConfig = register("config");
    }

    @Override
    public Config register(String resourcePath) {
        Config config = getConfig(resourcePath);

        if (config != null) {
            return config;
        }
        config = new BoostedYmlConfig(plugin, resourcePath);
        configs.put(resourcePath.toLowerCase(), config);
        return config;
    }

    @Override
    @Nullable
    public Config getConfig(String name) {
        return configs.get(name.toLowerCase());
    }

    @Override
    public void reloadAll() {
        configs.values().forEach(Config::reload);
    }

    @Override
    public void saveAll() {
        configs.values().forEach(Config::save);
    }

    @Override
    public Config getMainConfig() {
        return mainConfig;
    }
}
