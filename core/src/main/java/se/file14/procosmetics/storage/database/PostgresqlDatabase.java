package se.file14.procosmetics.storage.database;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.storage.SQLDatabase;
import se.file14.procosmetics.storage.connection.hikari.PostgresConnectionProvider;

public class PostgresqlDatabase extends SQLDatabase {

    public PostgresqlDatabase(ProCosmeticsPlugin plugin) {
        super(plugin, new PostgresConnectionProvider());
    }

    @Override
    public String getType() {
        return "PostgreSQL";
    }

    @Override
    protected String getCreateUsersTable() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "id SERIAL PRIMARY KEY, " +
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
                "ON CONFLICT (player_id, category) DO UPDATE SET cosmetic = EXCLUDED.cosmetic;";
    }

    @Override
    protected String getAddGadgetAmmo() {
        return "INSERT INTO %s (player_id, gadget, amount) VALUES (?, ?, ?) " +
                "ON CONFLICT (player_id, gadget) DO UPDATE SET amount = %s.amount + EXCLUDED.amount;";
    }

    @Override
    protected String getSetGadgetAmmo() {
        return "INSERT INTO %s (player_id, gadget, amount) VALUES (?, ?, ?) " +
                "ON CONFLICT (player_id, gadget) DO UPDATE SET amount = EXCLUDED.amount;";
    }

    @Override
    protected String getAddTreasureChests() {
        return "INSERT INTO %s (player_id, treasure, amount) VALUES (?, ?, ?) " +
                "ON CONFLICT (player_id, treasure) DO UPDATE SET amount = %s.amount + EXCLUDED.amount;";
    }

    @Override
    protected String getSetTreasureChests() {
        return "INSERT INTO %s (player_id, treasure, amount) VALUES (?, ?, ?) " +
                "ON CONFLICT (player_id, treasure) DO UPDATE SET amount = EXCLUDED.amount;";
    }
}