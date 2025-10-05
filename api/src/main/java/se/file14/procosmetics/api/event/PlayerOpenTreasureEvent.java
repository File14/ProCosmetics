package se.file14.procosmetics.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

public class PlayerOpenTreasureEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final TreasureChest treasureChest;
    private final Location location;

    public PlayerOpenTreasureEvent(User user, Player player, TreasureChest treasureChest, Location location) {
        this.user = user;
        this.player = player;
        this.treasureChest = treasureChest;
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public TreasureChest getTreasure() {
        return treasureChest;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}