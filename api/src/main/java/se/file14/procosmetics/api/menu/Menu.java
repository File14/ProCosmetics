package se.file14.procosmetics.api.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.user.User;

/**
 * Represents an interactive inventory menu for a player.
 */
public interface Menu {

    /**
     * Opens the menu for the player.
     * If the menu is already open, this method does nothing.
     */
    void open();

    /**
     * Closes the menu for the player.
     * If the menu is not open, this method does nothing.
     */
    void close();

    /**
     * Sets an item in the specified slot with a click handler.
     *
     * @param slot      The inventory slot (0-based)
     * @param itemStack The item to place in the slot
     * @param clickable The click handler for this item
     */
    void setItem(int slot, ItemStack itemStack, ClickableItem clickable);

    /**
     * Gets the click handler for a specific slot.
     *
     * @param slot The inventory slot to check
     * @return The clickable handler, or null if no handler exists
     */
    @Nullable
    ClickableItem getClickableSlot(int slot);

    /**
     * Removes an item from the specified slot.
     *
     * @param slot The inventory slot to clear
     */
    void removeItem(int slot);

    /**
     * Clears all items and click handlers from the menu.
     */
    void clear();

    /**
     * Gets the item used to fill empty inventory slots, if any.
     *
     * @return the {@link ItemStack} used for empty slots, or {@code null} if none is set
     */
    @Nullable
    ItemStack getFillEmptySlotsItem();

    /**
     * Fills empty slots in the inventory with a specific item.
     *
     * @param inventory The inventory to fill
     * @param itemStack The item to use for filling
     */
    void fillEmptySlots(Inventory inventory, ItemStack itemStack);

    /**
     * Plays the standard click sound for the player.
     */
    void playClickSound();

    /**
     * Plays the deny sound for the player.
     */
    void playDenySound();

    /**
     * Gets the user associated with this menu.
     *
     * @return The user object
     */
    User getUser();

    /**
     * Gets the player associated with this menu.
     *
     * @return The player object
     */
    Player getPlayer();

    /**
     * Gets the title of the menu.
     *
     * @return The menu title
     */
    Component getTitle();

    /**
     * Gets the number of rows in the menu.
     *
     * @return The number of rows (1-6)
     */
    int getRows();

    /**
     * Gets the total size of the menu (rows * 9).
     *
     * @return The total number of slots
     */
    int getSize();

    /**
     * Invalidates the menu, marking it as closed and clearing all items.
     */
    void invalidate();

    /**
     * Checks if the menu is currently valid/open.
     *
     * @return true if the menu is valid, false otherwise
     */
    boolean isValid();

    /**
     * Sets the previous menu for navigation purposes.
     *
     * @param previousMenu The menu to return to
     */
    void setPreviousMenu(Menu previousMenu);

    /**
     * Gets the previous menu, if any.
     *
     * @return The previous menu, or null if none exists
     */
    @Nullable
    Menu getPreviousMenu();

    /**
     * Attempts to set a "back" button item that opens the previous menu.
     * If no previous menu exists, this method does nothing.
     *
     * @param slot      The slot to place the back button
     * @param itemStack The item to use as the back button
     */
    void trySetPreviousMenuItem(int slot, ItemStack itemStack);
}
