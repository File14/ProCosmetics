package se.file14.procosmetics.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.event.PlayerPurchaseGadgetAmmoEvent;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

public class PlayerPurchaseGadgetAmmoEventImpl extends Event implements PlayerPurchaseGadgetAmmoEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ProCosmetics plugin;
    private final User user;
    private final Player player;
    private final GadgetType gadgetType;

    public PlayerPurchaseGadgetAmmoEventImpl(ProCosmetics plugin, User user, Player player, GadgetType gadgetType) {
        this.plugin = plugin;
        this.user = user;
        this.player = player;
        this.gadgetType = gadgetType;
    }

    @Override
    public ProCosmetics getPlugin() {
        return plugin;
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
    public GadgetType getGadgetType() {
        return gadgetType;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}