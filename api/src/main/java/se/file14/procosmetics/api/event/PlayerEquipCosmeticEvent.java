package se.file14.procosmetics.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nonnull;

/**
 * Called when a player equips a cosmetic.
 */
public class PlayerEquipCosmeticEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Player player;
    private final CosmeticType<?, ?> cosmeticType;

    /**
     * Constructs a new {@link PlayerPreEquipCosmeticEvent}.
     *
     * @param user         the user associated with this event
     * @param player       the Bukkit player equipping the cosmetic
     * @param cosmeticType the cosmetic type being equipped
     */
    @ApiStatus.Internal
    public PlayerEquipCosmeticEvent(User user, Player player, CosmeticType<?, ?> cosmeticType) {
        this.user = user;
        this.player = player;
        this.cosmeticType = cosmeticType;
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
     * Gets the cosmetic type for this event.
     *
     * @return the {@link CosmeticType} instance
     */
    public CosmeticType<?, ?> getCosmeticType() {
        return cosmeticType;
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
