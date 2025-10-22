package se.file14.procosmetics.api.cosmetic.pet;

import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.cosmetic.Cosmetic;

/**
 * Represents a pet cosmetic instance associated with a user.
 * Pets are companion entities that follow the player around,
 * providing decorative companionship.
 */
public interface Pet extends Cosmetic<PetType, PetBehavior> {

    /**
     * Spawns the pet at its default or predefined location.
     */
    @ApiStatus.Internal
    void spawn();

    /**
     * Spawns the pet at the specified location.
     *
     * @param location the world location where the pet should be spawned
     */
    @ApiStatus.Internal
    void spawn(Location location);
}
