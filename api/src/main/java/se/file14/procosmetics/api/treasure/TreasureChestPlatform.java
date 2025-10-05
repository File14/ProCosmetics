package se.file14.procosmetics.api.treasure;

import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import se.file14.procosmetics.api.user.User;

import java.util.List;

public interface TreasureChestPlatform {

    @ApiStatus.Internal
    void build();

    @ApiStatus.Internal
    void destroy();

    int getId();

    Location getCenter();

    List<Location> getChestLocations();

    User getUser();

    @ApiStatus.Internal
    void setUser(User user);

    boolean isInUse();
}