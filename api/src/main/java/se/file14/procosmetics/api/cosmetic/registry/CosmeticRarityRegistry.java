package se.file14.procosmetics.api.cosmetic.registry;

import se.file14.procosmetics.api.cosmetic.CosmeticRarity;

import javax.annotation.Nullable;

public interface CosmeticRarityRegistry {

    /**
     * Loads all rarities from the configuration.
     */
    void load();

    /**
     * Registers a cosmetic rarity.
     *
     * @param rarity the rarity to register
     */
    void register(CosmeticRarity rarity);

    /**
     * Gets a rarity by its key.
     *
     * @param key the rarity key
     * @return the rarity, or null if not found
     */
    @Nullable
    CosmeticRarity get(String key);

    /**
     * Gets a rarity by its key, returning a fallback rarity if not found.
     *
     * @param key the rarity key
     * @return the rarity, or the fallback rarity if not found
     */
    CosmeticRarity getSafely(String key);

    /**
     * Gets the fallback rarity used when a rarity is not found.
     *
     * @return the fallback rarity
     */
    CosmeticRarity getFallbackRarity();
}