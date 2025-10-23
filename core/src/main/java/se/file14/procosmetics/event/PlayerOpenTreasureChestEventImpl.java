package se.file14.procosmetics.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.event.PlayerOpenTreasureChestEvent;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

public class PlayerOpenTreasureChestEventImpl extends Event implements PlayerOpenTreasureChestEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ProCosmetics plugin;
    private final User user;
    private final Player player;
    private final TreasureChest treasureChest;

    public PlayerOpenTreasureChestEventImpl(ProCosmetics plugin, User user, Player player, TreasureChest treasureChest) {
        this.plugin = plugin;
        this.user = user;
        this.player = player;
        this.treasureChest = treasureChest;
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
    public TreasureChest getTreasureChest() {
        return treasureChest;
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