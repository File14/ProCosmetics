package se.file14.procosmetics.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.entitytype.CachedEntityType;
import se.file14.procosmetics.util.ReflectionUtil;
import se.file14.procosmetics.util.mapping.MappingRegistry;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public abstract class NMSEntityImpl implements NMSEntity {

    private static final String TEAM_NAME = "PROCOSMETICS";
    private static final double MAXIMUM_DISTANCE_BEFORE_TELEPORT = 16 * 16;
    private static final double MINIMUM_DISTANCE_BEFORE_PATH_FINDING = 5 * 5;

    protected static final Field BOUNDING_BOX_FIELD;
    protected static final Field JUMPING_FIELD;
    protected static final Method GET_GUARDIAN_A_TARGET_METHOD;
    private static final Constructor<?> AXIS_ALIGNED_BB_CONSTRUCTOR;

    protected final EntityTracker entityTracker;
    protected Location previousLocation;
    private float defaultRideSpeed = 1.3f;

    static {
        BOUNDING_BOX_FIELD = ReflectionUtil.getDeclaredField(
                ReflectionUtil.getNMSClass("world.entity", "Entity"),
                MappingRegistry.getMappedFieldName(MappingRegistry.BOUNDING_BOX),
                ReflectionUtil.getNMSClass("world.phys", "AxisAlignedBB")
        );
        JUMPING_FIELD = ReflectionUtil.getDeclaredField(
                ReflectionUtil.getNMSClass("world.entity", MappingRegistry.getMappedFieldName(MappingRegistry.LIVING_ENTITY)),
                MappingRegistry.getMappedFieldName(MappingRegistry.JUMPING)
        );

        GET_GUARDIAN_A_TARGET_METHOD = ReflectionUtil.getDeclaredMethod(ReflectionUtil.getNMSClass("world.entity.monster", MappingRegistry.getMappedFieldName(MappingRegistry.GUARDIAN)),
                MappingRegistry.getMappedFieldName(MappingRegistry.GUARDIAN_MOVE), boolean.class
        );

        AXIS_ALIGNED_BB_CONSTRUCTOR = ReflectionUtil.getConstructor(ReflectionUtil.getNMSClass("world.phys", "AxisAlignedBB"),
                Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE
        );
    }

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
        this.entityTracker = null; // For server-sided entities we don't need a tracker
        this.previousLocation = entity.getLocation();
    }

    public abstract void spawn(Player player);

    public abstract void despawn(Player... player);

    public abstract void sendPacketsToViewers(Object... packet);

    public abstract void sendEntityMetadataPacket(Player player);

    public abstract void sendUpdateAttributesPacket(Player player);

    public abstract void sendEntityEquipmentPacket(Player player);

    public abstract void sendPositionRotationPacket(Location location);

    public abstract void sendMetadataPacket();

    public abstract void sendIronGolemAttackAnimationPacket();

    public abstract void sendRabbitJumpAnimationPacket();

    public abstract void sendEntityAnimationPacket();

    public abstract void sendLeashHolderPacket(Player player);

    public abstract void sendVelocityPacket(Player player);

    public abstract void sendVelocityPacket();

    public abstract void sendMountPacket(Player player);

    public abstract void setMainHand(ItemStack itemStack);

    public abstract void setOffhandHand(ItemStack itemStack);

    public abstract void setHelmet(ItemStack itemStack);

    public abstract void setChestplate(ItemStack itemStack);

    public abstract void setLeggings(ItemStack itemStack);

    public abstract void setBoots(ItemStack itemStack);

    public abstract void setPositionRotation(Location location);

    public abstract void sendHeadRotationPacket();

    public abstract void setInvisible(boolean invisible);

    public abstract void setLeashHolder(Entity entity);

    public abstract void setBaby(boolean baby);

    public abstract void setZombieBaby(boolean baby);

    public abstract void setArmorStandSmall(boolean small);

    public abstract void setArmorStandBasePlate(boolean plate);

    public abstract void setArmorStandArms(boolean arms);

    public abstract void setArmorStandMarker(boolean marker);

    public abstract void setGuardianAttackTarget(int id);

    public abstract void setSlimeSize(int size);

    public abstract void setSilent(boolean silent);

    public abstract void setBatSleeping(boolean sleeping);

    public abstract void setCreeperIgnited(boolean ignited);

    public abstract void setCreeperPowered(boolean powered);

    public abstract boolean isCreeperPowered();

    public abstract void setSneaking(boolean sneaking);

    public abstract void setGravity(boolean gravity);

    public abstract void setCustomName(Component component);

    public abstract void setHeadPose(float x, float y, float z);

    public abstract void setBodyPose(float x, float y, float z);

    public abstract void setLeftArmPose(float x, float y, float z);

    public abstract void setRightArmPose(float x, float y, float z);

    public abstract void setLeftLegPose(float x, float y, float z);

    public abstract void setRightLegPose(float x, float y, float z);

    public abstract void setNoClip(boolean noClip);

    public abstract void setNoAI(boolean ai);

    public abstract void setInvulnerable(boolean invulnerable);

    public abstract void setPersistent(boolean persistent);

    public abstract void setDonkeyStanding(boolean standing);

    public abstract void setYaw(float yaw);

    public abstract void setPitch(float pitch);

    public abstract void move(Vector vector);

    public abstract void moveRide(Player player);

    public abstract void setVelocity(double x, double y, double z);

    public abstract void goToLocation(Location location, double speed);

    public abstract void setHurtTicks(int ticks);

    public abstract void setEntityItemStack(ItemStack itemStack);

    public abstract Object getNMSEntity();

    public abstract Entity getBukkitEntity();

    public abstract int getId();

    public abstract void removePathfinder();

    @Override
    public void setPosition(Location location) {
        setPositionRotation(location);
    }

    @Override
    @Nullable
    public Location getPreviousLocation() {
        return previousLocation;
    }

    @Override
    public void setVelocity(Vector velocity) {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public void setOffHand(@Nullable ItemStack itemStack) {
        setOffhandHand(itemStack);
    }

    @Override
    public void setAI(boolean ai) {
        setNoAI(!ai);
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
    @Nullable
    public EntityTracker getTracker() {
        return entityTracker;
    }

    @Override
    public Collection<Player> getViewers() {
        return entityTracker != null ? entityTracker.getViewers() : null;
    }

    @Override
    public void setGuardianTarget(int targetId) {
        setGuardianAttackTarget(targetId);

        try {
            Object nmsEntity = getNMSEntity();

            if (nmsEntity != null) {
                GET_GUARDIAN_A_TARGET_METHOD.invoke(nmsEntity, targetId != 0);
                sendMetadataPacket();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playAttackAnimation() {
        sendIronGolemAttackAnimationPacket();
    }

    @Override
    public void playRabbitJumpAnimation() {
        sendRabbitJumpAnimationPacket();
    }

    @Override
    public void mount(Entity mount) {
        // Implementation depends on NMS version
        // This would typically involve setting the vehicle/passenger relationship
    }

    @Override
    public void dismount() {
        // Implementation depends on NMS version
        // This would typically involve removing the vehicle/passenger relationship
    }

    @Override
    public float getRideSpeed() {
        return defaultRideSpeed;
    }

    @Override
    public void setRideSpeed(float speed) {
        this.defaultRideSpeed = speed;
    }

    @Override
    public void cleanup() {
        if (entityTracker != null) {
            entityTracker.removeEntity(this);
        }
    }

    public void sendEntityEquipmentPacket() {
        if (entityTracker != null) {
            entityTracker.getViewers().forEach(this::sendEntityEquipmentPacket);
        }
    }

    public Location getPreviousLocation(Location loc) {
        if (loc != null && previousLocation != null) {
            loc.setWorld(previousLocation.getWorld());
            loc.setX(previousLocation.getX());
            loc.setY(previousLocation.getY());
            loc.setZ(previousLocation.getZ());
            loc.setYaw(previousLocation.getYaw());
            loc.setPitch(previousLocation.getPitch());
        }
        return loc;
    }

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

    public float getDefaultRideSpeed() {
        return defaultRideSpeed;
    }

    public void setDefaultRideSpeed(float defaultRideSpeed) {
        this.defaultRideSpeed = defaultRideSpeed;
    }

    public void follow(Player player) {
        Location location = player.getLocation();
        Entity bukkitEntity = getBukkitEntity();

        if (bukkitEntity == null || location.getWorld() != bukkitEntity.getWorld()) {
            return;
        }
        Location entityLocation = bukkitEntity.getLocation();
        double distanceSquared = location.distanceSquared(entityLocation);

        if (distanceSquared < MAXIMUM_DISTANCE_BEFORE_TELEPORT) {
            if (distanceSquared > MINIMUM_DISTANCE_BEFORE_PATH_FINDING) {
                goToLocation(location.add(2.0d, 0.0d, 2.0d), 1.5d);
            }
        } else {
            entityLocation.setY(location.getY());

            if (location.distanceSquared(entityLocation) >= 900.0d) {
                bukkitEntity.teleport(location.add(2.0d, 0.0d, 2.0d));
            }
        }
    }

    @Override
    public void setBoundingBox(BoundingBox boundingBox) {
        Entity bukkitEntity = getBukkitEntity();

        if (bukkitEntity == null) {
            return;
        }
        Location location = bukkitEntity.getLocation();

        try {
            BOUNDING_BOX_FIELD.set(getNMSEntity(),
                    AXIS_ALIGNED_BB_CONSTRUCTOR.newInstance(location.getX() - boundingBox.getX(),
                            location.getY() - boundingBox.getY(), location.getZ() - boundingBox.getZ(),
                            location.getX() + boundingBox.getX(), location.getY() + boundingBox.getY(),
                            location.getZ() + boundingBox.getZ()
                    ));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}