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
package se.file14.procosmetics.treasure;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.event.PluginReloadEvent;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestManager;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.broadcaster.Broadcaster;
import se.file14.procosmetics.api.util.broadcaster.LootBroadcaster;
import se.file14.procosmetics.menu.menus.TreasureChestMenu;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.broadcaster.BroadcasterImpl;
import se.file14.procosmetics.util.broadcaster.LootBroadcasterImpl;
import se.file14.procosmetics.util.structure.NamedStructureData;
import se.file14.procosmetics.util.structure.StructureDataImpl;
import se.file14.procosmetics.util.structure.StructureReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TreasureChestManagerImpl implements TreasureChestManager {

    private final ProCosmeticsPlugin plugin;
    private final Config treasureChestsConfig;
    private final Config platformsConfig;
    private final Broadcaster openingBroadcaster;
    private final LootBroadcaster lootBroadcaster;
    private final Map<String, TreasureChest> treasuresChests = new HashMap<>();
    private final Map<Integer, TreasureChestPlatform> platforms = new Int2ObjectOpenHashMap<>();

    public TreasureChestManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;

        this.treasureChestsConfig = plugin.getConfigManager().register("treasure_chests");
        this.platformsConfig = plugin.getConfigManager().register("data/treasure_chest_platforms");
        this.openingBroadcaster = new BroadcasterImpl(treasureChestsConfig, "broadcast_opening_treasure");
        this.lootBroadcaster = new LootBroadcasterImpl(plugin, treasureChestsConfig, "broadcast_loot");

        loadTreasureChests();
    }

    private void loadTreasureChests() {
        for (String key : treasureChestsConfig.getSectionKeys("treasure_chests")) {
            key = key.toLowerCase();
            treasuresChests.put(key, new TreasureChestImpl(plugin, treasureChestsConfig, key));
        }
    }

    public void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Listeners(), plugin);
    }

    private void loadPlatforms() {
        String mainPath = "platforms";

        for (String name : platformsConfig.getSectionKeys(mainPath)) {
            String path = mainPath + "." + name + ".";
            int id;

            try {
                id = Integer.parseInt(name);
            } catch (NumberFormatException e) {
                plugin.getLogger().log(Level.WARNING, "Missing/invalid id for a treasure chest platform. Skipping.", e);
                continue;
            }

            if (getPlatform(id) != null) {
                plugin.getLogger().log(Level.WARNING, "Duplicate treasure chest platforms with id " + id + ". Only the first will be loaded.");
                continue;
            }
            String layout = platformsConfig.getString(path + "layout");
            StructureDataImpl structureData = StructureReader.loadStructure(layout);

            if (structureData == null) {
                plugin.getLogger().log(Level.WARNING, "Structure file " + layout + " not found for treasure chest platform " + id + "! Skipping.");
                continue;
            }
            NamedStructureData namedStructureData = new NamedStructureData(layout, structureData);
            Location location = LocationUtil.getLocationFromString(platformsConfig.getString(path + "location"));

            if (location == null) {
                plugin.getLogger().log(Level.WARNING, "Invalid or missing location for treasure chest platform " + id + ". Skipping.");
                continue;
            }
            platforms.put(id, new TreasureChestPlatformImpl(plugin, id, location, namedStructureData));
        }
    }

    private void savePlatform(TreasureChestPlatformImpl platform) {
        String path = "platforms." + platform.getId();

        platformsConfig.set(path + ".location", LocationUtil.getStringFromLocation(platform.getCenter()));
        platformsConfig.set(path + ".layout", platform.getNamedStructureData().name());

        platformsConfig.save();
    }

    public void deletePlatform(TreasureChestPlatform platform) {
        platform.destroy();

        platformsConfig.set("platforms." + platform.getId(), null);
        platformsConfig.save();

        platforms.remove(platform.getId());
    }

    public void createPlatform(Location location, NamedStructureData structureData) {
        location.setPitch(0.0f);
        int id = generateID();

        TreasureChestPlatformImpl platform = new TreasureChestPlatformImpl(plugin, id, location, structureData);
        savePlatform(platform);
        platforms.put(id, platform);
    }

    private int generateID() {
        int i = 0;

        while (getPlatform(i) != null) {
            i++;
        }
        return i;
    }

    private final class Listeners implements Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onServerStartUp(ServerLoadEvent event) {
            loadPlatforms();
        }

        @EventHandler
        public void onPluginReload(PluginReloadEvent event) {
            loadPlatforms();
        }

        @EventHandler
        public void onChestClick(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null) {
                User user = plugin.getUserManager().getConnected(player);

                if (user == null) {
                    return;
                }
                TreasureChestPlatform platform = getPlatform(block.getLocation());

                if (platform != null) {
                    new TreasureChestMenu(plugin, user).open();
                    user.setCurrentPlatform(platform);
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    @Nullable
    public TreasureChestPlatform getPlatform(Location location) {
        if (location == null) {
            return null;
        }
        for (TreasureChestPlatform platform : platforms.values()) {
            if (location.equals(platform.getCenter().getBlock().getLocation())) {
                return platform;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public TreasureChestPlatform getPlatform(int id) {
        return platforms.get(id);
    }

    @Override
    @Nullable
    public TreasureChest getTreasureChest(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        return treasuresChests.get(key.toLowerCase());
    }

    @Override
    public Config getTreasureChestsConfig() {
        return treasureChestsConfig;
    }

    @Override
    public Config getPlatformsConfig() {
        return platformsConfig;
    }

    @Override
    public Broadcaster getOpeningBroadcaster() {
        return openingBroadcaster;
    }

    @Override
    public LootBroadcaster getLootBroadcaster() {
        return lootBroadcaster;
    }

    @Override
    public Collection<TreasureChestPlatform> getPlatforms() {
        return platforms.values();
    }

    @Override
    public Collection<TreasureChest> getTreasureChests() {
        return treasuresChests.values();
    }
}
