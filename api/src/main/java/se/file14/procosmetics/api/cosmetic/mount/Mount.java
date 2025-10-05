package se.file14.procosmetics.api.cosmetic.mount;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.nms.NMSEntity;

/**
 * Represents a mount cosmetic instance associated with a user.
 * Mounts are rideable entities that players can spawn and ride.
 */
public interface Mount extends Cosmetic<MountType, MountBehavior> {

    @ApiStatus.Internal
    void spawn();

    @ApiStatus.Internal
    void spawn(Location location);

    /**
     * Gets the Bukkit entity representing this mount.
     *
     * @return the mount's Bukkit entity
     */
    Entity getEntity();

    /**
     * Gets the NMS entity representing this mount.
     *
     * @return the mount's NMS entity
     */
    NMSEntity getNMSEntity();
}
