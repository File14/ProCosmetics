package se.file14.procosmetics.api.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
     * Spawns the entity for a specific player.
     *
     * @param player The player to spawn the entity for
     */
    void spawn(Player player);

    /**
     * Despawns the entity for specific players.
     *
     * @param players The players to despawn the entity for
     */
    void despawn(Player... players);

    /**
     * Gets the Bukkit entity representation.
     *
     * @return The Bukkit entity, or null if not spawned
     */
    @Nullable
    Entity getBukkitEntity();

    /**
     * Gets the entity's unique ID.
     *
     * @return The entity ID
     */
    int getId();

    /**
     * Sets the entity's position and rotation.
     *
     * @param location The new location
     */
    void setPosition(Location location);

    /**
     * Gets the entity's previous location.
     *
     * @return The previous location, or null if not set
     */
    @Nullable
    Location getPreviousLocation();

    /**
     * Sets the entity's velocity.
     *
     * @param velocity The velocity vector
     */
    void setVelocity(Vector velocity);

    /**
     * Makes the entity follow a player.
     *
     * @param player The player to follow
     */
    void follow(Player player);

    /**
     * Makes the entity go to a specific location.
     *
     * @param location The target location
     * @param speed    The movement speed
     */
    void goToLocation(Location location, double speed);

    /**
     * Sets the item in the entity's main hand.
     *
     * @param itemStack The item to set
     */
    void setMainHand(@Nullable ItemStack itemStack);

    /**
     * Sets the item in the entity's offhand.
     *
     * @param itemStack The item to set
     */
    void setOffHand(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's helmet.
     *
     * @param itemStack The helmet item
     */
    void setHelmet(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's chestplate.
     *
     * @param itemStack The chestplate item
     */
    void setChestplate(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's leggings.
     *
     * @param itemStack The leggings item
     */
    void setLeggings(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's boots.
     *
     * @param itemStack The boots item
     */
    void setBoots(@Nullable ItemStack itemStack);

    /**
     * Sets the entity's custom name.
     *
     * @param component The custom name, or null to remove
     */
    void setCustomName(@Nullable Component component);

    /**
     * Sets whether the entity is invisible.
     *
     * @param invisible True to make invisible
     */
    void setInvisible(boolean invisible);

    /**
     * Sets whether the entity is a baby.
     *
     * @param baby True to make baby
     */
    void setBaby(boolean baby);

    /**
     * Sets whether the entity is sneaking.
     *
     * @param sneaking True to make sneaking
     */
    void setSneaking(boolean sneaking);

    /**
     * Sets whether the entity has gravity.
     *
     * @param gravity True to enable gravity
     */
    void setGravity(boolean gravity);

    /**
     * Sets whether the entity has AI.
     *
     * @param ai True to enable AI
     */
    void setAI(boolean ai);

    /**
     * Sets whether the entity is invulnerable.
     *
     * @param invulnerable True to make invulnerable
     */
    void setInvulnerable(boolean invulnerable);

    /**
     * Sets whether the entity is silent.
     *
     * @param silent True to make silent
     */
    void setSilent(boolean silent);

    /**
     * Sets whether the entity is persistent.
     *
     * @param persistent True to make persistent
     */
    void setPersistent(boolean persistent);

    /**
     * Removes collision with a player.
     *
     * @param player The player to remove collision with
     */
    void removeCollision(Player player);

    /**
     * Adds collision with a player.
     *
     * @param player The player to add collision with
     */
    void addCollision(Player player);

    /**
     * Gets the entity's group tracker.
     *
     * @return The group tracker, or null if not applicable
     */
    @Nullable
    EntityTracker getTracker();

    /**
     * Gets all players currently viewing this entity.
     *
     * @return Collection of viewing players
     */
    Collection<Player> getViewers();

    /**
     * Sets the size of a slime entity.
     *
     * @param size The slime size
     */
    void setSlimeSize(int size);

    /**
     * Sets whether a creeper is ignited.
     *
     * @param ignited True to ignite
     */
    void setCreeperIgnited(boolean ignited);

    /**
     * Sets whether a creeper is powered.
     *
     * @param powered True to power
     */
    void setCreeperPowered(boolean powered);

    /**
     * Gets whether a creeper is powered.
     *
     * @return True if powered
     */
    boolean isCreeperPowered();

    /**
     * Sets whether a bat is sleeping.
     *
     * @param sleeping True to make sleeping
     */
    void setBatSleeping(boolean sleeping);

    /**
     * Sets a guardian's attack target.
     *
     * @param targetId The target entity ID
     */
    void setGuardianTarget(int targetId);

    // === Armor Stand Specific ===

    /**
     * Sets whether an armor stand is small.
     *
     * @param small True to make small
     */
    void setArmorStandSmall(boolean small);

    /**
     * Sets whether an armor stand has a base plate.
     *
     * @param basePlate True to show base plate
     */
    void setArmorStandBasePlate(boolean basePlate);

    /**
     * Sets whether an armor stand has arms.
     *
     * @param arms True to show arms
     */
    void setArmorStandArms(boolean arms);

    /**
     * Sets whether an armor stand is a marker.
     *
     * @param marker True to make marker
     */
    void setArmorStandMarker(boolean marker);

    /**
     * Sets the head pose of an armor stand.
     *
     * @param x X rotation
     * @param y Y rotation
     * @param z Z rotation
     */
    void setHeadPose(float x, float y, float z);

    /**
     * Sets the body pose of an armor stand.
     *
     * @param x X rotation
     * @param y Y rotation
     * @param z Z rotation
     */
    void setBodyPose(float x, float y, float z);

    /**
     * Sets the left arm pose of an armor stand.
     *
     * @param x X rotation
     * @param y Y rotation
     * @param z Z rotation
     */
    void setLeftArmPose(float x, float y, float z);

    /**
     * Sets the right arm pose of an armor stand.
     *
     * @param x X rotation
     * @param y Y rotation
     * @param z Z rotation
     */
    void setRightArmPose(float x, float y, float z);

    /**
     * Sets the left leg pose of an armor stand.
     *
     * @param x X rotation
     * @param y Y rotation
     * @param z Z rotation
     */
    void setLeftLegPose(float x, float y, float z);

    /**
     * Sets the right leg pose of an armor stand.
     *
     * @param x X rotation
     * @param y Y rotation
     * @param z Z rotation
     */
    void setRightLegPose(float x, float y, float z);

    /**
     * Plays an attack animation.
     */
    void playAttackAnimation();

    /**
     * Plays rabbit jump animation.
     */
    void playRabbitJumpAnimation();

    /**
     * Set hurt ticks.
     *
     * @param hurtTicks The number of hurt ticks
     */
    void setHurtTicks(int hurtTicks);

    /**
     * Mounts this entity on another entity.
     *
     * @param mount The entity to mount on
     */
    void mount(Entity mount);

    /**
     * Dismounts this entity.
     */
    void dismount();

    /**
     * Gets the default ride speed.
     *
     * @return The ride speed
     */
    float getRideSpeed();

    /**
     * Sets the default ride speed.
     *
     * @param speed The ride speed
     */
    void setRideSpeed(float speed);

    /**
     * Sets the leash holder for this entity.
     *
     * @param holder The entity to hold the leash
     */
    void setLeashHolder(@Nullable Entity holder);

    /**
     * Removes pathfinding from this entity.
     */
    void removePathfinder();

    /**
     * Cleans up the entity and removes it from all viewers.
     */
    void cleanup();

    // === Additional missing methods from implementation ===

    /**
     * Sets whether a zombie is a baby.
     *
     * @param baby True to make baby zombie
     */
    void setZombieBaby(boolean baby);

    /**
     * Sets entity's yaw rotation.
     *
     * @param yaw The yaw value
     */
    void setYaw(float yaw);

    /**
     * Sets entity's pitch rotation.
     *
     * @param pitch The pitch value
     */
    void setPitch(float pitch);

    /**
     * Moves the entity by a vector.
     *
     * @param vector The movement vector
     */
    void move(Vector vector);

    /**
     * Moves the entity as a rideable mount.
     *
     * @param player The player riding
     */
    void moveRide(Player player);

    /**
     * Sets the entity's velocity with individual components.
     *
     * @param x X velocity
     * @param y Y velocity
     * @param z Z velocity
     */
    void setVelocity(double x, double y, double z);

    /**
     * Sets the entity's position and rotation.
     *
     * @param location The new location
     */
    void setPositionRotation(Location location);

    /**
     * Sets whether the entity clips through blocks.
     *
     * @param noClip True to disable clipping
     */
    void setNoClip(boolean noClip);

    /**
     * Sets whether a donkey is standing.
     *
     * @param standing True to make standing
     */
    void setDonkeyStanding(boolean standing);

    /**
     * Sets the entity's item stack (for item entities).
     *
     * @param itemStack The item stack
     */
    void setEntityItemStack(ItemStack itemStack);

    /**
     * Gets the NMS entity object.
     *
     * @return The NMS entity
     */
    Object getNMSEntity();

    /**
     * Sends packets to all viewers of this entity.
     *
     * @param packets The packets to send
     */
    void sendPacketsToViewers(Object... packets);

    /**
     * Sends entity metadata packet to a specific player.
     *
     * @param player The player to send to
     */
    void sendEntityMetadataPacket(Player player);

    /**
     * Sends entity equipment packet to a specific player.
     *
     * @param player The player to send to
     */
    void sendEntityEquipmentPacket(Player player);

    /**
     * Sends entity equipment packet to all viewers.
     */
    void sendEntityEquipmentPacket();

    /**
     * Sends position and rotation packet.
     *
     * @param location The location
     */
    void sendPositionRotationPacket(Location location);

    /**
     * Sends metadata packet to all viewers.
     */
    void sendMetadataPacket();

    /**
     * Sends iron golem attack animation packet.
     */
    void sendIronGolemAttackAnimationPacket();

    /**
     * Sends entity animation packet.
     */
    void sendEntityAnimationPacket();

    /**
     * Sends leash holder packet to a specific player.
     *
     * @param player The player to send to
     */
    void sendLeashHolderPacket(Player player);

    /**
     * Sends velocity packet to a specific player.
     *
     * @param player The player to send to
     */
    void sendVelocityPacket(Player player);

    /**
     * Sends velocity packet to all viewers.
     */
    void sendVelocityPacket();

    /**
     * Sends mount packet to a specific player.
     *
     * @param player The player to send to
     */
    void sendMountPacket(Player player);

    /**
     * Sends head rotation packet to all viewers.
     */
    void sendHeadRotationPacket();

    /**
     * Sets the bounding box of the entity.
     *
     * @param boundingBox The bounding box
     */
    void setBoundingBox(BoundingBox boundingBox);

    /**
     * Gets the previous location and copies it to the provided location.
     *
     * @param loc The location to copy to
     * @return The location parameter
     */
    Location getPreviousLocation(Location loc);

    /**
     * Sets the previous location.
     *
     * @param location The location to set
     */
    void setPreviousLocation(Location location);

    /**
     * Inner class for bounding box data.
     */
    class BoundingBox {

        private final double x, y, z;

        public BoundingBox(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}