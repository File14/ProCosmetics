package se.file14.procosmetics.placeholder;

import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.placeholder.incoming.IN_PlaceholderAPI;
import se.file14.procosmetics.placeholder.outgoing.OUT_PlaceHolderAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlaceholderManager {

    private final ProCosmeticsPlugin plugin;
    private final List<AbstractPlaceholder> placeholders = new ArrayList<>();

    public PlaceholderManager(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        hookPlugins();
    }

    private void hookPlugins() {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            plugin.getLogger().log(Level.INFO, "Hooking into PlaceholderAPI...");
            new OUT_PlaceHolderAPI(plugin).register();
            register(new IN_PlaceholderAPI());
        }
    }

    public void register(AbstractPlaceholder placeholder) {
        placeholders.add(placeholder);
    }

    public List<String> setPlaceholders(Player player, List<String> text) {
        for (AbstractPlaceholder placeholder : placeholders) {
            text = placeholder.setPlaceholders(player, text);
        }
        return text;
    }

    public String setPlaceholders(Player player, String text) {
        for (AbstractPlaceholder placeholder : placeholders) {
            text = placeholder.setPlaceholders(player, text);
        }
        text = text.replace("<coins>", String.valueOf(plugin.getEconomyManager().getEconomyProvider().getCoins(plugin.getUserManager().getConnected(player))))
                .replace("<ping>", String.valueOf(plugin.getNMSManager().getNMSUtil().getPing(player)));

        return text;
    }
}
