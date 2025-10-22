package se.file14.procosmetics.api.cosmetic.arroweffect;

import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.Cosmetic;

import javax.annotation.Nullable;

/**
 * Represents an arrow effect cosmetic instance associated with a user.
 * Arrow effects provide visual enhancements to projectiles fired by players,
 * such as particle trails during flight and upon impact.
 */
public interface ArrowEffect extends Cosmetic<ArrowEffectType, ArrowEffectBehavior> {

    /**
     * Gets the current projectile entity associated with this arrow effect.
     * This represents the arrow or other projectile that is currently being tracked.
     *
     * @return the projectile entity, or null if no projectile is currently active
     */
    @Nullable
    Entity getProjectile();
}
