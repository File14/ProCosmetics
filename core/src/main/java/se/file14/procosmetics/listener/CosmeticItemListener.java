package se.file14.procosmetics.listener;

import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.menus.MainMenu;
import se.file14.procosmetics.util.InventoryUtil;
import se.file14.procosmetics.util.item.ItemBuilderImpl;
import se.file14.procosmetics.util.item.ItemIdentifier;

import java.util.List;

public class CosmeticItemListener implements Listener {

    private static final ItemIdentifier COSMETIC_ID = new ItemIdentifier("COSMETIC_ITEM");

    private final ProCosmeticsPlugin plugin;
    private final Config config;

    public CosmeticItemListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager().getMainConfig();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.getBoolean("cosmetic_item.give_on.join") && isCosmeticAllowed(player.getWorld())) {
            User user = plugin.getUserManager().getConnected(player);

            if (user != null) {
                giveCosmeticItem(user, player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        InventoryUtil.remove(event.getPlayer().getInventory(), COSMETIC_ID);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }

        if (!isCosmeticAllowed(player.getWorld())) {
            user.unequipCosmetics(true, false);
            InventoryUtil.remove(event.getPlayer().getInventory(), COSMETIC_ID);
        } else if (!isCosmeticAllowed(event.getFrom())) {
            user.equipSavedCosmetics(true);

            if (config.getBoolean("cosmetic_item.give_on.world_change")) {
                giveCosmeticItem(user, player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        World respawnWorld = event.getRespawnLocation().getWorld();

        if (respawnWorld == null) {
            return;
        }
        Player player = event.getPlayer();

        if (isCosmeticAllowed(respawnWorld)) {
            if (config.getBoolean("cosmetic_item.give_on.death")
                    && (player.getWorld() == respawnWorld || player.getWorld() != respawnWorld
                    && config.getBoolean("cosmetic_item.give_on.world_change"))) {
                User user = plugin.getUserManager().getConnected(player);

                if (user != null) {
                    giveCosmeticItem(user, player);
                }
            }
            // We want to execute this 1 tick later since player.getLocation() returns the dead body location
            // and we want the cosmetics to spawn at the respawn-location
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                User user = plugin.getUserManager().getConnected(player);

                if (user != null) {
                    user.equipSavedCosmetics(true);
                }
            });
        }
    }

    private boolean isCosmeticAllowed(World world) {
        boolean blacklistedWorlds = config.getBoolean("multi_world.blacklisted_worlds");
        List<String> blackListedWorlds = config.getStringList("multi_world.worlds");
        String worldName = world.getName();

        return blacklistedWorlds && !blackListedWorlds.contains(worldName) ||
                !blacklistedWorlds && blackListedWorlds.contains(worldName);
    }

    @EventHandler()
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        // PREVENT SWAP
        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack itemStack1 = event.getClickedInventory().getItem(event.getSlot());
            ItemStack itemStack2 = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());

            if (event.getAction() == InventoryAction.HOTBAR_SWAP
                    && COSMETIC_ID.is(itemStack1) || COSMETIC_ID.is(itemStack2)) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        if (COSMETIC_ID.is(event.getCurrentItem())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (COSMETIC_ID.is(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(COSMETIC_ID::is);

        User user = plugin.getUserManager().getConnected(event.getEntity());

        if (user != null) {
            for (Cosmetic<?, ?> cosmetic : user.getCosmetics().values()) {
                CosmeticCategory<?, ?, ?> category = cosmetic.getType().getCategory();

                if (category.equals(plugin.getCategoryRegistries().arrowEffects())
                        || category.equals(plugin.getCategoryRegistries().deathEffects())) {
                    continue;
                }

                if (category.equals(plugin.getCategoryRegistries().music())) {
                    user.removeCosmetic(plugin.getCategoryRegistries().music(), true, false);
                } else {
                    user.unequipCosmetic(category, true, false);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwapItem(PlayerSwapHandItemsEvent event) {
        if (COSMETIC_ID.is(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (COSMETIC_ID.is(itemStack)) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            User user = plugin.getUserManager().getConnected(player);

            if (user != null) {
                new MainMenu(plugin, user).open();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityRightClick(PlayerInteractEntityEvent event) {
        // TODO: Add shelf support (1.21.9)
        if (event.getRightClicked() instanceof ItemFrame) {
            PlayerInventory playerInventory = event.getPlayer().getInventory();
            ItemStack itemStack = playerInventory.getItem(playerInventory.getHeldItemSlot());

            if (COSMETIC_ID.is(itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    public void giveCosmeticItem(User user, Player player) {
        ItemBuilder itemBuilder = getCosmeticItem(user);
        player.getInventory().setItem(itemBuilder.getSlot(), itemBuilder.getItemStack());
    }

    public ItemBuilder getCosmeticItem(User user) {
        return COSMETIC_ID.apply(new ItemBuilderImpl(config, "cosmetic_item")
                .setDisplayName(user.translate("item.cosmetic.name"))
                .setLoreComponent(user.translateList("item.cosmetic.desc")));
    }
}