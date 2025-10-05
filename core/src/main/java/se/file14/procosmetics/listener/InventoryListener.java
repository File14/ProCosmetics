package se.file14.procosmetics.listener;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import se.file14.procosmetics.util.MetadataUtil;

public class InventoryListener implements Listener {

    public InventoryListener() {
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickupItem(InventoryPickupItemEvent event) {
        if (event.getInventory().getType() == InventoryType.HOPPER && MetadataUtil.isCustomEntity(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Entity entity && MetadataUtil.isCustomEntity(entity)) {
            event.setCancelled(true);
        }
    }
}
