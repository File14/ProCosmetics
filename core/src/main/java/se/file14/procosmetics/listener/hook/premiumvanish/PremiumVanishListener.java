package se.file14.procosmetics.listener.hook.premiumvanish;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;

public class PremiumVanishListener implements Listener {

    private final ProCosmeticsPlugin plugin;

    public PremiumVanishListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVanish(PlayerHideEvent event) {
        Player player = event.getPlayer();

        if (!player.isOnline()) {
            return;
        }
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }
        user.unequipCosmetics(true, false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onUnVanish(PlayerShowEvent event) {
        Player player = event.getPlayer();

        if (!player.isOnline()) {
            return;
        }
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }
        user.equipSavedCosmetics(true);
    }
}