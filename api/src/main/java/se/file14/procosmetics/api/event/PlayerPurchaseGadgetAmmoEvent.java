package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

/**
 * Called when a player purchases gadget ammo.
 */
public class PlayerPurchaseGadgetAmmoEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final GadgetType gadgetType;

    /**
     * Constructs a new {@link PlayerPurchaseGadgetAmmoEvent}.
     *
     * @param user       the user associated with this purchase
     * @param player     the Bukkit player who purchased ammo
     * @param gadgetType the gadget type for which ammo was purchased
     */
    @ApiStatus.Internal
    public PlayerPurchaseGadgetAmmoEvent(User user, Player player, GadgetType gadgetType) {
        this.user = user;
        this.player = player;
        this.gadgetType = gadgetType;
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
     * Gets the gadget type for this event.
     *
     * @return the {@link GadgetType} instance
     */
    public GadgetType getGadgetType() {
        return gadgetType;
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
