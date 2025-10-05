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
        register("treasure_chests");
        register("rarities");
        register("data/treasure_chest_platforms");
    }

    @Override
    public Config register(String resourcePath) {
        Config config = getConfig(resourcePath);

        if (config != null) {
            return config;
        }
        config = new YmlConfig(plugin, resourcePath);
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
