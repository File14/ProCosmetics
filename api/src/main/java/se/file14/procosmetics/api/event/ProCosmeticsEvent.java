package se.file14.procosmetics.api.event;

import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.ProCosmetics;

/**
 * A superinterface for all ProCosmetics events.
 */
public interface ProCosmeticsEvent {

    /**
     * Get the API instance this event was dispatched from.
     *
     * @return the api instance
     */
    ProCosmetics getPlugin();

    /**
     * Gets the handler list for this event.
     *
     * @return the {@link HandlerList} instance
     */
    HandlerList getHandlers();
}
