package se.file14.procosmetics.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class YmlConfig implements Config {

    protected final ProCosmeticsPlugin plugin;
    protected final File file;
    protected int version;
    protected YamlConfiguration configuration;

    public YmlConfig(ProCosmeticsPlugin plugin, String resourcePath) {
        this.plugin = plugin;

        if (!resourcePath.endsWith(".yml")) {
            resourcePath += ".yml";
        }
        this.file = plugin.getDataFolder().toPath()
                .resolve(resourcePath)
                .toFile();

        ensureFileExists(resourcePath);
        load();
    }

    private void ensureFileExists(String resourcePath) {
        if (file.exists()) {
            return;
        }
        file.getParentFile().mkdirs();
        plugin.saveResource(resourcePath, false);
    }

    private void load() {
        configuration = YamlConfiguration.loadConfiguration(file);
        version = getInt("config-version", false);
    }

    @Override
    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save the " + file.getName() + " config.", e);
        }
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public boolean hasKey(String key) {
        return configuration.contains(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return getGeneric(key, false, true);
    }

    @Override
    public String getString(String key) {
        return getString(key, true);
    }

    @Override
    public String getString(String key, boolean logMissing) {
        return getGeneric(key, "missing-string", logMissing);
    }

    @Override
    public int getInt(String key) {
        return getInt(key, true);
    }

    @Override
    public int getInt(String key, boolean logMissing) {
        return getGeneric(key, 1, logMissing);
    }

    @Override
    public double getDouble(String key) {
        return getDouble(key, true);
    }

    @Override
    public double getDouble(String key, boolean logMissing) {
        return getGeneric(key, 1.0d, logMissing);
    }

    @Override
    public List<String> getStringList(String key) {
        return getStringList(key, true);
    }

    @Override
    public List<String> getStringList(String key, boolean logMissing) {
        return getGeneric(key, Collections.emptyList(), logMissing);
    }

    @Override
    public <T> List<T> getGenericList(String key) {
        return getGeneric(key, Collections.emptyList(), true);
    }

    @Override
    public List<Map<?, ?>> getMapList(String key) {
        return getMapList(key, false);
    }

    @Override
    public List<Map<?, ?>> getMapList(String key, boolean logMissing) {
        return getGeneric(key, Collections.emptyList(), logMissing);
    }

    @Override
    public <T> T getGeneric(String key, T defaultValue, boolean logMissing) {
        if (!hasKey(key)) {
            if (logMissing) {
                LogUtil.logMissingKey(defaultValue.getClass().getSimpleName().toLowerCase(), key, file);
            }
            return defaultValue;
        }
        return getGenericData(key);
    }

    @SuppressWarnings("unchecked")
    private <T> T getGenericData(String key) {
        return (T) configuration.get(key);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String key) {
        return configuration.getConfigurationSection(key);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public int getVersion() {
        return version;
    }
}
