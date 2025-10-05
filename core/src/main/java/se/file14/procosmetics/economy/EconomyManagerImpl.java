package se.file14.procosmetics.economy;

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.economy.EconomyFailureException;
import se.file14.procosmetics.api.economy.EconomyManager;
import se.file14.procosmetics.api.economy.EconomyProvider;

import java.util.logging.Level;

public class EconomyManagerImpl implements EconomyManager {

    private static final int MAX_ATTEMPTS = 3;

    private final ProCosmeticsPlugin plugin;
    private EconomyType type;
    private EconomyProvider economy;
    private boolean shouldHook;

    public EconomyManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        String configuredType = plugin.getConfigManager().getMainConfig().getString("economy.type").toUpperCase();

        try {
            type = EconomyType.valueOf(configuredType);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().log(Level.WARNING, "Invalid economy type configured: " + configuredType + ". Using built-in economy system.", e);
            type = EconomyType.BUILT_IN;
        }

        if (type != EconomyType.CUSTOM) {
            economy = type.create(plugin);

            if (type != EconomyType.BUILT_IN) {
                shouldHook = true;
            }
        }
    }

    @Override
    public void register(EconomyProvider economyProvider) {
        economy = economyProvider;
        plugin.getLogger().log(Level.INFO, "Successfully hooked into the " + economy.getPlugin() + " economy.");
    }

    public void hookPlugin() {
        if (shouldHook) {
            hook(0);
        }
    }

    private void hook(int attempt) {
        try {
            economy.hook(plugin);
        } catch (EconomyFailureException e) {
            if (attempt < MAX_ATTEMPTS) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> hook(attempt + 1), 10L);
            } else {
                plugin.getLogger().log(Level.WARNING, "Failed to hook into " + economy.getPlugin() + ". Using built-in economy system as fallback.");
                economy = EconomyType.BUILT_IN.create(plugin);
            }
        }
    }

    public EconomyType getType() {
        return type;
    }

    @Override
    public EconomyProvider getEconomyProvider() {
        return economy;
    }
}
