package se.file14.procosmetics.api.treasure;

import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Manages all registered treasure chests and active treasure platforms.
 * <p>
 * The {@code TreasureChestManager} provides access to loaded treasure chests,
 * their corresponding platforms, and utility methods for retrieving and managing
 * active treasure sessions.
 *
 * @see TreasureChest
 * @see TreasureChestPlatform
 */
public interface TreasureChestManager {

    /**
     * Gets the treasure platform located at the specified world location.
     *
     * @param location the world location to check
     * @return the {@link TreasureChestPlatform} at the location, or {@code null} if none exists
     */
    @Nullable
    TreasureChestPlatform getPlatform(Location location);

    /**
     * Gets a treasure platform by its unique numeric identifier.
     *
     * @param id the platform ID
     * @return the {@link TreasureChestPlatform} associated with the ID, or {@code null} if not found
     */
    @Nullable
    TreasureChestPlatform getPlatform(int id);

    /**
     * Gets a treasure chest by its unique key.
     *
     * @param key the treasure chest key
     * @return the {@link TreasureChest} associated with the key, or {@code null} if not found
     */
    @Nullable
    TreasureChest getTreasureChest(String key);

    /**
     * Gets a list of all loaded treasure chests.
     *
     * @return a list of all {@link TreasureChest} instances
     */
    List<TreasureChest> getTreasureChests();
}
