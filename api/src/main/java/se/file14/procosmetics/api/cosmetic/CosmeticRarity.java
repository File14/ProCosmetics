package se.file14.procosmetics.api.cosmetic;

import org.bukkit.FireworkEffect;
import se.file14.procosmetics.api.locale.Translator;

/**
 * Represents the rarity tier of a cosmetic.
 *
 * @see se.file14.procosmetics.api.cosmetic.registry.CosmeticRarityRegistry
 */
public interface CosmeticRarity {

    /**
     * Gets the unique key of this rarity.
     *
     * @return the rarity key
     */
    String getKey();

    /**
     * Gets the localized display name of this rarity.
     *
     * @param translator the {@link Translator} used to resolve localization
     * @return the localized rarity name
     */
    String getName(Translator translator);

    String getPrimaryColor();

    String getSecondaryColor();

    /**
     * Gets the number of detonations for the firework effect associated with this rarity.
     *
     * @return the number of firework detonations
     */
    int getDetonations();

    /**
     * Gets the interval between firework detonations in ticks.
     *
     * @return the interval between detonations, in ticks
     */
    int getTickInterval();

    /**
     * Gets the firework effect associated with this rarity.
     *
     * @return the {@link FireworkEffect} representing this rarity’s effect
     */
    FireworkEffect getFireworkEffect();
}
