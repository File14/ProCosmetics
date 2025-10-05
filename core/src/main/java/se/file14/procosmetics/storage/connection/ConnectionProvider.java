package se.file14.procosmetics.storage.connection;

import se.file14.procosmetics.ProCosmeticsPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    void initialize(ProCosmeticsPlugin plugin);

    String getConfigKey();

    Connection getConnection() throws SQLException;

    void shutdown();
}
