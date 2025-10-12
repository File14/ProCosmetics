package se.file14.procosmetics.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.entitytype.CachedEntityType;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class NMSEntityImpl<T> implements NMSEntity {

    private static final String TEAM_NAME = "PROCOSMETICS";
    private static final double MAXIMUM_DISTANCE_SQUARED_BEFORE_TELEPORT = 16 * 16;
    private static final double MAXIMUM_DISTANCE_SQUARED_BEFORE_PATH_FINDING = 5 * 5;

    protected final EntityTracker entityTracker;
    protected Location previousLocation;
    private float rideSpeed = 1.3f;

    public NMSEntityImpl(World world, CachedEntityType cachedEntityType, EntityTracker entityTracker) {
        this.entityTracker = entityTracker;
        this.previousLocation = new Location(world, 0, 0, 0);

        if (entityTracker != null) {
            entityTracker.addEntity(this);
        }
    }

    public NMSEntityImpl(World world, BlockData blockData, EntityTracker entityTracker) {
        this.entityTracker = entityTracker;
        this.previousLocation = new Location(world, 0, 0, 0);

        if (entityTracker != null) {
            entityTracker.addEntity(this);
        }
    }

    public NMSEntityImpl(Entity entity) {
        this.entityTracker = null;
        this.previousLocation = entity.getLocation();
    }

    public abstract void sendPacketToPlayers(@Nullable Collection<Player> players, @Nullable T packet);

    public abstract void sendPacketToViewers(@Nullable T packet);

    protected abstract T getSpawnPacket();

    protected abstract T getSpawnBundlePacket();

    @Override
    public void spawn(Collection<Player> players) {
        sendPacketToPlayers(players, getSpawnBundlePacket());
    }

    @Override
    public void spawn() {
        sendPacketToViewers(getSpawnBundlePacket());
    }

    protected abstract T getDespawnPacket();

    @Override
    public void despawn(Collection<Player> players) {
        sendPacketToPlayers(players, getDespawnPacket());
    }

    @Override
    public void despawn() {
        sendPacketToViewers(getDespawnPacket());
    }

    @Nullable
    protected abstract T getEntityMetadataPacket(boolean isInitialView);

    @Override
    public void sendEntityMetadataPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getEntityMetadataPacket(false));
    }

    @Override
    public void sendEntityMetadataPacket() {
        sendPacketToViewers(getEntityMetadataPacket(false));
    }

    @Nullable
    protected abstract T getUpdateAttributesPacket();

    @Override
    public void sendUpdateAttributesPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getUpdateAttributesPacket());
    }

    @Override
    public void sendUpdateAttributesPacket() {
        sendPacketToViewers(getUpdateAttributesPacket());
    }

    @Nullable
    public abstract T getEntityEquipmentPacket();

    @Override
    public void sendEntityEquipmentPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getEntityEquipmentPacket());
    }

    @Override
    public void sendEntityEquipmentPacket() {
        sendPacketToViewers(getEntityEquipmentPacket());
    }

    public abstract T getEntityEventPacket(byte eventId);

    @Override
    public void sendEntityEventPacket(Collection<Player> players, byte eventId) {
        sendPacketToPlayers(players, getEntityEventPacket(eventId));
    }

    @Override
    public void sendEntityEventPacket(byte eventId) {
        sendPacketToViewers(getEntityEventPacket(eventId));
    }

    public abstract T getAnimatePacket(int actionId);

    @Override
    public void sendAnimatePacket(Collection<Player> players, int actionId) {
        sendPacketToPlayers(players, getAnimatePacket(actionId));
    }

    @Override
    public void sendAnimatePacket(int actionId) {
        sendPacketToViewers(getAnimatePacket(actionId));
    }

    public abstract T getVelocityPacket();

    @Override
    public void sendVelocityPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getVelocityPacket());
    }

    @Override
    public void sendVelocityPacket() {
        sendPacketToViewers(getVelocityPacket());
    }

    public abstract T getSetPassengersPacket();

    @Override
    public void sendSetPassengersPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getSetPassengersPacket());
    }

    @Override
    public void sendSetPassengersPacket() {
        sendPacketToViewers(getSetPassengersPacket());
    }

    public abstract T getSetEntityLinkPacket();

    @Override
    public void sendSetEntityLinkPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getSetEntityLinkPacket());
    }

    @Override
    public void sendSetEntityLinkPacket() {
        sendPacketToViewers(getSetEntityLinkPacket());
    }

    public abstract T getRotateHeadPacket();

    @Override
    public void sendRotateHeadPacket() {
        sendPacketToViewers(getRotateHeadPacket());
    }

    @Override
    public void sendRotateHeadPacket(Collection<Player> players) {
        sendPacketToPlayers(players, getRotateHeadPacket());
    }

    public abstract T getTeleportEntityPacket();

    @Override
    public void sendPositionRotationPacket(Location location) {
        if (location == null || location.equals(previousLocation)) {
            return;
        }
        setPositionRotation(location);
        sendPacketToViewers(getTeleportEntityPacket());

        if (getBukkitEntity() instanceof LivingEntity) {
            sendRotateHeadPacket();
        }
    }

    @Override
    @Nullable
    public Location getPreviousLocation() {
        return previousLocation;
    }

    @Override
    public Location getPreviousLocation(Location location) {
        if (location != null && previousLocation != null) {
            location.setWorld(previousLocation.getWorld());
            location.setX(previousLocation.getX());
            location.setY(previousLocation.getY());
            location.setZ(previousLocation.getZ());
            location.setYaw(previousLocation.getYaw());
            location.setPitch(previousLocation.getPitch());
        }
        return location;
    }

    @Override
    public void setPreviousLocation(Location location) {
        if (previousLocation == null) {
            previousLocation = location.clone();
        } else {
            previousLocation.setWorld(location.getWorld());
            previousLocation.setX(location.getX());
            previousLocation.setY(location.getY());
            previousLocation.setZ(location.getZ());
            previousLocation.setYaw(location.getYaw());
            previousLocation.setPitch(location.getPitch());
        }
    }

    @Override
    public void follow(Player player) {
        Location location = player.getLocation();
        Entity bukkitEntity = getBukkitEntity();

        if (bukkitEntity == null || location.getWorld() != bukkitEntity.getWorld()) {
            return;
        }
        Location entityLocation = bukkitEntity.getLocation();
        double distanceSquared = location.distanceSquared(entityLocation);

        if (distanceSquared < MAXIMUM_DISTANCE_SQUARED_BEFORE_TELEPORT) {
            if (distanceSquared > MAXIMUM_DISTANCE_SQUARED_BEFORE_PATH_FINDING) {
                navigateTo(location.add(2.0d, 0.0d, 2.0d), 1.5d);
            }
        } else {
            entityLocation.setY(location.getY());

            if (location.distanceSquared(entityLocation) >= MAXIMUM_DISTANCE_SQUARED_BEFORE_TELEPORT) {
                bukkitEntity.teleport(location.add(2.0d, 0.0d, 2.0d));
            }
        }
    }

    @Override
    public float getRideSpeed() {
        return rideSpeed;
    }

    @Override
    public void setRideSpeed(float speed) {
        this.rideSpeed = speed;
    }

    @Override
    public void addCollision(Player player) {
        Team team = player.getScoreboard().getTeam(TEAM_NAME);

        if (team == null) {
            return;
        }
        Scoreboard mainScoreboard = ProCosmeticsPlugin.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        Entity bukkitEntity = getBukkitEntity();

        if (bukkitEntity != null) {
            if (player.getScoreboard().equals(mainScoreboard) && team.getSize() > 1) {
                team.removeEntry(bukkitEntity.getUniqueId().toString());
            } else {
                team.unregister();
            }
        }
    }

    @Override
    public void removeCollision(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);

        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        Entity bukkitEntity = getBukkitEntity();

        if (bukkitEntity != null) {
            String uuid = bukkitEntity.getUniqueId().toString();

            if (!team.hasEntry(uuid)) {
                team.addEntry(uuid);
            }
        }
    }

    public abstract Object getNMSEntity();

    @Override
    @Nullable
    public EntityTracker getTracker() {
        return entityTracker;
    }

    protected static <T> void addIfNotNull(Collection<T> collection, @Nullable T packet) {
        if (packet != null) {
            collection.add(packet);
        }
    }
}