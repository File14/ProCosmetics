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
package se.file14.procosmetics.storage;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.storage.Database;
import se.file14.procosmetics.storage.database.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DatabaseTypeProvider {

    private static final Map<String, Function<ProCosmeticsPlugin, Database>> DATABASE_PROVIDERS = new HashMap<>();

    static {
        register("mysql", MysqlDatabase::new);
        register("mariadb", MariaDbDatabase::new);
        register("postgresql", PostgresqlDatabase::new);
        register("sqlite", SqliteDatabase::new);
        register("mongodb", MongoDbDatabase::new);
    }

    public static Database createDatabase(ProCosmeticsPlugin plugin, String databaseType) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (databaseType == null || databaseType.trim().isEmpty()) {
            throw new IllegalArgumentException("Database type cannot be null or empty");
        }
        String normalizedType = databaseType.toLowerCase().trim();
        Function<ProCosmeticsPlugin, Database> provider = DATABASE_PROVIDERS.get(normalizedType);

        if (provider == null) {
            throw new IllegalArgumentException(String.format("Unsupported database type: %s. Supported types: %s", databaseType, getSupportedTypes()));
        }
        return provider.apply(plugin);
    }

    public static void register(String databaseType, Function<ProCosmeticsPlugin, Database> databaseProvider) {
        if (databaseType == null || databaseType.trim().isEmpty()) {
            throw new IllegalArgumentException("Database type cannot be null or empty");
        }
        if (databaseProvider == null) {
            throw new IllegalArgumentException("Database provider cannot be null");
        }
        DATABASE_PROVIDERS.put(databaseType.toLowerCase().trim(), databaseProvider);
    }

    public static boolean unregister(String databaseType) {
        if (databaseType == null || databaseType.trim().isEmpty()) {
            return false;
        }
        return DATABASE_PROVIDERS.remove(databaseType.toLowerCase().trim()) != null;
    }

    public static Set<String> getSupportedTypes() {
        return DATABASE_PROVIDERS.keySet();
    }
}
