package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.user.User;

public class PlayerPreEquipCosmeticEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final CosmeticType<?, ?> cosmeticType;
    private boolean cancelled;

    public PlayerPreEquipCosmeticEvent(User user, Player player, CosmeticType<?, ?> cosmeticType) {
        this.user = user;
        this.player = player;
        this.cosmeticType = cosmeticType;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public CosmeticType<?, ?> getCosmeticType() {
        return cosmeticType;
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