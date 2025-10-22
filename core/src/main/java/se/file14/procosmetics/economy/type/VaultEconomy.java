package se.file14.procosmetics.economy.type;

import it.unimi.dsi.fastutil.booleans.BooleanIntPair;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.economy.EconomyHookException;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;

import java.util.concurrent.CompletableFuture;

public class VaultEconomy implements EconomyProvider {

    private Economy economy;

    @Override
    public String getPlugin() {
        return "Vault";
    }

    @Override
    public void hook(ProCosmetics plugin) throws EconomyHookException {
        RegisteredServiceProvider<Economy> ecoProvider = plugin.getJavaPlugin().getServer().getServicesManager().getRegistration(Economy.class);

        if (ecoProvider == null) {
            throw new EconomyHookException("Unable to detect an economy-provider to Vault! Using the built-in economy system for now.");
        }
        economy = ecoProvider.getProvider();
    }

    @Override
    public int getCoins(User user) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUniqueId());

        return (int) economy.getBalance(offlinePlayer);
    }

    @Override
    public CompletableFuture<BooleanIntPair> getCoinsAsync(User user) {
        return CompletableFuture.completedFuture(BooleanIntPair.of(true, getCoins(user)));
    }

    @Override
    public CompletableFuture<Boolean> addCoinsAsync(User user, int amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUniqueId());

        return CompletableFuture.completedFuture(economy.depositPlayer(offlinePlayer, amount).transactionSuccess());
    }

    @Override
    public CompletableFuture<Boolean> setCoinsAsync(User user, int amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUniqueId());
        double currentBalance = economy.getBalance(offlinePlayer);
        double difference = currentBalance - amount;
        boolean success;

        if (difference > 0) {
            success = economy.withdrawPlayer(offlinePlayer, difference).transactionSuccess();
        } else if (difference < 0) {
            success = economy.depositPlayer(offlinePlayer, -difference).transactionSuccess();
        } else {
            // Balance is already correct, no transaction needed
            success = true;
        }
        return CompletableFuture.completedFuture(success);
    }

    @Override
    public CompletableFuture<Boolean> removeCoinsAsync(User user, int amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUniqueId());

        if (!economy.has(offlinePlayer, amount)) {
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(economy.withdrawPlayer(offlinePlayer, amount).transactionSuccess());
    }
}