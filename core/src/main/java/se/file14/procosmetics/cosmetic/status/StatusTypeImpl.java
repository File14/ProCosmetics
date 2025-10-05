package se.file14.procosmetics.cosmetic.status;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.status.Status;
import se.file14.procosmetics.api.cosmetic.status.StatusBehavior;
import se.file14.procosmetics.api.cosmetic.status.StatusType;
import se.file14.procosmetics.api.rarity.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Function;
import java.util.function.Supplier;

public class StatusTypeImpl extends CosmeticTypeImpl<StatusType, StatusBehavior> implements StatusType {

    private final long refreshInterval;
    private final Function<User, Component> textProvider;

    public StatusTypeImpl(String key,
                          CosmeticCategory<StatusType, StatusBehavior, ?> category,
                          Supplier<StatusBehavior> behaviorFactory,
                          boolean enabled,
                          boolean findable,
                          boolean purchasable,
                          int cost,
                          CosmeticRarity rarity,
                          ItemStack itemStack,
                          long refreshInterval,
                          Function<User, Component> textProvider) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.refreshInterval = refreshInterval;
        this.textProvider = textProvider;
    }

    @Override
    protected Status createInstance(ProCosmeticsPlugin plugin, User user, StatusBehavior behavior) {
        return new StatusImpl(plugin, user, this, behavior);
    }

    @Override
    public long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    public Function<User, Component> getTextProvider() {
        return textProvider;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<StatusType, StatusBehavior, StatusType.Builder> implements StatusType.Builder {

        private long refreshInterval = -1L;
        private Function<User, Component> textProvider;

        public BuilderImpl(String key, CosmeticCategory<StatusType, StatusBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected StatusType.Builder self() {
            return this;
        }

        @Override
        public StatusType.Builder readFromConfig() {
            super.readFromConfig();

            refreshInterval = category.getConfig().getInt("refresh_interval", false);
            textProvider = user -> user.translate("cosmetic." + category.getKey() + "." + key + ".tag");

            return this;
        }

        @Override
        public StatusType.Builder refreshInterval(long refreshInterval) {
            this.refreshInterval = refreshInterval;
            return this;
        }

        @Override
        public StatusType.Builder textProvider(Function<User, Component> textProvider) {
            this.textProvider = textProvider;
            return this;
        }

        @Override
        public StatusType build() {
            return new StatusTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    refreshInterval,
                    textProvider
            );
        }
    }
}