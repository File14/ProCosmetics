package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

/**
 * Called when a player attempts to use a gadget.
 */
public class PlayerPreUseGadgetEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final Gadget gadget;
    private boolean cancelled;

    /**
     * Constructs a new {@link PlayerPreUseGadgetEvent}.
     *
     * @param user   the user associated with this event
     * @param player the Bukkit player attempting to use the gadget
     * @param gadget the gadget being used
     */
    @ApiStatus.Internal
    public PlayerPreUseGadgetEvent(User user, Player player, Gadget gadget) {
        this.user = user;
        this.player = player;
        this.gadget = gadget;
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
     * Gets the gadget for this event.
     *
     * @return the {@link Gadget} instance
     */
    public Gadget getGadget() {
        return gadget;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
