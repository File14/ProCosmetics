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
package se.file14.procosmetics.storage.database;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.storage.SQLDatabase;
import se.file14.procosmetics.storage.connection.hikari.MySqlConnectionProvider;

public class MysqlDatabase extends SQLDatabase {

    public MysqlDatabase(ProCosmeticsPlugin plugin) {
        super(plugin, new MySqlConnectionProvider());
    }

    @Override
    public String getType() {
        return "MySQL";
    }

    @Override
    protected String getCreateUsersTable() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(36) NOT NULL UNIQUE, " +
                "name VARCHAR(32) NOT NULL, " +
                "coins INT NOT NULL DEFAULT 0, " +
                "self_view_morph BOOLEAN NOT NULL DEFAULT FALSE, " +
                "self_view_status BOOLEAN NOT NULL DEFAULT FALSE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";
    }

    @Override
    protected String getCreateCosmeticsTable() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "player_id INT NOT NULL, " +
                "category VARCHAR(32) NOT NULL, " +
                "cosmetic VARCHAR(32) NOT NULL, " +
                "PRIMARY KEY (player_id, category), " +
                "FOREIGN KEY (player_id) REFERENCES %s(id) ON DELETE CASCADE " +
                ");";
    }

    @Override
    protected String getCreateGadgetAmmoTable() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "player_id INT NOT NULL, " +
                "gadget VARCHAR(32) NOT NULL, " +
                "amount INT NOT NULL DEFAULT 0, " +
                "PRIMARY KEY (player_id, gadget), " +
                "FOREIGN KEY (player_id) REFERENCES %s(id) ON DELETE CASCADE, " +
                "CHECK (amount >= 0)" +
                ");";
    }

    @Override
    protected String getCreateTreasureChestsTable() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "player_id INT NOT NULL, " +
                "treasure_chest VARCHAR(32) NOT NULL, " +
                "amount INT NOT NULL DEFAULT 0, " +
                "PRIMARY KEY (player_id, treasure), " +
                "FOREIGN KEY (player_id) REFERENCES %s(id) ON DELETE CASCADE, " +
                "CHECK (amount >= 0)" +
                ");";
    }

    @Override
    protected String getEquipCosmeticQuery() {
        return "INSERT INTO %s (player_id, category, cosmetic) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cosmetic = VALUES(cosmetic);";
    }

    @Override
    protected String getAddGadgetAmmo() {
        return "INSERT INTO %s (player_id, gadget, amount) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = amount + VALUES(amount);";
    }

    @Override
    protected String getSetGadgetAmmo() {
        return "INSERT INTO %s (player_id, gadget, amount) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = VALUES(amount);";
    }

    @Override
    protected String getAddTreasureChests() {
        return "INSERT INTO %s (player_id, treasure_chest, amount) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = amount + VALUES(amount);";
    }

    @Override
    protected String getSetTreasureChests() {
        return "INSERT INTO %s (player_id, treasure_chest, amount) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = VALUES(amount);";
    }
}
