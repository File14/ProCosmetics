package se.file14.procosmetics.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.user.User;

public class CosmeticEntitySpawnEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final Entity entity;

    public CosmeticEntitySpawnEvent(User user, Player player, Entity entity) {
        this.user = user;
        this.player = player;
        this.entity = entity;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}