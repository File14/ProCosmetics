package se.file14.procosmetics.listener.hook.essentials;

import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;

public class EssentialsVanishListener implements Listener {

    private final ProCosmeticsPlugin plugin;

    public EssentialsVanishListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        Player player = event.getAffected().getBase().getPlayer();

        if (player == null || !player.isOnline()) {
            return;
        }
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }
        if (event.getValue()) {
            user.unequipCosmetics(true, false);
        } else {
            user.equipSavedCosmetics(true);
        }
    }
}