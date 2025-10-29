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
package se.file14.procosmetics.storage.connection.file;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.storage.connection.ConnectionProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SqliteConnectionProvider implements ConnectionProvider {

    private SQLiteDataSource dataSource;

    @Override
    public void initialize(ProCosmeticsPlugin plugin) {
        Config config = plugin.getConfigManager().getMainConfig();
        File file = new File(plugin.getDataFolder() + "/data", config.getString("storage.sqlite.database") + ".db");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create the database file.", e);
            }
        }
        try {
            SQLiteJDBCLoader.initialize();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize the SQLiteJDBCLoader.", e);
        }
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + file);

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to enable foreign keys in SQLite.", e);
        }
    }

    @Override
    public String getConfigKey() {
        return "sqlite";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() {
    }
}
