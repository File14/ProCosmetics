package se.file14.procosmetics.api.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Represents a version-independent NMS (Net Minecraft Server) entity wrapper.
 * This interface provides comprehensive control over entity properties, positioning,
 * equipment, animations, and packet-level operations.
 *
 * <p>NMS entities can be spawned for specific players, allowing for player-specific
 * visibility, and are typically managed by an {@link EntityTracker} for automatic
 * viewer management based on range and custom visibility rules.</p>
 */
public interface NMSEntity {

    /**
     * Spawns the entity for a specific collection of players.
     * Only the specified players will be able to see this entity.
     *
     * @param players The collection of players who should see the entity
     */
    void spawn(Collection<Player> players);

    /**
     * Spawns the entity for all tracked players.
     * This typically includes all players within tracking range.
     */
    void spawn();

    /**
     * Despawns the entity for a specific collection of players.
     * The entity will be removed from view for these players only.
     *
     * @param players The collection of players for whom to despawn the entity
     */
    void despawn(Collection<Player> players);

    /**
     * Despawns the entity for all tracked players.
     * This removes the entity from view for all players who can currently see it.
     */
    void despawn();

    /**
     * Sends entity metadata packet to specific players.
     * This updates properties like custom name visibility, flags, and entity-specific data.
     *
     * @param players The collection of players to send the packet to
     */
    void sendEntityMetadataPacket(Collection<Player> players);

    /**
     * Sends entity metadata packet to all tracked players.
     * This updates properties like custom name visibility, flags, and entity-specific data.
     */
    void sendEntityMetadataPacket();

    /**
     * Sends entity attribute update packet to specific players.
     * This updates attributes like movement speed, max health, and attack damage.
     *
     * @param players The collection of players to send the packet to
     */
    void sendUpdateAttributesPacket(Collection<Player> players);

    /**
     * Sends entity attribute update packet to all tracked players.
     * This updates attributes like movement speed, max health, and attack damage.
     */
    void sendUpdateAttributesPacket();

    /**
     * Sends entity equipment packet to specific players.
     * This updates the items held in hands and armor pieces.
     *
     * @param players The collection of players to send the packet to
     */
    void sendEntityEquipmentPacket(Collection<Player> players);

    /**
     * Sends entity equipment packet to all tracked players.
     * This updates the items held in hands and armor pieces.
     */
    void sendEntityEquipmentPacket();

    /**
     * Sends an entity event packet to specific players.
     * Events include hurt animation, death animation, and entity-specific events.
     *
     * @param players The collection of players to send the packet to
     * @param eventId The event ID to trigger
     */
    void sendEntityEventPacket(Collection<Player> players, byte eventId);

    /**
     * Sends an entity event packet to all tracked players.
     * Events include hurt animation, death animation, and entity-specific events.
     *
     * @param eventId The event ID to trigger
     */
    void sendEntityEventPacket(byte eventId);

    /**
     * Sends an animation packet to specific players.
     * This triggers animations like swinging arm or taking damage.
     *
     * @param players  The collection of players to send the packet to
     * @param actionId The animation action ID
     */
    void sendAnimatePacket(Collection<Player> players, int actionId);

    /**
     * Sends an animation packet to all tracked players.
     * This triggers animations like swinging arm or taking damage.
     *
     * @param actionId The animation action ID
     */
    void sendAnimatePacket(int actionId);

    /**
     * Sends velocity update packet to specific players.
     * This applies the current velocity vector to the entity on client side.
     *
     * @param players The collection of players to send the packet to
     */
    void sendVelocityPacket(Collection<Player> players);

    /**
     * Sends velocity update packet to all tracked players.
     * This applies the current velocity vector to the entity on client side.
     */
    void sendVelocityPacket();

    /**
     * Sends passenger update packet to specific players.
     * This synchronizes which entities are riding this entity.
     *
     * @param players The collection of players to send the packet to
     */
    void sendSetPassengersPacket(Collection<Player> players);

    /**
     * Sends passenger update packet to all tracked players.
     * This synchronizes which entities are riding this entity.
     */
    void sendSetPassengersPacket();

    /**
     * Sends entity link packet to specific players.
     * This establishes relationships between entities like leash connections.
     *
     * @param players The collection of players to send the packet to
     */
    void sendSetEntityLinkPacket(Collection<Player> players);

    /**
     * Sends entity link packet to all tracked players.
     * This establishes relationships between entities like leash connections.
     */
    void sendSetEntityLinkPacket();

    /**
     * Sends head rotation packet to all tracked players.
     * This updates only the head yaw without affecting body rotation.
     */
    void sendRotateHeadPacket();

    /**
     * Sends head rotation packet to specific players.
     * This updates only the head yaw without affecting body rotation.
     *
     * @param players The collection of players to send the packet to
     */
    void sendRotateHeadPacket(Collection<Player> players);

    /**
     * Sends position and rotation update packet to all tracked players.
     * This teleports the entity to the specified location on client side.
     *
     * @param location The target location including position and rotation
     */
    void sendPositionRotationPacket(Location location);

    /**
     * Sets the entity's position and rotation.
     *
     * @param location The new location
     */
    void setPositionRotation(Location location);

    /**
     * Gets the entity's previous location.
     *
     * @return The previous location, or null if not set
     */
    @Nullable
    Location getPreviousLocation();

    /**
     * Sets entity's yaw rotation.
     *
     * @param yaw The yaw value in degrees
     */
    void setYaw(float yaw);

    /**
     * Sets entity's pitch rotation.
     *
     * @param pitch The pitch value in degrees
     */
    void setPitch(float pitch);

    /**
     * Gets the previous location and copies it to the provided location.
     *
     * @param loc The location to copy to
     * @return The location parameter with copied values
     */
    Location getPreviousLocation(Location loc);

    /**
     * Sets the previous location.
     * This is used for interpolation and movement tracking.
     *
     * @param location The location to set as previous
     */
    void setPreviousLocation(Location location);

    /**
     * Makes the entity follow a player.
     * The entity will navigate towards and track the player's position.
     *
     * @param player The player to follow
     */
    void follow(Player player);

    /**
     * Makes the entity navigate to a specific location with a set speed.
     * The entity will use pathfinding to reach the destination.
     *
     * @param location The target location
     * @param speed    The movement speed multiplier
     */
    void navigateTo(Location location, double speed);

    /**
     * Moves the entity by a vector.
     * This applies an immediate position offset.
     *
     * @param vector The movement vector
     */
    void move(Vector vector);

    /**
     * Moves the entity as a rideable mount.
     * This handles movement input from a player riding the entity.
     *
     * @param player The player riding the entity
     */
    void moveRide(Player player);

    /**
     * Gets the default ride speed.
     *
     * @return The ride speed multiplier
     */
    float getRideSpeed();

    /**
     * Sets the default ride speed.
     *
     * @param speed The ride speed multiplier
     */
    void setRideSpeed(float speed);

    /**
     * Sets the entity's velocity.
     *
     * @param x, y, z The velocity components
     */
    void setVelocity(double x, double y, double z);

    /**
     * Sets the entity's velocity.
     *
     * @param velocity The velocity vector
     */
    default void setVelocity(Vector velocity) {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    /**
     * Adds collision with a player.
     * The entity will physically interact with the specified player.
     *
     * @param player The player to add collision with
     */
    void addCollision(Player player);

    /**
     * Removes collision with a player.
     * The entity will pass through the specified player.
     *
     * @param player The player to remove collision with
     */
    void removeCollision(Player player);

    /**
     * Sets the item in the entity's main hand.
     *
     * @param itemStack The item to set, or null to clear
     */
    void setMainHand(@Nullable ItemStack itemStack);

    /**
     * Sets the item in the entity's offhand.
     *
     * @param itemStack The item to set, or null to clear
     */
    void setOffHand(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's helmet.
     *
     * @param itemStack The helmet item, or null to clear
     */
    void setHelmet(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's chestplate.
     *
     * @param itemStack The chestplate item, or null to clear
     */
    void setChestplate(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's leggings.
     *
     * @param itemStack The leggings item, or null to clear
     */
    void setLeggings(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's boots.
     *
     * @param itemStack The boots item, or null to clear
     */
    void setBoots(@Nullable ItemStack itemStack);

    /**
     * Sets the head pose of an armor stand.
     * All angles are in degrees.
     *
     * @param x X rotation (pitch)
     * @param y Y rotation (yaw)
     * @param z Z rotation (roll)
     */
    void setHeadPose(float x, float y, float z);

    /**
     * Sets the body pose of an armor stand.
     * All angles are in degrees.
     *
     * @param x X rotation (pitch)
     * @param y Y rotation (yaw)
     * @param z Z rotation (roll)
     */
    void setBodyPose(float x, float y, float z);

    /**
     * Sets the left arm pose of an armor stand.
     * All angles are in degrees.
     *
     * @param x X rotation (pitch)
     * @param y Y rotation (yaw)
     * @param z Z rotation (roll)
     */
    void setLeftArmPose(float x, float y, float z);

    /**
     * Sets the right arm pose of an armor stand.
     * All angles are in degrees.
     *
     * @param x X rotation (pitch)
     * @param y Y rotation (yaw)
     * @param z Z rotation (roll)
     */
    void setRightArmPose(float x, float y, float z);

    /**
     * Sets the left leg pose of an armor stand.
     * All angles are in degrees.
     *
     * @param x X rotation (pitch)
     * @param y Y rotation (yaw)
     * @param z Z rotation (roll)
     */
    void setLeftLegPose(float x, float y, float z);

    /**
     * Sets the right leg pose of an armor stand.
     * All angles are in degrees.
     *
     * @param x X rotation (pitch)
     * @param y Y rotation (yaw)
     * @param z Z rotation (roll)
     */
    void setRightLegPose(float x, float y, float z);

    /**
     * Sets the entity's custom name.
     * The custom name appears above the entity when visible.
     *
     * @param component The custom name component, or null to remove
     */
    void setCustomName(@Nullable Component component);

    /**
     * Sets the leash holder for this entity.
     * Creates a visual leash connection between this entity and the holder.
     *
     * @param holder The entity to hold the leash, or null to remove leash
     */
    void setLeashHolder(@Nullable Entity holder);

    /**
     * Sets whether a creeper is powered.
     * When powered, the aura shield is visible.
     *
     * @param powered True to power the creeper
     */
    void setCreeperPowered(boolean powered);

    /**
     * Sets whether a creeper is ignited.
     * When ignited, the creeper begins its explosion countdown.
     *
     * @param ignited True to ignite the creeper
     */
    void setCreeperIgnited(boolean ignited);

    /**
     * Sets hurt ticks for the entity.
     *
     * @param hurtTicks The number of hurt ticks
     */
    void setHurtTicks(int hurtTicks);

    /**
     * Sets a pose for the entity.
     *
     * @param pose The pose
     */
    void setPose(Pose pose);

    /**
     * Sets whether the entity clips through blocks.
     *
     * @param noClip True to disable block collision
     */
    void setNoClip(boolean noClip);

    /**
     * Sets the entity's item stack (for item entities).
     *
     * @param itemStack The item stack
     */
    void setEntityItemStack(ItemStack itemStack);

    /**
     * Sets whether a horse is standing (rearing).
     *
     * @param standing True to make the horse stand
     */
    void setHorseStanding(boolean standing);

    /**
     * Sets a guardian's attack target.
     * This creates the laser beam effect pointing at the target.
     *
     * @param targetId The target entity ID, or 0 to remove target
     */
    void setGuardianTarget(int targetId);

    /**
     * Removes pathfinding from this entity.
     * The entity will no longer automatically navigate or move on its own.
     */
    void removePathfinder();

    /**
     * Gets the underlying NMS entity object.
     * This returns the version-specific Minecraft server entity instance.
     *
     * @return The NMS entity object
     */
    Object getNMSEntity();

    /**
     * Gets the entity's unique network ID.
     *
     * @return The entity ID used in packets
     */
    int getId();

    /**
     * Gets the Bukkit entity wrapper if this NMS entity is backed by a real entity.
     *
     * @return The Bukkit entity, or null if this is a virtual entity
     */
    @Nullable
    Entity getBukkitEntity();

    /**
     * Gets the entity's group tracker.
     * The tracker manages automatic spawning and despawning based on player proximity.
     *
     * @return The group tracker, or null if not tracked
     */
    @Nullable
    EntityTracker getTracker();
}