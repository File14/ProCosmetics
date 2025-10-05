package se.file14.procosmetics.storage;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.storage.Database;

public abstract class DatabaseImpl implements Database {

    protected final ProCosmeticsPlugin plugin;

    public DatabaseImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }
}