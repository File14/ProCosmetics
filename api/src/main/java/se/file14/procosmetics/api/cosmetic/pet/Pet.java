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

    @ApiStatus.Internal
    void spawn();

    @ApiStatus.Internal
    void spawn(Location location);
}
