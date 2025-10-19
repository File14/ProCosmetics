package se.file14.procosmetics;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.ProCosmeticsProvider;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.registry.CategoryRegistries;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRarityRegistry;
import se.file14.procosmetics.api.storage.Database;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.CommandBase;
import se.file14.procosmetics.command.SimpleCommand;
import se.file14.procosmetics.command.commands.ProCosmeticsCommand;
import se.file14.procosmetics.command.commands.UnsupportedCommand;
import se.file14.procosmetics.command.commands.external.*;
import se.file14.procosmetics.config.ConfigManagerImpl;
import se.file14.procosmetics.cosmetic.registry.CategoryRegistriesImpl;
import se.file14.procosmetics.cosmetic.registry.CosmeticRarityRegistryImpl;
import se.file14.procosmetics.economy.EconomyManagerImpl;
import se.file14.procosmetics.listener.*;
import se.file14.procosmetics.listener.hook.cmi.CMIVanishListener;
import se.file14.procosmetics.listener.hook.essentials.EssentialsVanishListener;
import se.file14.procosmetics.listener.hook.premiumvanish.PremiumVanishListener;
import se.file14.procosmetics.locale.LanguageManagerImpl;
import se.file14.procosmetics.menu.MenuManagerImpl;
import se.file14.procosmetics.nms.NMSManagerImpl;
import se.file14.procosmetics.packet.PacketManager;
import se.file14.procosmetics.placeholder.PlaceholderManager;
import se.file14.procosmetics.redis.RedisManager;
import se.file14.procosmetics.storage.DatabaseTypeProvider;
import se.file14.procosmetics.treasure.TreasureChestManagerImpl;
import se.file14.procosmetics.treasure.TreasureChestPlatformImpl;
import se.file14.procosmetics.user.UserManagerImpl;
import se.file14.procosmetics.util.LogUtil;
import se.file14.procosmetics.util.ResourceExporter;
import se.file14.procosmetics.util.block.FakeBlockManager;
import se.file14.procosmetics.util.version.VersionUtil;
import se.file14.procosmetics.worldguard.WorldGuardManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProCosmeticsPlugin extends JavaPlugin implements ProCosmetics {

    private static ProCosmeticsPlugin plugin;
    private BukkitAudiences adventure;

    private Logger logger;
    private Executor syncExecutor;
    private Executor ayncExecutor;
    private ConfigManagerImpl configManager;
    private LanguageManagerImpl languageManager;
    private NMSManagerImpl nmsManager;
    private CosmeticRarityRegistryImpl cosmeticRarityRegistry;
    private CategoryRegistriesImpl categoryRegistries;
    private UserManagerImpl userManager;
    private TreasureChestManagerImpl treasureChestManager;
    private MenuManagerImpl menuManager;
    private PacketManager packetManager;
    private FakeBlockManager fakeBlockManager;
    private EconomyManagerImpl economyManager;
    private PlaceholderManager placeholderManager;
    private CommandBase commandBase;
    private RedisManager redisManager;
    private Database database;
    private WorldGuardManager worldGuardManager;
    private boolean disabling;

    @Override
    public void onLoad() {
        ProCosmeticsPlugin.plugin = this;
        logger = getLogger();
        syncExecutor = runnable -> getServer().getScheduler().runTask(this, runnable);
        ayncExecutor = runnable -> getServer().getScheduler().runTaskAsynchronously(this, runnable);
        disabling = false;

        if (!VersionUtil.isSupported()) {
            LogUtil.printUnsupported();
            return;
        }
        logger.info("Initializing...");

        ResourceExporter.export(this);

        configManager = new ConfigManagerImpl(this);
        languageManager = new LanguageManagerImpl(this);
        nmsManager = new NMSManagerImpl(this);
        cosmeticRarityRegistry = new CosmeticRarityRegistryImpl(this);
        categoryRegistries = new CategoryRegistriesImpl(this);
        userManager = new UserManagerImpl(this);
        treasureChestManager = new TreasureChestManagerImpl(this);
        menuManager = new MenuManagerImpl(this);
        packetManager = new PacketManager(this);
        fakeBlockManager = new FakeBlockManager(this);
        economyManager = new EconomyManagerImpl(this);
        placeholderManager = new PlaceholderManager(this);
        commandBase = new CommandBase(this);

        initializeRedis();
        initializeDatabase();
        initializeMetrics();
        preHookPlugins();
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        if (!VersionUtil.isSupported()) {
            LogUtil.printUnsupported();
            commandBase = new CommandBase(this);
            registerCommands(new UnsupportedCommand(this));
            return;
        }
        economyManager.hookPlugin();

        registerListeners();
        registerCommands();
        hookPlugins();
        checkUpdate();

        NoteBlockAPI.initializeAPI(this);

        userManager.loadOnlinePlayers();
        if (redisManager != null) {
            redisManager.registerChannels();
        }
        ProCosmeticsProvider.register(this);

        logger.info("Initialized!");
    }

    private void registerListeners() {
        userManager.registerListeners();
        treasureChestManager.registerListeners();
        menuManager.registerListeners();
        packetManager.registerListeners();

        registerListeners(new BlockListener(),
                new CosmeticItemListener(this),
                new CosmeticListener(this),
                new CreatureSpawnListener(),
                new EntityListener(),
                new FallDamageListener(this),
                new InventoryListener(),
                new PlayerListener(this)
        );
    }

    @Override
    public void onDisable() {
        if (!VersionUtil.isSupported()) {
            LogUtil.printUnsupported();
            return;
        }
        disabling = true;

        if (redisManager != null) {
            redisManager.shutdown();
        }

        for (User user : userManager.getAllConnected()) {
            user.clearAllCosmetics(true, false);
        }
        database.shutdown();

        for (TreasureChestPlatformImpl platform : treasureChestManager.getPlatforms()) {
            platform.getHologram().despawn();
        }
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);

        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        NoteBlockAPI.getAPI().onDisable(this);
    }

    private void initializeRedis() {
        if (configManager.getMainConfig().getBoolean("redis.enable")) {
            redisManager = new RedisManager(this);
        }
    }

    private void initializeDatabase() {
        database = DatabaseTypeProvider.createDatabase(plugin, configManager.getMainConfig().getString("storage.type"));
    }

    private void registerCommands() {
        commandBase.getCommands().clear();

        registerCommands(new CosmeticsCommand(this),
                new EquipCommand(this),
                new UnequipCommand(this),
                new UnequipAllCommand(this),
                new ToggleSelfViewCommand(this),
                new ProCosmeticsCommand(this)
        );
    }

    private void preHookPlugins() {
        PluginManager pluginManager = getServer().getPluginManager();

        if (pluginManager.getPlugin("WorldGuard") != null && configManager.getMainConfig().getBoolean("world_guard.enable")) {
            logger.info("Hooking into WorldGuard...");
            this.worldGuardManager = new WorldGuardManager(this);
        }
    }

    private void hookPlugins() {
        PluginManager pluginManager = getServer().getPluginManager();

        if (pluginManager.getPlugin("Essentials") != null) {
            logger.info("Hooking into Essentials...");
            registerListeners(new EssentialsVanishListener(this));
        }
        if (pluginManager.getPlugin("CMI") != null) {
            logger.info("Hooking into CMI...");
            registerListeners(new CMIVanishListener(this));
        }
        if (pluginManager.getPlugin("PremiumVanish") != null) {
            logger.info("Hooking into PremiumVanish...");
            registerListeners(new PremiumVanishListener(this));
        }
        if (pluginManager.getPlugin("SuperVanish") != null) {
            logger.info("Hooking into SuperVanish...");
            registerListeners(new PremiumVanishListener(this));
        }

        if (worldGuardManager != null) {
            worldGuardManager.registerHandler();
        }
    }

    private void initializeMetrics() {
        Config config = configManager.getMainConfig();

        if (config.getBoolean("settings.enable_metrics")) {
            Metrics metrics = new Metrics(this, 6408);
            metrics.addCustomChart(new SimplePie("database", () -> database.getType()));
            metrics.addCustomChart(new SimplePie("economy", () -> economyManager.getType().getName()));
            metrics.addCustomChart(new SimplePie("world-guard", () -> worldGuardManager != null ? "Yes" : "No"));
        }
    }

    private void checkUpdate() {
        if (configManager.getMainConfig().getBoolean("settings.check_updates")) {
            ayncExecutor.execute(() -> {
                try {
                    URLConnection urlConnection = URI.create("https://api.spigotmc.org/legacy/update.php?resource=49106").toURL().openConnection();
                    urlConnection.setConnectTimeout(1000);

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        String version = getDescription().getVersion();
                        String line = reader.readLine();

                        if (!line.equalsIgnoreCase(version)) {
                            getLogger().log(Level.INFO, "There is a newer version available for " + getName() + ". Currently running " + version + " and the latest release is " + line + ".");
                        }
                    }
                } catch (IOException e) {
                    getLogger().log(Level.WARNING, "Failed to check for plugin updates.");
                }
            });
        }
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            if (listener != null) {
                pluginManager.registerEvents(listener, this);
            }
        }
    }

    @SafeVarargs
    private void registerCommands(SimpleCommand<CommandSender>... commands) {
        for (SimpleCommand<CommandSender> command : commands) {
            commandBase.registerCommand(command);
        }
    }

    public static ProCosmeticsPlugin getPlugin() {
        return plugin;
    }

    public BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public Executor getSyncExecutor() {
        return syncExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return ayncExecutor;
    }

    @Override
    public ConfigManagerImpl getConfigManager() {
        return configManager;
    }

    @Override
    public LanguageManagerImpl getLanguageManager() {
        return languageManager;
    }

    @Override
    public NMSManagerImpl getNMSManager() {
        return nmsManager;
    }

    @Override
    public CosmeticRarityRegistry getCosmeticRarityRegistry() {
        return cosmeticRarityRegistry;
    }

    @Override
    public CategoryRegistries getCategoryRegistries() {
        return categoryRegistries;
    }

    @Override
    public UserManagerImpl getUserManager() {
        return userManager;
    }

    @Override
    public TreasureChestManagerImpl getTreasureChestManager() {
        return treasureChestManager;
    }

    @Override
    public MenuManagerImpl getMenuManager() {
        return menuManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public FakeBlockManager getBlockRestoreManager() {
        return fakeBlockManager;
    }

    @Override
    public EconomyManagerImpl getEconomyManager() {
        return economyManager;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    public CommandBase getCommandBase() {
        return commandBase;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public boolean isDisabling() {
        return disabling;
    }
}