package se.file14.procosmetics.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.event.CosmeticEntitySpawnEvent;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

public class CosmeticEntitySpawnEventImpl extends Event implements CosmeticEntitySpawnEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ProCosmetics plugin;
    private final User user;
    private final Player player;
    private final Entity entity;

    public CosmeticEntitySpawnEventImpl(ProCosmetics plugin, User user, Player player, Entity entity) {
        this.plugin = plugin;
        this.user = user;
        this.player = player;
        this.entity = entity;
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
    public Entity getEntity() {
        return entity;
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