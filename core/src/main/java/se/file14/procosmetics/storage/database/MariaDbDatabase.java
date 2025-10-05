package se.file14.procosmetics.storage.database;

import se.file14.procosmetics.ProCosmeticsPlugin;

public class MariaDbDatabase extends MysqlDatabase {

    public MariaDbDatabase(ProCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getType() {
        return "MariaDB";
    }
}
