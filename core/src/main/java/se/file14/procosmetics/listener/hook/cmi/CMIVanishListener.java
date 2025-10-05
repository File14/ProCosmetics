package se.file14.procosmetics.listener.hook.cmi;

import com.Zrips.CMI.events.CMIPlayerUnVanishEvent;
import com.Zrips.CMI.events.CMIPlayerVanishEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;

public class CMIVanishListener implements Listener {

    private final ProCosmeticsPlugin plugin;

    public CMIVanishListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVanish(CMIPlayerVanishEvent event) {
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
    public void onUnVanish(CMIPlayerUnVanishEvent event) {
        Player player = event.getPlayer();

        if (player.isOnline()) {
            User user = plugin.getUserManager().getConnected(player);

            if (user != null) {
                user.equipSavedCosmetics(true);
            }
        }
    }
}
