package se.file14.procosmetics.api.cosmetic.registry;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

import java.util.Collection;

/**
 * Represents a registry that manages all cosmetic types within a specific category.
 * <p>
 * Each {@link se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory}
 * has its own {@code CosmeticRegistry} instance that manages its cosmetics.
 *
 * @param <T> the cosmetic type contained within this registry
 * @param <B> the behavior type associated with the cosmetic
 * @param <U> the builder type used to construct cosmetic types
 * @see CosmeticCategory
 * @see CosmeticType
 * @see CosmeticBehavior
 */
public interface CosmeticRegistry<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>,
        U extends CosmeticType.Builder<T, B, U>> {

    /**
     * Registers a new cosmetic type.
     *
     * @param type the cosmetic type to register
     * @return the registered cosmetic type
     */
    T register(T type);

    /**
     * Creates a new builder for this cosmetic type.
     * Builder can optionally read base data from config.
     *
     * @param key the unique key for this cosmetic type
     * @return a new builder instance
     */
    U builder(String key);

    /**
     * Gets a cosmetic type by its key.
     *
     * @param key the cosmetic key to look up
     * @return the cosmetic type, or {@code null} if not found
     */
    T getType(String key);

    /**
     * Retrieves a cosmetic type by its key only if it is currently enabled.
     *
     * @param key the cosmetic key to look up
     * @return the enabled cosmetic type, or {@code null} if not found or disabled
     */
    T getEnabledType(String key);

    /**
     * Gets all registered cosmetic types in this registry.
     *
     * @return a collection of all registered cosmetic types
     */
    Collection<T> getTypes();

    /**
     * Gets all registered and currently enabled cosmetic types in this registry.
     *
     * @return a collection of all enabled cosmetic types
     */
    Collection<T> getEnabledTypes();

    /**
     * Checks whether a cosmetic type with the given key is registered.
     *
     * @param key the cosmetic key to check
     * @return {@code true} if a cosmetic with the given key is registered
     */
    boolean isRegistered(String key);

    /**
     * Unregisters a cosmetic type
     *
     * @param key the key of the cosmetic type to unregister
     * @return {@code true} if the cosmetic type was found and removed
     */
    boolean unregister(String key);

    /**
     * Clears all registered cosmetic types from this registry.
     */
    void clear();
}
