package se.file14.procosmetics.api.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Manages menu instances and tracks which menus are currently open for players.
 * This interface provides functionality for menu registration and retrieval.
 */
public interface MenuManager {

    /**
     * Registers a menu instance for internal tracking.
     *
     * @param menu the {@link Menu} instance to register
     */
    @ApiStatus.Internal
    void register(Menu menu);

    /**
     * Gets the menu that is currently open for the specified player.
     *
     * @param player the player to check
     * @return the menu currently open for the player, or null if no menu is open
     */
    @Nullable
    Menu getOpenMenu(Player player);
}
