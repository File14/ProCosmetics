package se.file14.procosmetics.api.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents a clickable item in a menu with a custom click handler.
 * Implementations of this interface define the behavior that occurs when
 * a player clicks on an item in a menu inventory.
 */
public interface ClickableItem {

    /**
     * Handles the click event when a player clicks on this item.
     *
     * @param event the inventory click event containing information about the click
     */
    void handle(InventoryClickEvent event);
}
