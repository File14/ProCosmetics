package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.user.User;

public class PlayerUseGadgetEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final Gadget gadget;
    private boolean cancelled;

    public PlayerUseGadgetEvent(User user, Player player, Gadget gadget) {
        this.user = user;
        this.player = player;
        this.gadget = gadget;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

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

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}