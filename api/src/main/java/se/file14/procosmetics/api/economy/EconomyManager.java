package se.file14.procosmetics.api.economy;

/**
 * Manages the economy provider used for cosmetic purchases and transactions.
 * This interface allows registration of custom economy providers and provides
 * access to the currently active economy system.
 */
public interface EconomyManager {

    /**
     * Registers an economy provider to be used for transactions.
     *
     * @param provider the economy provider to register
     */
    void register(EconomyProvider provider);

    /**
     * Gets the currently registered economy provider.
     *
     * @return the active economy provider
     */
    EconomyProvider getEconomyProvider();
}
