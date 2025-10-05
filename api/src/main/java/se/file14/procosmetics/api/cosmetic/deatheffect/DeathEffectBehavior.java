package se.file14.procosmetics.api.cosmetic.deatheffect;

import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;

public interface DeathEffectBehavior extends CosmeticBehavior<DeathEffectType> {

    void playEffect(CosmeticContext<DeathEffectType> context, Location location);

}