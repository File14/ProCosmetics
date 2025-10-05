package se.file14.procosmetics.api.cosmetic.balloon;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface BalloonBehavior extends CosmeticBehavior<BalloonType> {

    void onUpdate(CosmeticContext<BalloonType> context, Location location);
}
