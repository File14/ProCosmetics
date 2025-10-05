package se.file14.procosmetics.storage.connection.hikari;

public class PostgresConnectionProvider extends HikariConnectionProvider {

    @Override
    protected String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    protected String getDriverJdbcIdentifier() {
        return "postgresql";
    }
}