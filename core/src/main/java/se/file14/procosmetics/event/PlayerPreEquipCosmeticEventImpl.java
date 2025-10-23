package se.file14.procosmetics.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.event.PlayerPreEquipCosmeticEvent;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

public class PlayerPreEquipCosmeticEventImpl extends Event implements PlayerPreEquipCosmeticEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ProCosmetics plugin;
    private final User user;
    private final Player player;
    private final CosmeticType<?, ?> cosmeticType;
    private boolean cancelled;

    public PlayerPreEquipCosmeticEventImpl(ProCosmetics plugin, User user, Player player, CosmeticType<?, ?> cosmeticType) {
        this.plugin = plugin;
        this.user = user;
        this.player = player;
        this.cosmeticType = cosmeticType;
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

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}