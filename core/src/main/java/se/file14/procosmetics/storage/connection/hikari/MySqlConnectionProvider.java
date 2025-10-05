package se.file14.procosmetics.storage.connection.hikari;

import java.util.Map;

public class MySqlConnectionProvider extends HikariConnectionProvider {

    @Override
    protected String getDriverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    protected String getDriverJdbcIdentifier() {
        return "mysql";
    }

    @Override
    protected void setDefaultProperties(Map<String, Object> properties) {
        super.setDefaultProperties(properties);
        properties.put("cachePrepStmts", "true");
        properties.put("prepStmtCacheSize", "250");
        properties.put("prepStmtCacheSqlLimit", "2048");
        properties.put("useServerPrepStmts", "true");
        properties.put("useLocalSessionState", "true");
        properties.put("rewriteBatchedStatements", "true");
        properties.put("cacheResultSetMetadata", "true");
        properties.put("cacheServerConfiguration", "true");
        properties.put("elideSetAutoCommits", "true");
        properties.put("maintainTimeStats", "false");
    }
}
