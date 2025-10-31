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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import org.bson.Document;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;
import se.file14.procosmetics.storage.DatabaseImpl;
import se.file14.procosmetics.user.UserImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class MongoDbDatabase extends DatabaseImpl {

    private final MongoClient mongoClient;
    private MongoCollection<Document> usersCollection;
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public MongoDbDatabase(ProCosmeticsPlugin plugin) {
        super(plugin);

        Config config = plugin.getConfigManager().getMainConfig();
        String uri = config.getString("storage.mongodb.uri");
        String collectionPrefix = config.getString("storage.mongodb.collection_prefix");

        this.mongoClient = MongoClients.create(uri);

        // Extract database name from URI or use default
        String databaseName = extractDatabaseFromUri(uri);
        if (databaseName == null || databaseName.isEmpty()) {
            databaseName = config.getString("storage.mongodb.database");
        }

        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            this.usersCollection = database.getCollection(collectionPrefix + "_users");

            // Initialize ID counter from existing data
            Document maxIdDoc = usersCollection.find()
                    .sort(new Document("id", -1))
                    .limit(1)
                    .first();
            if (maxIdDoc != null) {
                idCounter.set(maxIdDoc.getInteger("id", 0));
            }
            createIndexes();
            plugin.getLogger().info("Successfully established connection to the MongoDB server!");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize MongoDB collections.", e);
        }
    }

    private String extractDatabaseFromUri(String uri) {
        try {
            // MongoDB URI format: mongodb://[username:password@]host[:port]/[database][?options]
            int slashIndex = uri.indexOf("/", "mongodb://".length());

            if (slashIndex != -1) {
                int questionIndex = uri.indexOf("?", slashIndex);
                String dbName;

                if (questionIndex != -1) {
                    dbName = uri.substring(slashIndex + 1, questionIndex);
                } else {
                    dbName = uri.substring(slashIndex + 1);
                }
                // Return null if database name is empty or just whitespace
                return dbName.trim().isEmpty() ? null : dbName;
            }
        } catch (IndexOutOfBoundsException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to extract database name from URI", e);
        }
        return null;
    }

    private void createIndexes() {
        usersCollection.createIndex(new Document("uuid", 1), new IndexOptions().unique(true));
        usersCollection.createIndex(new Document("name", 1).append("last_seen", -1));
        usersCollection.createIndex(new Document("id", 1), new IndexOptions().unique(true));
    }

    @Override
    public String getType() {
        return "MongoDB";
    }

    @Override
    public UserImpl loadUser(UUID uuid) {
        try {
            Document doc = usersCollection.find(Filters.eq("uuid", uuid.toString())).first();

            if (doc != null) {
                return loadUserFromDocument(doc);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load user with UUID: " + uuid, e);
        }
        return null;
    }

    @Override
    public UserImpl loadUser(String name) {
        try {
            Document doc = usersCollection
                    .find(Filters.eq("name", name))
                    .sort(new Document("last_seen", -1))
                    .limit(1)
                    .first();

            if (doc != null) {
                return loadUserFromDocument(doc);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load user with name: " + name, e);
        }
        return null;
    }

    @Override
    public UserImpl loadUser(int id) {
        try {
            Document doc = usersCollection.find(Filters.eq("id", id)).first();

            if (doc != null) {
                return loadUserFromDocument(doc);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load user with id: " + id, e);
        }
        return null;
    }

    private UserImpl loadUserFromDocument(Document document) {
        int id = document.getInteger("id");
        UUID uuid = UUID.fromString(document.getString("uuid"));
        String name = document.getString("name");
        int coins = document.getInteger("coins", 0);
        boolean selfViewMorph = document.getBoolean("self_view_morph", false);
        boolean selfViewStatus = document.getBoolean("self_view_status", false);

        UserImpl user = new UserImpl(plugin, id, uuid, name);
        user.setCoins(coins);
        user.setSelfViewMorph(selfViewMorph, false);
        user.setSelfViewStatus(selfViewStatus, false);

        // Load cosmetics if restore is enabled
        if (plugin.getConfigManager().getMainConfig().getBoolean("settings.restore_cosmetics")) {
            loadUserCosmetics(user, document);
        }
        loadUserGadgetAmmo(user, document);
        loadUserTreasureChests(user, document);

        return user;
    }

    private void loadUserCosmetics(UserImpl user, Document document) {
        try {
            List<Document> cosmetics = document.getList("cosmetics", Document.class, new ArrayList<>());

            for (Document cosmeticDocument : cosmetics) {
                String categoryKey = cosmeticDocument.getString("category");
                String cosmeticKey = cosmeticDocument.getString("cosmetic");
                CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategoryRaw(categoryKey);

                if (category == null || !category.isEnabled()) {
                    continue;
                }
                CosmeticType<?, ?> type = category.getCosmeticRegistry().getType(cosmeticKey);

                if (type == null) {
                    continue;
                }
                user.setCosmetic(category, ((CosmeticTypeImpl<?, ?>) type).create(plugin, user));
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load cosmetics for player: " + user, e);
        }
    }

    private void loadUserGadgetAmmo(UserImpl user, Document doc) {
        try {
            List<Document> gadgetAmmo = doc.getList("gadget_ammo", Document.class, new ArrayList<>());

            for (Document ammoDoc : gadgetAmmo) {
                String key = ammoDoc.getString("gadget");
                int amount = ammoDoc.getInteger("amount", 0);
                GadgetType type = plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getType(key);

                if (type != null) {
                    user.setAmmo(type, amount);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load gadget ammo for player: " + user, e);
        }
    }

    private void loadUserTreasureChests(UserImpl user, Document doc) {
        try {
            List<Document> treasures = doc.getList("treasure_chests", Document.class, new ArrayList<>());

            for (Document treasureDoc : treasures) {
                String treasureKey = treasureDoc.getString("treasure_chest");
                int amount = treasureDoc.getInteger("amount", 0);
                TreasureChest treasureChest = plugin.getTreasureChestManager().getTreasureChest(treasureKey);

                if (treasureChest != null) {
                    user.setTreasureChests(treasureChest, amount);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load treasure chests for player: " + user, e);
        }
    }

    @Override
    public UserImpl insertUser(UUID uuid, String name) {
        try {
            int id = idCounter.incrementAndGet();
            Date now = new Date();

            Document userDoc = new Document()
                    .append("id", id)
                    .append("uuid", uuid.toString())
                    .append("name", name)
                    .append("coins", 0)
                    .append("self_view_morph", false)
                    .append("self_view_status", false)
                    .append("created_at", now)
                    .append("last_seen", now)
                    .append("cosmetics", new ArrayList<>())
                    .append("gadget_ammo", new ArrayList<>())
                    .append("treasure_chests", new ArrayList<>()
                    );
            usersCollection.insertOne(userDoc);
            return new UserImpl(plugin, id, uuid, name);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to insert player " + name, e);
        }
        return null;
    }

    @Override
    public void updateName(User user, String name) {
        try {
            usersCollection.updateOne(
                    Filters.eq("id", user.getDatabaseId()),
                    Updates.set("name", name)
            );
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to update name for " + user, e);
        }
    }

    @Override
    public void updateLastSeen(User user) {
        try {
            usersCollection.updateOne(
                    Filters.eq("id", user.getDatabaseId()),
                    Updates.set("last_seen", new Date())
            );
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to update last seen for " + user, e);
        }
    }

    @Override
    public CompletableFuture<Boolean> saveEquippedCosmeticAsync(User user, CosmeticType<?, ?> cosmeticType) {
        if (cosmeticType.getCategory() == plugin.getCategoryRegistries().music()) {
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                String categoryKey = cosmeticType.getCategory().getKey();
                String cosmeticKey = cosmeticType.getKey();

                // Remove existing cosmetic in the same category
                usersCollection.updateOne(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.pull("cosmetics", Filters.eq("category", categoryKey))
                );

                // Add new cosmetic
                Document cosmeticDoc = new Document()
                        .append("category", categoryKey)
                        .append("cosmetic", cosmeticKey);

                usersCollection.updateOne(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.push("cosmetics", cosmeticDoc)
                );
                return true;
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to equip cosmetic for player " + user, e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> removeEquippedCosmeticAsync(User user, CosmeticCategory<?, ?, ?> category) {
        if (category == plugin.getCategoryRegistries().music()) {
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                usersCollection.updateOne(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.pull("cosmetics", Filters.eq("category", category.getKey()))
                );
                return true;
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to unequip cosmetic for player " + user, e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> setSelfViewMorphAsync(User user, boolean enabled) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                usersCollection.updateOne(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.set("self_view_morph", enabled)
                );
                return true;
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set self-view morph for player " + user, e);
                return false;
            }
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
            try {
                usersCollection.updateOne(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.set("self_view_status", enabled)
                );
                return true;
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set self-view status for player " + user, e);
                return false;
            }
        }).thenApplyAsync(result -> {
            if (result) {
                user.setSelfViewStatus(enabled, false);
            }
            return result;
        }, plugin.getSyncExecutor());
    }

    @Override
    protected CompletableFuture<BooleanIntPair> addCoinsAsyncImpl(User user, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.AFTER);

                Document result = usersCollection.findOneAndUpdate(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.inc("coins", amount),
                        options
                );

                if (result != null) {
                    int newAmount = result.getInteger("coins", 0);
                    return BooleanIntPair.of(true, newAmount);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to add coins for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> removeCoinsAsyncImpl(User user, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.AFTER);

                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.gte("coins", amount)
                        ),
                        Updates.inc("coins", -amount),
                        options
                );

                if (result != null) {
                    int newAmount = result.getInteger("coins", 0);
                    return BooleanIntPair.of(true, newAmount);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to remove coins for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> setCoinsAsyncImpl(User user, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            if (amount < 0) {
                return BooleanIntPair.of(false, user.getCoins());
            }

            try {
                FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.AFTER);

                Document result = usersCollection.findOneAndUpdate(
                        Filters.eq("id", user.getDatabaseId()),
                        Updates.set("coins", amount),
                        options
                );

                if (result != null) {
                    int newAmount = result.getInteger("coins", 0);
                    return BooleanIntPair.of(true, newAmount);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set coins for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> addGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String gadgetKey = gadgetType.getKey();

                // Try to increment existing entry
                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.elemMatch("gadget_ammo", Filters.eq("gadget", gadgetKey))
                        ),
                        Updates.inc("gadget_ammo.$.amount", amount),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                );

                // If no existing entry, create new one
                if (result == null) {
                    Document newAmmo = new Document()
                            .append("gadget", gadgetKey)
                            .append("amount", amount);

                    result = usersCollection.findOneAndUpdate(
                            Filters.eq("id", user.getDatabaseId()),
                            Updates.push("gadget_ammo", newAmmo),
                            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                    );
                }

                if (result != null) {
                    List<Document> gadgetAmmo = result.getList("gadget_ammo", Document.class, new ArrayList<>());

                    for (Document ammoDoc : gadgetAmmo) {
                        if (gadgetKey.equals(ammoDoc.getString("gadget"))) {
                            int newAmount = ammoDoc.getInteger("amount", 0);
                            return BooleanIntPair.of(true, newAmount);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to add gadget ammo for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> removeGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String gadgetKey = gadgetType.getKey();

                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.elemMatch("gadget_ammo",
                                        Filters.and(
                                                Filters.eq("gadget", gadgetKey),
                                                Filters.gte("amount", amount)
                                        ))
                        ),
                        Updates.inc("gadget_ammo.$.amount", -amount),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                );

                if (result != null) {
                    List<Document> gadgetAmmo = result.getList("gadget_ammo", Document.class, new ArrayList<>());

                    for (Document ammoDoc : gadgetAmmo) {
                        if (gadgetKey.equals(ammoDoc.getString("gadget"))) {
                            int newAmount = ammoDoc.getInteger("amount", 0);

                            // Remove entry if amount is 0
                            if (newAmount == 0) {
                                usersCollection.updateOne(
                                        Filters.eq("id", user.getDatabaseId()),
                                        Updates.pull("gadget_ammo", Filters.eq("gadget", gadgetKey))
                                );
                            }
                            return BooleanIntPair.of(true, newAmount);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to remove gadget ammo for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> setGadgetAmmoAsyncImpl(User user, GadgetType gadgetType, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            if (amount < 0) {
                return BooleanIntPair.of(false, user.getAmmo(gadgetType));
            }

            try {
                String gadgetKey = gadgetType.getKey();

                // Try to update existing entry using positional operator
                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.elemMatch("gadget_ammo", Filters.eq("gadget", gadgetKey))
                        ),
                        Updates.set("gadget_ammo.$.amount", amount),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                );

                // If no existing entry, create new one
                if (result == null) {
                    Document newAmmo = new Document()
                            .append("gadget", gadgetKey)
                            .append("amount", amount);

                    usersCollection.updateOne(
                            Filters.eq("id", user.getDatabaseId()),
                            Updates.push("gadget_ammo", newAmmo)
                    );
                }
                return BooleanIntPair.of(true, amount);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set gadget ammo for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> addTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String treasureKey = treasureChest.getKey();

                // Try to increment existing entry
                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.elemMatch("treasure_chests", Filters.eq("treasure_chest", treasureKey))
                        ),
                        Updates.inc("treasure_chests.$.amount", amount),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                );

                // If no existing entry, create new one
                if (result == null) {
                    Document newTreasure = new Document()
                            .append("treasure_chest", treasureKey)
                            .append("amount", amount);

                    result = usersCollection.findOneAndUpdate(
                            Filters.eq("id", user.getDatabaseId()),
                            Updates.push("treasure_chests", newTreasure),
                            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                    );
                }

                if (result != null) {
                    List<Document> treasures = result.getList("treasure_chests", Document.class, new ArrayList<>());

                    for (Document treasureDoc : treasures) {
                        if (treasureKey.equals(treasureDoc.getString("treasure_chest"))) {
                            int newAmount = treasureDoc.getInteger("amount", 0);
                            return BooleanIntPair.of(true, newAmount);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to add treasure chests for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> removeTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String treasureKey = treasureChest.getKey();
                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.elemMatch("treasure_chests",
                                        Filters.and(
                                                Filters.eq("treasure_chest", treasureKey),
                                                Filters.gte("amount", amount)
                                        ))
                        ),
                        Updates.inc("treasure_chests.$.amount", -amount),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                );

                if (result != null) {
                    List<Document> treasures = result.getList("treasure_chests", Document.class, new ArrayList<>());

                    for (Document treasureDoc : treasures) {
                        if (treasureKey.equals(treasureDoc.getString("treasure_chest"))) {
                            int newAmount = treasureDoc.getInteger("amount", 0);

                            // Remove entry if amount is 0
                            if (newAmount == 0) {
                                usersCollection.updateOne(
                                        Filters.eq("id", user.getDatabaseId()),
                                        Updates.pull("treasure_chests", Filters.eq("treasure_chest", treasureKey))
                                );
                            }
                            return BooleanIntPair.of(true, newAmount);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to remove treasure chests for player " + user, e);
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
    protected CompletableFuture<BooleanIntPair> setTreasureChestsAsyncImpl(User user, TreasureChest treasureChest, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            if (amount < 0) {
                return BooleanIntPair.of(false, user.getTreasureChests(treasureChest));
            }

            try {
                String treasureKey = treasureChest.getKey();

                // Try to update existing entry using positional operator
                Document result = usersCollection.findOneAndUpdate(
                        Filters.and(
                                Filters.eq("id", user.getDatabaseId()),
                                Filters.elemMatch("treasure_chests", Filters.eq("treasure_chest", treasureKey))
                        ),
                        Updates.set("treasure_chests.$.amount", amount),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
                );

                // If no existing entry, create new one
                if (result == null) {
                    Document newTreasure = new Document()
                            .append("treasure_chest", treasureKey)
                            .append("amount", amount);

                    usersCollection.updateOne(
                            Filters.eq("id", user.getDatabaseId()),
                            Updates.push("treasure_chests", newTreasure)
                    );
                }
                return BooleanIntPair.of(true, amount);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to set treasure chests for player " + user, e);
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
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
