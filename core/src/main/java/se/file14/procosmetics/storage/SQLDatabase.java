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

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;
import se.file14.procosmetics.storage.connection.ConnectionProvider;
import se.file14.procosmetics.user.UserImpl;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public abstract class SQLDatabase extends DatabaseImpl {

    // Standard SQL queries
    private static final String SQL_LOAD_USER_BY_UUID = "SELECT * FROM %s WHERE uuid = ?;";
    private static final String SQL_LOAD_USER_BY_NAME = "SELECT * FROM %s WHERE name = ? ORDER BY last_seen DESC LIMIT 1;";
    private static final String SQL_LOAD_USER_BY_ID = "SELECT * FROM %s WHERE id = ?;";
    private static final String SQL_INSERT_USER = "INSERT INTO %s (uuid, name) VALUES (?, ?);";
    private static final String SQL_UPDATE_USER_NAME = "UPDATE %s SET name = ? WHERE id = ?;";
    private static final String SQL_UPDATE_LAST_SEEN = "UPDATE %s SET last_seen = ? WHERE id = ?;";

    private static final String SQL_SET_SELF_VIEW_MORPH = "UPDATE %s SET self_view_morph = ? WHERE id = ?;";
    private static final String SQL_SET_SELF_VIEW_STATUS = "UPDATE %s SET self_view_status = ? WHERE id = ?;";

    private static final String SQL_ADD_COINS = "UPDATE %s SET coins = coins + ? WHERE id = ?;";
    private static final String SQL_REMOVE_COINS = "UPDATE %s SET coins = coins - ? WHERE id = ? AND coins >= ?;";
    private static final String SQL_SET_COINS = "UPDATE %s SET coins = ? WHERE id = ? AND ? >= 0;";
    private static final String SQL_GET_COINS = "SELECT coins FROM %s WHERE id = ?;";

    private static final String SQL_LOAD_USER_COSMETICS = "SELECT category, cosmetic FROM %s WHERE player_id = ?;";
    private static final String SQL_UNEQUIP_COSMETIC = "DELETE FROM %s WHERE player_id = ? AND category = ?;";

    private static final String SQL_LOAD_USER_GADGET_AMMO = "SELECT gadget, amount FROM %s WHERE player_id = ?;";
    private static final String SQL_REMOVE_GADGET_AMMO = "UPDATE %s SET amount = amount - ? WHERE player_id = ? AND gadget = ? AND amount >= ?;";
    private static final String SQL_GET_GADGET_AMMO = "SELECT amount FROM %s WHERE player_id = ? AND gadget = ?;";

    private static final String SQL_LOAD_USER_TREASURE_CHESTS = "SELECT treasure_chest, amount FROM %s WHERE player_id = ?;";
    private static final String SQL_REMOVE_TREASURE_CHESTS = "UPDATE %s SET amount = amount - ? WHERE player_id = ? AND treasure_chest = ? AND amount >= ?;";
    private static final String SQL_GET_TREASURE_CHEST = "SELECT amount FROM %s WHERE player_id = ? AND treasure_chest = ?;";

    protected final ConnectionProvider connectionProvider;
    private final String usersTable;
    private final String cosmeticsTable;
    private final String gadgetAmmoTable;
    private final String treasureChestsTable;

    public SQLDatabase(ProCosmeticsPlugin plugin, ConnectionProvider connectionProvider) {
        super(plugin);
        this.connectionProvider = connectionProvider;
        connectionProvider.initialize(plugin);

        Config config = plugin.getConfigManager().getMainConfig();
        String tablePrefix = config.getString("storage." + connectionProvider.getConfigKey() + ".table_prefix", true);

        usersTable = tablePrefix + "_users";
        cosmeticsTable = tablePrefix + "_cosmetics";
        gadgetAmmoTable = tablePrefix + "_gadget_ammo";
        treasureChestsTable = tablePrefix + "_treasure_chests";

        initializeTables();
    }

    // Abstract methods for database-specific queries
    // This is needed to handle differences in SQL syntax between databases.
    protected abstract String getCreateUsersTable();

    protected abstract String getCreateCosmeticsTable();

    protected abstract String getCreateGadgetAmmoTable();

    protected abstract String getCreateTreasureChestsTable();

    protected abstract String getEquipCosmeticQuery();

    protected abstract String getAddGadgetAmmo();

    protected abstract String getSetGadgetAmmo();

    protected abstract String getAddTreasureChests();

    protected abstract String getSetTreasureChests();

    protected void initializeTables() {
        try (Connection connection = connectionProvider.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.addBatch(String.format(getCreateUsersTable(), usersTable));
                statement.addBatch(String.format(getCreateCosmeticsTable(), cosmeticsTable, usersTable));
                statement.addBatch(String.format(getCreateGadgetAmmoTable(), gadgetAmmoTable, usersTable));
                statement.addBatch(String.format(getCreateTreasureChestsTable(), treasureChestsTable, usersTable));
                statement.executeBatch();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create SQL tables.", e);
        }
    }

    @Override
    public UserImpl loadUser(UUID uuid) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(SQL_LOAD_USER_BY_UUID, usersTable))) {
            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return loadUserData(resultSet);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load user with UUID: " + uuid + ".", e);
        }
        return null;
    }

    @Override
    public UserImpl loadUser(String name) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(SQL_LOAD_USER_BY_NAME, usersTable))) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return loadUserData(resultSet);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load user with name: " + name + ".", e);
        }
        return null;
    }

    @Override
    public UserImpl loadUser(int id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(SQL_LOAD_USER_BY_ID, usersTable))) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return loadUserData(resultSet);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load user with id: " + id, e);
        }
        return null;
    }

    private UserImpl loadUserData(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        UUID uuid = UUID.fromString(resultSet.getString("uuid"));
        String name = resultSet.getString("name");
        int coins = resultSet.getInt("coins");
        boolean selfViewMorph = resultSet.getBoolean("self_view_morph");
        boolean selfViewStatus = resultSet.getBoolean("self_view_status");

        UserImpl user = new UserImpl(plugin, id, uuid, name);
        user.setCoins(coins);
        user.setSelfViewMorph(selfViewMorph, false);
        user.setSelfViewStatus(selfViewStatus, false);

        // Load cosmetics if restore is enabled
        if (plugin.getConfigManager().getMainConfig().getBoolean("settings.restore_cosmetics")) {
            loadUserCosmetics(user);
        }
        loadUserGadgetAmmo(user);
        loadUserTreasureChests(user);

        return user;
    }

    private void loadUserCosmetics(UserImpl user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(SQL_LOAD_USER_COSMETICS, cosmeticsTable))) {
            statement.setInt(1, user.getDatabaseId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String categoryKey = resultSet.getString("category");
                    String cosmeticKey = resultSet.getString("cosmetic");

                    CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategoryRaw(categoryKey);

                    // Ignore if not found or if not enabled
                    if (category == null || !category.isEnabled()) {
                        continue;
                    }
                    CosmeticType<?, ?> type = category.getCosmeticRegistry().getType(cosmeticKey);

                    // Ignore if not found
                    if (type == null) {
                        continue;
                    }
                    // TODO: Remove cast when more stuff is exposed to API
                    user.setCosmetic(category, ((CosmeticTypeImpl<?, ?>) type).create(plugin, user));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load cosmetics for player: " + user + ".", e);
        }
    }

    private void loadUserGadgetAmmo(UserImpl user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(SQL_LOAD_USER_GADGET_AMMO, gadgetAmmoTable))) {
            statement.setInt(1, user.getDatabaseId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String key = resultSet.getString("gadget");
                    int amount = resultSet.getInt("amount");

                    GadgetType type = plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getType(key);

                    // Ignore if not found
                    if (type == null) {
                        continue;
                    }
                    user.setAmmo(type, amount);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load gadget ammo for player: " + user + ".", e);
        }
    }

    private void loadUserTreasureChests(UserImpl user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(SQL_LOAD_USER_TREASURE_CHESTS, treasureChestsTable))) {
            statement.setInt(1, user.getDatabaseId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(resultSet.getString("treasure_chest"));
                    int amount = resultSet.getInt("amount");

                    if (treasureChest != null) {
                        user.setTreasureChests(treasureChest, amount);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load treasure chests for player: " + user + ".", e);
        }
    }

    @Override
    public UserImpl insertUser(UUID uuid, String name) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     String.format(SQL_INSERT_USER, usersTable),
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new UserImpl(plugin, id, uuid, name);
                } else {
                    plugin.getLogger().log(Level.WARNING, "No ID obtained for player " + name);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to insert player " + name, e);
        }
        return null;
    }

    @Override
    public void updateName(User user, String name) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement stmt = connection.prepareStatement(String.format(SQL_UPDATE_USER_NAME, usersTable))) {
            stmt.setString(1, name);
            stmt.setInt(2, user.getDatabaseId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to update name for " + user, e);
        }
    }

    @Override
    public void updateLastSeen(User user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement stmt = connection.prepareStatement(String.format(SQL_UPDATE_LAST_SEEN, usersTable))) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, user.getDatabaseId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to update last seen for " + user, e);
        }
    }

    @Override
    public CompletableFuture<Boolean> saveEquippedCosmeticAsync(User user, CosmeticType<?, ?> cosmeticType) {
        // Skip the music category
        if (cosmeticType.getCategory() == plugin.getCategoryRegistries().music()) {
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement(String.format(getEquipCosmeticQuery(), cosmeticsTable))) {
                statement.setInt(1, user.getDatabaseId());
                statement.setString(2, cosmeticType.getCategory().getKey());
                statement.setString(3, cosmeticType.getKey());

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to equip cosmetic for player " + user + ".", e);
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeEquippedCosmeticAsync(User user, CosmeticCategory<?, ?, ?> category) {
        // Skip the music category
        if (category == plugin.getCategoryRegistries().music()) {
            return CompletableFuture.completedFuture(true);
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement(String.format(SQL_UNEQUIP_COSMETIC, cosmeticsTable))) {
                statement.setInt(1, user.getDatabaseId());
                statement.setString(2, category.getKey());
                statement.executeUpdate();

                return true;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to unequip cosmetic for player " + user + ".", e);
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> setSelfViewMorphAsync(User user, boolean enabled) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement(String.format(SQL_SET_SELF_VIEW_MORPH, usersTable))) {
                statement.setBoolean(1, enabled);
                statement.setInt(2, user.getDatabaseId());
                int rowsAffected = statement.executeUpdate();

                return rowsAffected > 0;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set self-view morph for player " + user + ".", e);
            }
            return false;
        }).thenApplyAsync(result -> {
            if (result) {
                user.setSelfViewMorph(enabled, false);
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> setSelfViewStatusAsync(User user, boolean enabled) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection();
                 PreparedStatement statement = connection.prepareStatement(String.format(SQL_SET_SELF_VIEW_STATUS, usersTable))) {
                statement.setBoolean(1, enabled);
                statement.setInt(2, user.getDatabaseId());
                int rowsAffected = statement.executeUpdate();

                return rowsAffected > 0;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set self-view status for player " + user + ".", e);
            }
            return false;
        }).thenApplyAsync(result -> {
            if (result) {
                user.setSelfViewStatus(enabled, false);
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> addCoinsAsyncImpl(User user, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(SQL_ADD_COINS, usersTable))) {
                    statement.setInt(1, amount);
                    statement.setInt(2, user.getDatabaseId());

                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_COINS, usersTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("coins");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to add coins for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getCoins());
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setCoins(result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeCoinsAsyncImpl(User user, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection()) {
                // Remove coins
                try (PreparedStatement statement = connection.prepareStatement(String.format(SQL_REMOVE_COINS, usersTable))) {
                    statement.setInt(1, amount);
                    statement.setInt(2, user.getDatabaseId());
                    statement.setInt(3, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_COINS, usersTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("coins");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to remove coins for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getCoins());
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setCoins(result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> setCoinsAsyncImpl(User user, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            if (amount < 0) {
                return BooleanIntPair.of(false, user.getCoins());
            }

            try (Connection connection = connectionProvider.getConnection()) {
                // Set coins
                try (PreparedStatement statement = connection.prepareStatement(String.format(SQL_SET_COINS, usersTable))) {
                    statement.setInt(1, amount);
                    statement.setInt(2, user.getDatabaseId());
                    statement.setInt(3, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_COINS, usersTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("coins");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set coins for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getCoins());
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setCoins(result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> addGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(getAddGadgetAmmo(), gadgetAmmoTable))) {
                    statement.setInt(1, user.getDatabaseId());
                    statement.setString(2, gadgetType.getKey());
                    statement.setInt(3, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_GADGET_AMMO, gadgetAmmoTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());
                            selectStatement.setString(2, gadgetType.getKey());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("amount");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to add gadget ammo for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getAmmo(gadgetType));
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setAmmo(gadgetType, result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(SQL_REMOVE_GADGET_AMMO, gadgetAmmoTable))) {
                    statement.setInt(1, amount);
                    statement.setInt(2, user.getDatabaseId());
                    statement.setString(3, gadgetType.getKey());
                    statement.setInt(4, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStmt = connection.prepareStatement(String.format(SQL_GET_GADGET_AMMO, gadgetAmmoTable))) {
                            selectStmt.setInt(1, user.getDatabaseId());
                            selectStmt.setString(2, gadgetType.getKey());

                            try (ResultSet resultSet = selectStmt.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("amount");
                                    return BooleanIntPair.of(true, newAmount);
                                } else {
                                    // Row was deleted (amount became 0), return 0
                                    return BooleanIntPair.of(true, 0);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to remove gadget ammo for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getAmmo(gadgetType));
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setAmmo(gadgetType, result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> setGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            if (amount < 0) {
                return BooleanIntPair.of(false, user.getAmmo(gadgetType));
            }

            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(getSetGadgetAmmo(), gadgetAmmoTable))) {
                    statement.setInt(1, user.getDatabaseId());
                    statement.setString(2, gadgetType.getKey());
                    statement.setInt(3, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStmt = connection.prepareStatement(String.format(SQL_GET_GADGET_AMMO, gadgetAmmoTable))) {
                            selectStmt.setInt(1, user.getDatabaseId());
                            selectStmt.setString(2, gadgetType.getKey());

                            try (ResultSet resultSet = selectStmt.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("amount");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set gadget ammo for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getAmmo(gadgetType));
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setAmmo(gadgetType, result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> addTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(getAddTreasureChests(), treasureChestsTable))) {
                    statement.setInt(1, user.getDatabaseId());
                    statement.setString(2, treasureChest.getKey());
                    statement.setInt(3, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_TREASURE_CHEST, treasureChestsTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());
                            selectStatement.setString(2, treasureChest.getKey());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("amount");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to add treasure chests for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getTreasureChests(treasureChest));
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setTreasureChests(treasureChest, result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> removeTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(SQL_REMOVE_TREASURE_CHESTS, treasureChestsTable))) {
                    statement.setInt(1, amount);
                    statement.setInt(2, user.getDatabaseId());
                    statement.setString(3, treasureChest.getKey());
                    statement.setInt(4, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_TREASURE_CHEST, treasureChestsTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());
                            selectStatement.setString(2, treasureChest.getKey());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("amount");
                                    return BooleanIntPair.of(true, newAmount);
                                } else {
                                    // Row was deleted (amount became 0), return 0
                                    return BooleanIntPair.of(true, 0);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to remove treasure chests for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getTreasureChests(treasureChest));
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setTreasureChests(treasureChest, result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public CompletableFuture<BooleanIntPair> setTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            if (amount < 0) {
                return BooleanIntPair.of(false, user.getTreasureChests(treasureChest));
            }

            try (Connection connection = connectionProvider.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(String.format(getSetTreasureChests(), treasureChestsTable))) {
                    statement.setInt(1, user.getDatabaseId());
                    statement.setString(2, treasureChest.getKey());
                    statement.setInt(3, amount);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Get the actual new value from database
                        try (PreparedStatement selectStatement = connection.prepareStatement(String.format(SQL_GET_TREASURE_CHEST, treasureChestsTable))) {
                            selectStatement.setInt(1, user.getDatabaseId());
                            selectStatement.setString(2, treasureChest.getKey());

                            try (ResultSet resultSet = selectStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    int newAmount = resultSet.getInt("amount");
                                    return BooleanIntPair.of(true, newAmount);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set treasure chests for player " + user + ".", e);
            }
            return BooleanIntPair.of(false, user.getTreasureChests(treasureChest));
        }).thenApplyAsync(result -> {
            if (result.leftBoolean()) {
                user.setTreasureChests(treasureChest, result.rightInt());
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    public void shutdown() {
        connectionProvider.shutdown();
    }
}
