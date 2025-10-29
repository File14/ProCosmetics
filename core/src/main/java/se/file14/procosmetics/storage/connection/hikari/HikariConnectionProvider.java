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
package se.file14.procosmetics.storage.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.storage.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public abstract class HikariConnectionProvider implements ConnectionProvider {

    private ProCosmeticsPlugin plugin;
    private HikariDataSource hikari;

    @Override
    public void initialize(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;

        try {
            Config config = plugin.getConfigManager().getMainConfig();
            HikariConfig hikariConfig = createHikariConfig(config);

            this.hikari = new HikariDataSource(hikariConfig);

            // Test the connection
            try (Connection connection = hikari.getConnection()) {
                plugin.getLogger().log(Level.INFO, "Database connection pool initialized successfully.");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize database connection pool.", e);
        }
    }

    private HikariConfig createHikariConfig(Config config) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(plugin.getName() + "-hikari");
        hikariConfig.setDriverClassName(getDriverClassName());
        hikariConfig.setJdbcUrl(buildJdbcUrl(config));
        hikariConfig.setUsername(config.getString("storage.hikari.user"));
        hikariConfig.setPassword(config.getString("storage.hikari.password"));

        configurePoolSettings(hikariConfig, config);

        configureDataSourceProperties(hikariConfig, config);

        return hikariConfig;
    }

    private String buildJdbcUrl(Config config) {
        return String.format("jdbc:%s://%s:%s/%s",
                getDriverJdbcIdentifier(),
                config.getString("storage.hikari.host"),
                config.getInt("storage.hikari.port"),
                config.getString("storage.hikari.database")
        );
    }

    private void configurePoolSettings(HikariConfig hikariConfig, Config config) {
        hikariConfig.setMaximumPoolSize(config.getInt("storage.hikari.maximum_pool_size"));
        hikariConfig.setMinimumIdle(config.getInt("storage.hikari.minimum_idle"));
        hikariConfig.setMinimumIdle(config.getInt("storage.hikari.idle_timeout"));
        hikariConfig.setMaxLifetime(config.getInt("storage.hikari.maximum_lifetime"));
        hikariConfig.setKeepaliveTime(config.getInt("storage.hikari.keepalive_time"));
        hikariConfig.setConnectionTimeout(config.getInt("storage.hikari.connection_timeout"));
    }

    private void configureDataSourceProperties(HikariConfig hikariConfig, Config config) {
        Map<String, Object> properties = new HashMap<>();
        setDefaultProperties(properties);

        // Override with custom properties from config
        ConfigurationSection section = config.getConfigurationSection("storage.hikari.properties");
        if (section != null) {
            properties.putAll(section.getValues(false));
        }

        // Apply all properties to HikariConfig
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            hikariConfig.addDataSourceProperty(property.getKey(), property.getValue());
        }
    }

    protected void setDefaultProperties(Map<String, Object> properties) {
        // Rapid Recovery settings - https://github.com/brettwooldridge/HikariCP/wiki/Rapid-Recovery
        properties.put("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));
    }

    protected abstract String getDriverClassName();

    protected abstract String getDriverJdbcIdentifier();

    @Override
    public String getConfigKey() {
        return "hikari";
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (hikari == null || hikari.isClosed()) {
            throw new SQLException("Connection pool is not initialized or has been closed.");
        }
        return hikari.getConnection();
    }

    @Override
    public void shutdown() {
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
        }
    }

    public HikariDataSource getDataSource() {
        return hikari;
    }
}
