package se.file14.procosmetics.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.menu.ClickableItem;
import se.file14.procosmetics.api.menu.Menu;
import se.file14.procosmetics.api.menu.MenuManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MenuManagerImpl implements MenuManager {

    private final ProCosmeticsPlugin plugin;
    private final Map<UUID, Menu> openMenus = new HashMap<>();

    public MenuManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Listeners(), plugin);
    }

    @Override
    public void register(Menu menu) {
        openMenus.put(menu.getPlayer().getUniqueId(), menu);
    }

    public Menu getOpenMenu(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    private final class Listeners implements Listener {

        @EventHandler
        private void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            InventoryHolder holder = inventory.getHolder();

            if (event.getAction() == InventoryAction.NOTHING && event.getCurrentItem() != null
                    || holder == null
                    || !holder.equals(player)) {
                return;
            }
            Menu menu = openMenus.get(player.getUniqueId());

            if (menu != null) {
                ClickableItem slot = menu.getClickableSlot(event.getRawSlot());

                event.setCancelled(true);

                if (slot != null && menu.isValid()) {
                    slot.handle(event);
                }
            }
        }

        @EventHandler
        private void onDrag(InventoryDragEvent event) {
            Player player = (Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            InventoryHolder holder = inventory.getHolder();

            if (holder == null || !holder.equals(player)) {
                return;
            }

            if (openMenus.get(player.getUniqueId()) != null) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onClose(InventoryCloseEvent event) {
            Player player = (Player) event.getPlayer();
            Menu menu = openMenus.remove(player.getUniqueId());

            if (menu != null && menu.isValid()) {
                menu.invalidate();
            }
        }

        @EventHandler
        private void onDeath(PlayerDeathEvent event) {
            Player player = event.getEntity();
            Menu menu = openMenus.remove(player.getUniqueId());

            if (menu != null && menu.isValid()) {
                menu.invalidate();
            }
        }

        @EventHandler
        private void onQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            Menu menu = openMenus.remove(player.getUniqueId());

            if (menu != null && menu.isValid()) {
                menu.invalidate();
            }
        }

        @EventHandler
        private void onReload(PluginDisableEvent event) {
            if (event.getPlugin() == plugin) {
                for (Menu menu : List.copyOf(openMenus.values())) {
                    menu.close();
                }
                openMenus.clear();
            }
        }
    }
}
