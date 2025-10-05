package se.file14.procosmetics.api.cosmetic.deatheffect;

import se.file14.procosmetics.api.cosmetic.Cosmetic;

/**
 * Represents a death effect cosmetic instance associated with a user.
 * Death effects play particle effects when the player dies.
 */
public interface DeathEffect extends Cosmetic<DeathEffectType, DeathEffectBehavior> {

}
