package se.file14.procosmetics.api.treasure;

import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

public interface TreasureChestManager {

    @Nullable
    TreasureChestPlatform getPlatform(Location location);

    @Nullable
    TreasureChestPlatform getPlatform(int id);

    @Nullable
    TreasureChest getTreasureChest(String key);

    List<TreasureChest> getTreasureChests();
}
