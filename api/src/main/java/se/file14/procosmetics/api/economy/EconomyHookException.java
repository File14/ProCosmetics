package se.file14.procosmetics.api.economy;

/**
 * Thrown when the plugin fails to hook into an external economy system.
 * <p>
 * This exception indicates that an attempt to connect or initialize
 * an economy provider was unsuccessful.
 *
 * @see se.file14.procosmetics.api.economy.EconomyProvider
 */
public class EconomyHookException extends Exception {

    /**
     * Constructs a new {@link EconomyHookException} with the specified detail message.
     *
     * @param message a message describing the reason for the failure
     */
    public EconomyHookException(String message) {
        super(message);
    }
}
