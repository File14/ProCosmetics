package se.file14.procosmetics.api.cosmetic.registry;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

import java.util.Collection;

public interface CosmeticRegistry<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>,
        U extends CosmeticType.Builder<T, B, U>> {

    /**
     * Registers a new cosmetic type with all data provided programmatically
     *
     * @param type Builder containing all the cosmetic data
     * @return The registered cosmetic type
     */
    T register(T type);

    /**
     * Creates a new builder for this cosmetic type
     * Builder can optionally read base data from config
     *
     * @param key The unique key for this cosmetic type
     * @return A builder instance
     */
    U builder(String key);

    /**
     * Gets a cosmetic type by its key
     *
     * @param key The key to look up
     * @return The cosmetic type or null if not found
     */
    T getType(String key);

    T getEnabledType(String key);

    /**
     * Gets all registered cosmetic types
     *
     * @return Collection of all cosmetic types
     */
    Collection<T> getTypes();

    /**
     * Gets all registered enabled cosmetic types
     *
     * @return Collection of all enabled cosmetic types
     */
    Collection<T> getEnabledTypes();

    boolean isRegistered(String key);

    /**
     * Unregisters a cosmetic type
     *
     * @param key The key of the type to unregister
     * @return true if the type was found and removed
     */
    boolean unregister(String key);

    void clear();
}