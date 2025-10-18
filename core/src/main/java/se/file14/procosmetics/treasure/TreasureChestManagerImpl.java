package se.file14.procosmetics.treasure;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
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
import se.file14.procosmetics.menu.menus.TreasureChestMenu;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.broadcaster.Broadcaster;
import se.file14.procosmetics.util.broadcaster.LootBroadcaster;
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
    private final Broadcaster openingTreasureBroadcaster;
    private final LootBroadcaster lootBroadcaster;
    private final List<TreasureChest> treasuresChests = new ArrayList<>();
    private final List<TreasureChestPlatformImpl> platforms = new ArrayList<>();

    public TreasureChestManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;

        this.treasureChestsConfig = plugin.getConfigManager().register("treasure_chests");
        this.platformsConfig = plugin.getConfigManager().register("data/treasure_chest_platforms");
        this.openingTreasureBroadcaster = new Broadcaster(treasureChestsConfig, "broadcast_opening_treasure");
        this.lootBroadcaster = new LootBroadcaster(plugin, treasureChestsConfig, "broadcast_loot");

        loadTreasureChests();
    }

    private void loadTreasureChests() {
        String mainPath = "treasure_chests";

        if (treasureChestsConfig.hasKey(mainPath)) {
            treasureChestsConfig.getConfigurationSection(mainPath).getKeys(false).forEach(key ->
                    treasuresChests.add(new TreasureChestImpl(plugin, key)));
        }
    }

    public void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Listeners(), plugin);
    }

    private void loadPlatforms() {
        String mainPath = "platforms";

        if (!platformsConfig.hasKey(mainPath)) {
            return;
        }

        for (String name : platformsConfig.getConfigurationSection(mainPath).getKeys(false)) {
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
        FileConfiguration configuration = platformsConfig.getConfiguration();
        String path = "platforms." + platform.getId();

        if (!platformsConfig.hasKey(path)) {
            configuration.createSection(path);
        }
        configuration.set(path + ".location", LocationUtil.getStringFromLocation(platform.getCenter()));
        configuration.set(path + ".layout", platform.getNamedStructureData().name());

        platformsConfig.save();
    }

    public void deletePlatform(TreasureChestPlatformImpl platform) {
        platform.destroy();

        FileConfiguration configuration = platformsConfig.getConfiguration();
        configuration.set("platforms." + platform.getId(), null);
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

        while (checkExists(i)) {
            i++;
        }
        return i;
    }

    private boolean checkExists(int i) {
        return platforms.stream().anyMatch(treasureChest ->
                treasureChest.getId() == i);
    }

    private final class Listeners implements Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onServerStartUp(ServerLoadEvent event) {
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

                for (TreasureChestPlatform platform : platforms) {
                    if (block.equals(platform.getCenter().getBlock())) {
                        new TreasureChestMenu(plugin, user).open();
                        user.setCurrentPlatform(platform);

                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public TreasureChestPlatformImpl getPlatform(Location location) {
        return platforms.stream().filter(platform ->
                location.getBlock().equals(platform.getCenter().getBlock())).findAny().orElse(null);
    }

    @Override
    @Nullable
    public TreasureChestPlatformImpl getPlatform(int id) {
        return platforms.stream().filter(platform ->
                id == platform.getId()).findAny().orElse(null);
    }

    @Override
    @Nullable
    public TreasureChest getTreasureChest(String key) {
        return treasuresChests.stream().filter(treasure ->
                key.equalsIgnoreCase(treasure.getKey())).findAny().orElse(null);
    }

    public Broadcaster getOpeningTreasureBroadcaster() {
        return openingTreasureBroadcaster;
    }

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