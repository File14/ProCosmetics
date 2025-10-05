package se.file14.procosmetics.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * Provides static access to the ProCosmetics API instance.
 * This class serves as a singleton provider for accessing the main plugin API
 * from external plugins or internal components.
 *
 * <p>Example usage:
 * <pre>{@code
 * ProCosmetics api = ProCosmeticsProvider.get();
 * UserManager userManager = api.getUserManager();
 * }</pre>
 */
public final class ProCosmeticsProvider {

    private static ProCosmetics plugin;

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws UnsupportedOperationException always, as this class cannot be instantiated
     */
    private ProCosmeticsProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Gets the ProCosmetics API instance.
     *
     * @return The ProCosmetics instance
     * @throws IllegalStateException if ProCosmetics has not been registered yet
     */
    public static ProCosmetics get() {
        if (plugin == null) {
            throw new IllegalStateException("ProCosmetics is not loaded yet!");
        }
        return plugin;
    }

    @ApiStatus.Internal
    public static void register(ProCosmetics plugin) {
        ProCosmeticsProvider.plugin = plugin;
    }

    @ApiStatus.Internal
    public static void unregister() {
        plugin = null;
    }
}