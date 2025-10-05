package se.file14.procosmetics.cosmetic.registry;

import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRegistry;
import se.file14.procosmetics.api.menu.CosmeticMenu;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;
import se.file14.procosmetics.menu.CosmeticMenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.Collection;
import java.util.function.BiFunction;

public class CosmeticCategoryImpl<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>,
        U extends CosmeticType.Builder<T, B, U>>
        implements CosmeticCategory<T, B, U> {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final String key;
    private final CosmeticRegistry<T, B, U> registry;
    private final Config config;
    private final boolean enabled;
    private final String permission;
    private final String purchasePermission;
    private final ItemBuilderImpl menuItem;
    private final TriFunction<ProCosmetics, User, CosmeticCategory<T, B, U>, CosmeticMenuImpl<T>> menuFactory;

    public CosmeticCategoryImpl(String key,
                                BiFunction<String, CosmeticCategory<T, B, U>, U> builderFactory,
                                TriFunction<ProCosmetics, User, CosmeticCategory<T, B, U>, CosmeticMenuImpl<T>> menuFactory) {
        this.key = key.toLowerCase();
        this.registry = new CosmeticRegistryImpl<>(this, builderFactory);
        this.config = PLUGIN.getConfigManager().register(key);
        this.enabled = config.getBoolean("enable");
        this.permission = "procosmetics.cosmetic." + key + ".*";
        this.purchasePermission = "procosmetics.purchase." + key + ".*";
        this.menuItem = new ItemBuilderImpl(PLUGIN.getConfigManager().getMainConfig(), "menu.main.items." + key);
        this.menuFactory = menuFactory;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public CosmeticRegistry<T, B, U> getCosmeticRegistry() {
        return registry;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getPurchasePermission() {
        return purchasePermission;
    }

    public ItemBuilderImpl getMenuItem() {
        return menuItem;
    }

    @Override
    public CosmeticMenu<T> createMenu(ProCosmetics plugin, User user) {
        return menuFactory.apply(plugin, user, this);
    }

    @Override
    public int getUnlockedCosmetics(Player player) {
        Collection<? extends CosmeticType<?, ?>> cosmeticTypes = registry.getEnabledTypes();

        if (player.hasPermission(CosmeticTypeImpl.PERMISSION_ALL_COSMETICS) || player.hasPermission(permission)) {
            return cosmeticTypes.size();
        }
        int i = 0;

        for (CosmeticType<?, ?> cosmeticType : cosmeticTypes) {
            if (player.hasPermission(cosmeticType.getPermission())) {
                i++;
            }
        }
        return i;
    }
}