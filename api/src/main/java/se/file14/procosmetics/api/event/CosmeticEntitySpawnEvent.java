package se.file14.procosmetics.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

/**
 * Called when a cosmetic-related server-entity is spawned for a player.
 */
public class CosmeticEntitySpawnEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final Entity entity;

    /**
     * Constructs a new {@link CosmeticEntitySpawnEvent}.
     *
     * @param user   the user associated with the spawned entity
     * @param player the Bukkit player who owns or triggered the cosmetic
     * @param entity the spawned entity instance
     */
    @ApiStatus.Internal
    public CosmeticEntitySpawnEvent(User user, Player player, Entity entity) {
        this.user = user;
        this.player = player;
        this.entity = entity;
    }

    /**
     * Gets the user for this event.
     *
     * @return the {@link User} instance
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the player for this event.
     *
     * @return the {@link Player} instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the cosmetic-related entity that was spawned.
     *
     * @return the spawned {@link Entity}
     */
    public Entity getEntity() {
        return entity;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return the static {@link HandlerList} instance
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
