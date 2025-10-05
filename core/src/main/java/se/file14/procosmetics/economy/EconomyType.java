package se.file14.procosmetics.economy;

import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.economy.type.BuiltInEconomy;
import se.file14.procosmetics.economy.type.PlayerPointsEconomy;
import se.file14.procosmetics.economy.type.VaultEconomy;

import java.util.function.Function;

public enum EconomyType {

    BUILT_IN("Built-In", BuiltInEconomy::new),
    VAULT("Vault", plugin -> new VaultEconomy()),
    PLAYER_POINTS("Player Points", plugin -> new PlayerPointsEconomy()),
    CUSTOM("Custom", null);

    private final String name;
    private final Function<ProCosmetics, EconomyProvider> factory;

    EconomyType(String name, Function<ProCosmetics, EconomyProvider> factory) {
        this.name = name;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public EconomyProvider create(ProCosmetics plugin) {
        return factory != null ? factory.apply(plugin) : null;
    }
}