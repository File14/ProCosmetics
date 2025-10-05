package se.file14.procosmetics.nms;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.AbstractRunnable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class EntityTrackerImpl extends AbstractRunnable implements EntityTracker {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private static final double DEFAULT_TRACKING_RANGE = 48.0d;
    private static final long DEFAULT_UPDATE_INTERVAL = 20L;
    private static final long DEFAULT_START_DELAY = 1L;

    private double trackingRange = DEFAULT_TRACKING_RANGE;
    private long updateInterval = DEFAULT_UPDATE_INTERVAL;
    private long startDelay = DEFAULT_START_DELAY;

    private final Set<NMSEntity> entities = ConcurrentHashMap.newKeySet();
    private final Set<Player> viewers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> antiViewers = ConcurrentHashMap.newKeySet();

    private volatile Player owner;
    private volatile UUID ownerUUID;

    private volatile Predicate<Player> visibilityPredicate;
    private volatile BiPredicate<Player, Player> ownerVisibilityPredicate;

    private volatile ViewerCallback onViewerAdded;
    private volatile ViewerCallback onViewerRemoved;
    private volatile EntityCallback onEntityAdded;
    private volatile EntityCallback onEntityRemoved;

    private final ThreadLocal<Location> reusableLocation = ThreadLocal.withInitial(() -> new Location(null, 0, 0, 0));

    public EntityTrackerImpl() {
    }

    public EntityTrackerImpl(@Nullable Player owner) {
        setOwner(owner);
    }

    @Override
    public void addEntity(NMSEntity entity) {
        if (entities.add(entity)) {
            if (onEntityAdded != null) {
                onEntityAdded.onEvent(entity, this);
            }
        }
    }

    @Override
    public void removeEntity(NMSEntity entity) {
        if (entities.remove(entity)) {
            // Despawn entity for all current viewers
            for (Player viewer : viewers) {
                entity.despawn(viewer);
            }

            if (onEntityRemoved != null) {
                onEntityRemoved.onEvent(entity, this);
            }
        }
    }

    @Override

    public Collection<NMSEntity> getEntities() {
        return ImmutableSet.copyOf(entities);
    }

    @Override
    public void clearEntities() {
        Set<NMSEntity> entitiesToRemove = ImmutableSet.copyOf(entities);

        for (NMSEntity entity : entitiesToRemove) {
            removeEntity(entity);
        }
    }

    @Override
    public void startTracking() {
        if (!isTracking()) {
            runTaskTimer(PLUGIN, startDelay, updateInterval);
        }
    }

    @Override
    public void stopTracking() {
        if (isTracking()) {
            cancel();
            // Despawn all entities for all viewers
            for (Player viewer : ImmutableSet.copyOf(viewers)) {
                removeViewer(viewer);
            }
        }
    }

    @Override
    public boolean isTracking() {
        return isRunning();
    }

    @Override
    public void destroy() {
        stopTracking();
        clearEntities();
        clearAntiViewers();

        // Clear callbacks
        onViewerAdded = null;
        onViewerRemoved = null;
        onEntityAdded = null;
        onEntityRemoved = null;
    }

    @Override
    public Collection<Player> getViewers() {
        return ImmutableSet.copyOf(viewers);
    }

    @Override
    public void addViewer(Player player) {
        if (viewers.add(player)) {
            // Spawn all entities for this viewer
            for (NMSEntity entity : entities) {
                entity.spawn(player);
            }

            if (onViewerAdded != null) {
                onViewerAdded.onEvent(player, this);
            }
        }
    }

    @Override
    public void removeViewer(Player player) {
        if (viewers.remove(player)) {
            // Despawn all entities for this viewer
            for (NMSEntity entity : entities) {
                entity.despawn(player);
            }

            if (onViewerRemoved != null) {
                onViewerRemoved.onEvent(player, this);
            }
        }
    }

    @Override
    public boolean isViewer(Player player) {
        return viewers.contains(player);
    }

    @Override
    public void addAntiViewer(Player player) {
        antiViewers.add(player.getUniqueId());
        removeViewer(player);
    }

    @Override
    public void removeAntiViewer(Player player) {
        antiViewers.remove(player.getUniqueId());
    }

    @Override
    public boolean isAntiViewer(Player player) {
        return antiViewers.contains(player.getUniqueId());
    }

    @Override
    public Collection<UUID> getAntiViewers() {
        return ImmutableSet.copyOf(antiViewers);
    }

    @Override
    public void clearAntiViewers() {
        antiViewers.clear();
    }

    @Override
    public void setOwner(@Nullable Player owner) {
        this.owner = owner;
        this.ownerUUID = owner != null ? owner.getUniqueId() : null;

        // Update viewers based on the new owner
        if (isTracking()) {
            updateViewers();
        }
    }

    @Override
    @Nullable
    public Player getOwner() {
        return owner;
    }

    @Override
    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setTrackingRange(double range) {
        this.trackingRange = range;
    }

    @Override
    public double getTrackingRange() {
        return trackingRange;
    }

    @Override
    public void setUpdateInterval(long interval) {
        this.updateInterval = interval;

        // Restart tracking with the new interval if currently running
        if (isTracking()) {
            stopTracking();
            startTracking();
        }
    }

    @Override
    public long getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public void setStartDelay(long delay) {
        this.startDelay = delay;
    }

    @Override
    public long getStartDelay() {
        return startDelay;
    }

    @Override
    public void setVisibilityPredicate(@Nullable Predicate<Player> predicate) {
        this.visibilityPredicate = predicate;
    }

    @Override
    public void setOwnerVisibilityPredicate(@Nullable BiPredicate<Player, Player> predicate) {
        this.ownerVisibilityPredicate = predicate;
    }

    @Override
    public void respawnAt(Location location) {
        // Despawn all entities for all viewers
        for (Player viewer : ImmutableSet.copyOf(viewers)) {
            removeViewer(viewer);
        }
        // Update entity positions
        for (NMSEntity entity : entities) {
            entity.setPosition(location);
        }
        // Force update to respawn entities
        updateViewers();
    }

    @Override
    @Nullable
    public Location getTrackingLocation() {
        for (NMSEntity entity : entities) {
            Location location = entity.getPreviousLocation();
            if (location != null) {
                return location.clone();
            }
        }
        return null;
    }


    @Override
    public void destroyAfter(int ticks) {
        PLUGIN.getServer().getScheduler().runTaskLater(PLUGIN, this::destroy, ticks);
    }

    @Override
    public void updateViewers() {
        // Force immediate update
        run();
    }

    @Override
    public void setOnViewerAdded(@Nullable ViewerCallback callback) {
        this.onViewerAdded = callback;
    }

    @Override
    public void setOnViewerRemoved(@Nullable ViewerCallback callback) {
        this.onViewerRemoved = callback;
    }

    @Override
    public void setOnEntityAdded(@Nullable EntityCallback callback) {
        this.onEntityAdded = callback;
    }

    @Override
    public void setOnEntityRemoved(@Nullable EntityCallback callback) {
        this.onEntityRemoved = callback;
    }

    @Override
    public void run() {
        if (entities.isEmpty()) {
            return;
        }
        Location trackingLocation = getTrackingLocation();

        if (trackingLocation == null) {
            return;
        }
        // Remove offline viewers
        viewers.removeIf(player -> !player.isOnline());

        // Get reusable location for this thread
        Location playerLocation = reusableLocation.get();
        double rangeSquared = trackingRange * trackingRange;

        // Check all online players
        for (Player player : PLUGIN.getServer().getOnlinePlayers()) {
            if (!player.isValid()) {
                continue;
            }
            player.getLocation(playerLocation);
            boolean shouldView = shouldPlayerSeeEntities(player, playerLocation, trackingLocation, rangeSquared);
            boolean currentlyViewing = isViewer(player);

            if (shouldView && !currentlyViewing) {
                addViewer(player);
            } else if (!shouldView && currentlyViewing) {
                removeViewer(player);
            }
        }
    }

    /**
     * Determines if a player should see the entities based on all visibility rules.
     */
    private boolean shouldPlayerSeeEntities(Player player, Location playerLocation, Location trackingLocation, double rangeSquared) {
        // Check anti-viewers
        if (isAntiViewer(player)) {
            return false;
        }

        // Check world and range
        if (!playerLocation.getWorld().equals(trackingLocation.getWorld())
                || playerLocation.distanceSquared(trackingLocation) > rangeSquared) {
            return false;
        }

        // Check owner visibility
        if (owner != null && !canPlayerSeeOwner(player, owner)) {
            return false;
        }

        // Check custom visibility predicate
        if (visibilityPredicate != null && !visibilityPredicate.test(player)) {
            return false;
        }

        // Check owner visibility predicate
        if (ownerVisibilityPredicate != null && owner != null && !ownerVisibilityPredicate.test(player, owner)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a player can see the owner using Bukkit's canSee method.
     */
    private boolean canPlayerSeeOwner(Player player, Player owner) {
        if (player.equals(owner)) {
            return true; // Owner can always see their own entities
        }
        if (!owner.isOnline()) {
            return false; // Can't see entities of an offline owner
        }
        return player.canSee(owner);
    }


    public static Builder builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl implements EntityTracker.Builder {

        private double trackingRange = 32.0d;
        private long updateInterval = 20L;
        private long startDelay = 5L;
        private Player owner;
        private Predicate<Player> visibilityPredicate;
        private BiPredicate<Player, Player> ownerVisibilityPredicate;
        private EntityTrackerImpl.ViewerCallback onViewerAdded;
        private EntityTrackerImpl.ViewerCallback onViewerRemoved;
        private EntityTrackerImpl.EntityCallback onEntityAdded;
        private EntityTrackerImpl.EntityCallback onEntityRemoved;


        @Override
        public Builder trackingRange(double range) {
            this.trackingRange = range;
            return this;
        }


        @Override
        public Builder updateInterval(long interval) {
            this.updateInterval = interval;
            return this;
        }


        @Override
        public Builder startDelay(long delay) {
            this.startDelay = delay;
            return this;
        }


        @Override
        public Builder owner(@Nullable Player owner) {
            this.owner = owner;
            return this;
        }


        @Override
        public Builder visibilityPredicate(@Nullable Predicate<Player> predicate) {
            this.visibilityPredicate = predicate;
            return this;
        }


        @Override
        public Builder ownerVisibilityPredicate(@Nullable BiPredicate<Player, Player> predicate) {
            this.ownerVisibilityPredicate = predicate;
            return this;
        }


        @Override
        public Builder onViewerAdded(@Nullable EntityTrackerImpl.ViewerCallback callback) {
            this.onViewerAdded = callback;
            return this;
        }


        @Override
        public Builder onViewerRemoved(@Nullable EntityTrackerImpl.ViewerCallback callback) {
            this.onViewerRemoved = callback;
            return this;
        }


        @Override
        public Builder onEntityAdded(@Nullable EntityTrackerImpl.EntityCallback callback) {
            this.onEntityAdded = callback;
            return this;
        }


        @Override
        public Builder onEntityRemoved(@Nullable EntityTrackerImpl.EntityCallback callback) {
            this.onEntityRemoved = callback;
            return this;
        }


        @Override
        public EntityTrackerImpl build() {
            EntityTrackerImpl tracker = new EntityTrackerImpl();
            tracker.setTrackingRange(trackingRange);
            tracker.setUpdateInterval(updateInterval);
            tracker.setStartDelay(startDelay);
            tracker.setOwner(owner);
            tracker.setVisibilityPredicate(visibilityPredicate);
            tracker.setOwnerVisibilityPredicate(ownerVisibilityPredicate);
            tracker.setOnViewerAdded(onViewerAdded);
            tracker.setOnViewerRemoved(onViewerRemoved);
            tracker.setOnEntityAdded(onEntityAdded);
            tracker.setOnEntityRemoved(onEntityRemoved);
            return tracker;
        }
    }
}