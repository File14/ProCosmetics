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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestManager;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.broadcaster.Broadcaster;
import se.file14.procosmetics.api.util.broadcaster.LootBroadcaster;
import se.file14.procosmetics.event.PluginReloadEventImpl;
import se.file14.procosmetics.menu.menus.TreasureChestMenu;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.broadcaster.BroadcasterImpl;
import se.file14.procosmetics.util.broadcaster.LootBroadcasterImpl;
import se.file14.procosmetics.util.structure.NamedStructureData;
import se.file14.procosmetics.util.structure.StructureDataImpl;
import se.file14.procosmetics.util.structure.StructureReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TreasureChestManagerImpl implements TreasureChestManager {

    private final ProCosmeticsPlugin plugin;
    private final Config treasureChestsConfig;
    private final Config platformsConfig;
    private final Broadcaster openingBroadcaster;
    private final LootBroadcasterImpl lootBroadcaster;
    private final List<TreasureChest> treasuresChests = new ArrayList<>();
    private final List<TreasureChestPlatformImpl> platforms = new ArrayList<>();

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
            treasuresChests.add(new TreasureChestImpl(plugin, treasureChestsConfig, key.toLowerCase()));
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
            platforms.add(new TreasureChestPlatformImpl(plugin, id, location, namedStructureData));
        }
    }

    private void savePlatform(TreasureChestPlatformImpl platform) {
        String path = "platforms." + platform.getId();

        platformsConfig.set(path + ".location", LocationUtil.getStringFromLocation(platform.getCenter()));
        platformsConfig.set(path + ".layout", platform.getNamedStructureData().name());

        platformsConfig.save();
    }

    public void deletePlatform(TreasureChestPlatformImpl platform) {
        platform.destroy();

        platformsConfig.set("platforms." + platform.getId(), null);
        platformsConfig.save();

        platforms.remove(platform);
    }

    public void createPlatform(Location location, NamedStructureData structureData) {
        location.setPitch(0.0f);
        int id = generateID();

        TreasureChestPlatformImpl platform = new TreasureChestPlatformImpl(plugin, id, location, structureData);
        savePlatform(platform);
        platforms.add(platform);
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
        public void onPluginReload(PluginReloadEventImpl event) {
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
    public TreasureChestPlatformImpl getPlatform(Location location) {
        if (location == null) {
            return null;
        }
        for (TreasureChestPlatformImpl platform : platforms) {
            if (location.equals(platform.getCenter().getBlock().getLocation())) {
                return platform;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public TreasureChestPlatformImpl getPlatform(int id) {
        for (TreasureChestPlatformImpl platform : platforms) {
            if (id == platform.getId()) {
                return platform;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public TreasureChest getTreasureChest(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        for (TreasureChest treasureChest : treasuresChests) {
            if (treasureChest.getKey().equalsIgnoreCase(key)) {
                return treasureChest;
            }
        }
        return null;
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

    public List<TreasureChestPlatformImpl> getPlatforms() {
        return platforms;
    }

    @Override
    public List<TreasureChest> getTreasureChests() {
        return treasuresChests;
    }
}
