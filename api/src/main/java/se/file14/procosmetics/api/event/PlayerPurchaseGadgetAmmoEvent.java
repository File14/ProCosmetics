package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;

public class PlayerPurchaseGadgetAmmoEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final GadgetType gadgetType;

    public PlayerPurchaseGadgetAmmoEvent(User user, Player player, GadgetType gadgetType) {
        this.user = user;
        this.player = player;
        this.gadgetType = gadgetType;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public GadgetType getGadgetType() {
        return gadgetType;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}