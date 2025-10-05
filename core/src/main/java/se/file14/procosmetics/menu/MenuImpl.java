package se.file14.procosmetics.menu;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.menu.ClickableItem;
import se.file14.procosmetics.api.menu.Menu;
import se.file14.procosmetics.api.user.User;

import java.util.logging.Level;

public abstract class MenuImpl implements Menu {

    protected final ProCosmetics plugin;
    protected final User user;
    protected final Player player;
    protected final Component title;
    protected final int rows;
    protected final Inventory inventory;
    private final Int2ObjectMap<ClickableItem> clickableSlots = new Int2ObjectArrayMap<>();
    private boolean valid;
    private Menu previousMenu;

    protected MenuImpl(ProCosmetics plugin, User user, Component title, int rows) {
        this.plugin = plugin;
        this.user = user;
        this.player = user.getPlayer();
        this.title = title;
        this.rows = Math.min(6, Math.max(1, rows));
        this.inventory = plugin.getJavaPlugin().getServer().createInventory(player, getSize(),
                LegacyComponentSerializer.legacySection().serialize(title));
    }

    public void open() {
        if (valid) {
            return;
        }
        addItems();
        fillEmptySlots(inventory, getFillEmptySlotsItem());

        player.openInventory(inventory);
        plugin.getMenuManager().register(this);
        valid = true;
    }

    public void close() {
        if (valid) {
            player.closeInventory();
        }
    }

    protected abstract void addItems();

    private int getActualSlot(int slot) {
        int size = getSize();

        if (slot < 0) {
            slot = size + slot;
        }
        return slot;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack, ClickableItem clickable) {
        if (itemStack == null || clickable == null) {
            return;
        }
        int actualSlot = getActualSlot(slot);

        if (actualSlot > inventory.getSize()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            String itemName = itemMeta != null && itemMeta.hasDisplayName()
                    ? ChatColor.stripColor(itemMeta.getDisplayName())
                    : itemStack.getType().name();

            plugin.getJavaPlugin().getLogger().log(Level.WARNING, "Cannot add item " + itemName + " to menu " + title + ". Slot " + actualSlot
                    + " exceeds inventory size of " + inventory.getSize() + ".");
            return;
        }
        inventory.setItem(actualSlot, itemStack);
        clickableSlots.put(actualSlot, clickable);
    }

    @Override
    @Nullable
    public ClickableItem getClickableSlot(int slot) {
        return clickableSlots.getOrDefault(getActualSlot(slot), null);
    }

    @Override
    public void removeItem(int slot) {
        int actualSlot = getActualSlot(slot);

        inventory.setItem(actualSlot, null);
        clickableSlots.remove(actualSlot);
    }

    @Override
    public void clear() {
        inventory.clear();
        clickableSlots.clear();
    }

    @Override
    public void fillEmptySlots(Inventory inventory, @Nullable ItemStack itemStack) {
        if (inventory == null || itemStack == null) {
            return;
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack tempItemStack = inventory.getItem(i);

            if (tempItemStack == null || tempItemStack.getType().isAir()) {
                inventory.setItem(i, itemStack);
            }
        }
    }

    @Override
    public void playClickSound() {
        player.playSound(player, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }

    @Override
    public void playDenySound() {
        player.playSound(player, Sound.ENTITY_ITEM_BREAK, 0.5f, 0.5f);
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getSize() {
        return getRows() * 9;
    }

    @Override
    public void invalidate() {
        valid = false;
        clear();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setPreviousMenu(Menu previousMenu) {
        this.previousMenu = previousMenu;
    }

    @Override
    @Nullable
    public Menu getPreviousMenu() {
        return previousMenu;
    }

    @Override
    public void trySetPreviousMenuItem(int slot, ItemStack itemStack) {
        Menu previousMenu = getPreviousMenu();

        if (previousMenu != null) {
            setItem(slot, itemStack, event -> {
                playClickSound();
                previousMenu.open();
            });
        }
    }
}