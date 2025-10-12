package se.file14.procosmetics.api.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Manages visibility and lifecycle of a group of NMS entities.
 * Entity trackers automatically handle spawning and despawning entities for players
 * based on configurable range, owner restrictions, anti-viewer lists, and custom
 * visibility predicates. Trackers run periodic updates to maintain viewer lists
 * and can be configured with callbacks for viewer and entity lifecycle events.
 *
 * <p>Typical usage involves creating a tracker via the {@link Builder}, adding
 * entities, and calling {@link #startTracking()} to begin automatic viewer management.</p>
 */
public interface EntityTracker {

    /**
     * Adds an entity to this tracker.
     *
     * @param nmsEntity The entity to add
     */
    void addEntity(NMSEntity nmsEntity);

    /**
     * Removes an entity from this tracker.
     *
     * @param nmsEntity The entity to remove
     */
    void removeEntity(NMSEntity nmsEntity);

    /**
     * Gets all entities managed by this tracker.
     *
     * @return Immutable collection of entities
     */
    Collection<NMSEntity> getEntities();

    /**
     * Removes all entities from this tracker.
     */
    void clearEntities();

    /**
     * Starts tracking and spawning entities for nearby players.
     */
    void startTracking();

    /**
     * Stops tracking and despawns entities for all viewers.
     */
    void stopTracking();

    /**
     * Checks if tracking is currently active.
     *
     * @return True if tracking is active
     */
    boolean isTracking();

    /**
     * Destroys this tracker and cleans up all resources.
     */
    void destroy();

    /**
     * Gets all players currently viewing entities from this tracker.
     *
     * @return Immutable collection of viewing players
     */
    Collection<Player> getViewers();

    /**
     * Manually adds a viewer to this tracker.
     *
     * @param player The player to add as a viewer
     */
    void addViewer(Player player);

    @ApiStatus.Internal
    void addViewers(Collection<Player> player);

    /**
     * Manually removes a viewer from this tracker.
     *
     * @param player The player to remove as a viewer
     */
    void removeViewer(Player player);

    @ApiStatus.Internal
    void removeViewers(Collection<Player> player);

    /**
     * Checks if a player is currently viewing entities from this tracker.
     *
     * @param player The player to check
     * @return True if the player is a viewer
     */
    boolean isViewer(Player player);

    /**
     * Adds a player to the anti-viewer list (prevents them from seeing entities).
     *
     * @param player The player to add to the anti-viewer list
     */
    void addAntiViewer(Player player);

    /**
     * Removes a player from the anti-viewer list.
     *
     * @param player The player to remove from the anti-viewer list
     */
    void removeAntiViewer(Player player);

    /**
     * Checks if a player is in the anti-viewer list.
     *
     * @param player The player to check
     * @return True if the player is an anti-viewer
     */
    boolean isAntiViewer(Player player);

    /**
     * Gets all anti-viewer UUIDs.
     *
     * @return Immutable collection of anti-viewer UUIDs
     */
    Collection<UUID> getAntiViewers();

    /**
     * Clears all anti-viewers.
     */
    void clearAntiViewers();

    /**
     * Sets the owner of this entity group.
     * Entities will only be visible to players who can see the owner.
     *
     * @param owner The owner player, or null to remove owner restrictions
     */
    void setOwner(@Nullable Player owner);

    /**
     * Gets the current owner of this entity group.
     *
     * @return The owner player, or null if no owner is set
     */
    @Nullable
    Player getOwner();

    /**
     * Gets the owner's UUID.
     *
     * @return The owner's UUID, or null if no owner is set
     */
    @Nullable
    UUID getOwnerUUID();

    /**
     * Sets the tracking range in blocks.
     *
     * @param range The range in blocks
     */
    void setTrackingRange(double range);

    /**
     * Gets the current tracking range.
     *
     * @return The tracking range in blocks
     */
    double getTrackingRange();

    /**
     * Sets the update interval in ticks.
     *
     * @param interval The interval in ticks
     */
    void setUpdateInterval(long interval);

    /**
     * Gets the current update interval.
     *
     * @return The update interval in ticks
     */
    long getUpdateInterval();

    /**
     * Sets the start delay in ticks.
     *
     * @param delay The delay in ticks
     */
    void setStartDelay(long delay);

    /**
     * Gets the current start delay.
     *
     * @return The start delay in ticks
     */
    long getStartDelay();

    /**
     * Sets a custom visibility predicate for determining if a player should see entities.
     * This is evaluated in addition to standard range and anti-viewer checks.
     *
     * @param predicate The visibility predicate, or null to remove custom rules
     */
    void setVisibilityPredicate(@Nullable Predicate<Player> predicate);

    /**
     * Sets a custom visibility predicate that considers both the potential viewer and owner.
     *
     * @param predicate The visibility predicate, or null to remove custom rules
     */
    void setOwnerVisibilityPredicate(@Nullable BiPredicate<Player, Player> predicate);

    /**
     * Forces all entities to respawn at a new location.
     *
     * @param location The new location
     */
    void respawnAt(Location location);

    /**
     * Gets the current tracking location (based on entities' positions).
     *
     * @return The tracking location, or null if no entities are present
     */
    @Nullable
    Location getTrackingLocation();

    /**
     * Schedules the tracker to be destroyed after a specified delay.
     *
     * @param ticks The delay in ticks
     */
    void destroyAfter(int ticks);

    /**
     * Forces an immediate update of all viewers.
     */
    void updateViewers();

    /**
     * Builder class for creating and configuring EntityTracker instances.
     */
    interface Builder {

        /**
         * Sets the tracking range in blocks.
         *
         * @param range The range in blocks
         * @return This builder
         */
        Builder trackingRange(double range);

        /**
         * Sets the update interval in ticks.
         *
         * @param interval The interval in ticks
         * @return This builder
         */
        Builder updateInterval(long interval);

        /**
         * Sets the start delay in ticks.
         *
         * @param delay The delay in ticks
         * @return This builder
         */
        Builder startDelay(long delay);

        /**
         * Sets the owner of the entity group.
         *
         * @param owner The owner player
         * @return This builder
         */
        Builder owner(@Nullable Player owner);

        /**
         * Sets a custom visibility predicate.
         *
         * @param predicate The visibility predicate
         * @return This builder
         */
        Builder visibilityPredicate(@Nullable Predicate<Player> predicate);

        /**
         * Sets a custom owner visibility predicate.
         *
         * @param predicate The owner visibility predicate
         * @return This builder
         */
        Builder ownerVisibilityPredicate(@Nullable BiPredicate<Player, Player> predicate);


        /**
         * Builds the EntityTracker with the configured settings.
         *
         * @return A new EntityTracker instance
         */
        EntityTracker build();
    }
}

