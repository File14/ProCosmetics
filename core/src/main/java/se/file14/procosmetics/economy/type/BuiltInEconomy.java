package se.file14.procosmetics.economy.type;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyFailureException;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

public class BuiltInEconomy implements EconomyProvider {

    private final ProCosmetics plugin;

    public BuiltInEconomy(ProCosmetics plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPlugin() {
        return plugin.getJavaPlugin().getName();
    }

    @Override
    public void hook(ProCosmetics plugin) throws EconomyFailureException {
    }

    @Override
    public int getCoins(User user) {
        return user.getCoins();
    }

    @Override
    public CompletableFuture<BooleanIntPair> getCoinsAsync(User user) {
        return CompletableFuture.completedFuture(BooleanIntPair.of(true, getCoins(user)));
    }

    @Override
    public CompletableFuture<Boolean> addCoinsAsync(User user, int amount) {
        return plugin.getDatabase().addCoinsAsync(user, amount).thenApply(BooleanIntPair::leftBoolean);
    }

    @Override
    public CompletableFuture<Boolean> setCoinsAsync(User user, int amount) {
        return plugin.getDatabase().setCoinsAsync(user, amount).thenApply(BooleanIntPair::leftBoolean);
    }

    @Override
    public CompletableFuture<Boolean> removeCoinsAsync(User user, int amount) {
        return plugin.getDatabase().removeCoinsAsync(user, amount).thenApply(BooleanIntPair::leftBoolean);
    }
}
