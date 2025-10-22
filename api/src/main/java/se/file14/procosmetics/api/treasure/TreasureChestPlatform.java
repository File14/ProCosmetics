package se.file14.procosmetics.api.treasure;

import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.user.User;

import java.util.List;

/**
 * Represents a treasure chest platform.
 * <p>
 * A {@code TreasureChestPlatform} defines the world locations used during treasure openings,
 * such as the platform center and the chest spawn positions.
 *
 * @see TreasureChestManager
 * @see TreasureChest
 */
public interface TreasureChestPlatform {

    /**
     * Builds the treasure platform structure in the world.
     */
    @ApiStatus.Internal
    void build();

    /**
     * Destroys or removes the treasure platform from the world.
     */
    @ApiStatus.Internal
    void destroy();

    /**
     * Gets the unique numeric identifier of this treasure platform.
     * <p>
     * Each platform has a distinct ID used to track and retrieve it
     * through the {@link TreasureChestManager}.
     *
     * @return the platform ID
     */
    int getId();

    /**
     * Gets the central location of the treasure platform.
     *
     * @return the center {@link Location}
     */
    Location getCenter();

    /**
     * Gets all world locations where treasure chests are placed on this platform.
     *
     * @return a list of chest {@link Location} positions
     */
    List<Location> getChestLocations();

    /**
     * Gets the user currently using this treasure platform.
     *
     * @return the {@link User} associated with this platform, or {@code null} if unused
     */
    User getUser();

    /**
     * Assigns the user currently using this platform.
     *
     * @param user the {@link User} using this platform
     */
    @ApiStatus.Internal
    void setUser(User user);

    /**
     * Checks whether this platform is currently in use by a player.
     *
     * @return {@code true} if the platform is in use, otherwise {@code false}
     */
    boolean isInUse();
}
