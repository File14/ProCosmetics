package se.file14.procosmetics.storage.connection.hikari;

public class MariaDbConnectionProvider extends HikariConnectionProvider {

    @Override
    protected String getDriverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    protected String getDriverJdbcIdentifier() {
        return "mariadb";
    }
}
