package se.file14.procosmetics.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;

public class FallDamageListener implements Listener {

    private final ProCosmeticsPlugin plugin;

    public FallDamageListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();

        if (event.getEntity() instanceof Player player
                && (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.SUFFOCATION)) {
            User user = plugin.getUserManager().getConnected(player);

            if (user != null && user.hasFallDamageProtection()) {
                event.setCancelled(true);
            }
        }
    }
}