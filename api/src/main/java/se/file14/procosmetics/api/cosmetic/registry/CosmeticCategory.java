package se.file14.procosmetics.api.cosmetic.registry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.menu.CosmeticMenu;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;

public interface CosmeticCategory<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>,
        U extends CosmeticType.Builder<T, B, U>> {

    String getKey();

    boolean isEnabled();

    String getPermission();

    String getPurchasePermission();

    CosmeticRegistry<T, B, U> getCosmeticRegistry();

    int getUnlockedCosmetics(Player player);

    CosmeticMenu<T> createMenu(ProCosmetics plugin, User user);

    @Nullable
    Config getConfig();

    interface Builder {

        Builder key(String key);

        Builder registry(CosmeticRegistry<?, ?, ?> registry);

        Builder enabled(boolean enabled);

        Builder permission(String permission);

        Builder purchasePermission(String permission);

        Builder menuItem(ItemStack item);

        Builder menuItem(ItemBuilder itemBuilder);

        Builder menuSlot(int slot);

        Builder displayName(String displayName);

        Builder description(String... description);

        CosmeticCategory<?, ?, ?> build();

        static Builder create() {
            throw new UnsupportedOperationException("Must be implemented by plugin");
        }
    }
}