package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

/**
 * Called when a player opens a treasure chest.
 */
public class PlayerOpenTreasureChestEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final TreasureChest treasureChest;

    /**
     * Constructs a new {@link PlayerPreEquipCosmeticEvent}.
     *
     * @param user          the user associated with this event
     * @param player        the Bukkit player equipping the cosmetic
     * @param treasureChest the treasure chest being opened
     */
    @ApiStatus.Internal
    public PlayerOpenTreasureChestEvent(User user, Player player, TreasureChest treasureChest) {
        this.user = user;
        this.player = player;
        this.treasureChest = treasureChest;
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
     * Gets the treasure chest for this event.
     *
     * @return the {@link TreasureChest} instance
     */
    public TreasureChest getTreasureChest() {
        return treasureChest;
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
