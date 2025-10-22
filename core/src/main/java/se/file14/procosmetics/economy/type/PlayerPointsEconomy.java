package se.file14.procosmetics.economy.type;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyHookException;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

public class PlayerPointsEconomy implements EconomyProvider {

    private PlayerPointsAPI api;

    @Override
    public String getPlugin() {
        return "PlayerPoints";
    }

    @Override
    public void hook(ProCosmetics plugin) throws EconomyHookException {
        api = PlayerPoints.getInstance().getAPI();

        if (api == null) {
            throw new EconomyHookException("PlayerPoints API was null! Is it installed?");
        }
    }

    @Override
    public int getCoins(User user) {
        return api.look(user.getUniqueId());
    }

    @Override
    public CompletableFuture<BooleanIntPair> getCoinsAsync(User user) {
        return CompletableFuture.completedFuture(BooleanIntPair.of(true, getCoins(user)));
    }

    @Override
    public CompletableFuture<Boolean> addCoinsAsync(User user, int amount) {
        return CompletableFuture.completedFuture(api.give(user.getUniqueId(), amount));
    }

    @Override
    public CompletableFuture<Boolean> setCoinsAsync(User user, int amount) {
        return CompletableFuture.completedFuture(api.set(user.getUniqueId(), amount));
    }

    @Override
    public CompletableFuture<Boolean> removeCoinsAsync(User user, int amount) {
        return CompletableFuture.completedFuture(api.take(user.getUniqueId(), amount));
    }
}