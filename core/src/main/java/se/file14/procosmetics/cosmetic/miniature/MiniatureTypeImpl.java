package se.file14.procosmetics.cosmetic.miniature;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.miniature.Miniature;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class MiniatureTypeImpl extends CosmeticTypeImpl<MiniatureType, MiniatureBehavior> implements MiniatureType {

    private final boolean invisible;
    private final boolean arms;

    public MiniatureTypeImpl(String key,
                             CosmeticCategory<MiniatureType, MiniatureBehavior, ?> category,
                             Supplier<MiniatureBehavior> behaviorFactory,
                             boolean enabled,
                             boolean findable,
                             boolean purchasable,
                             int cost,
                             CosmeticRarity rarity,
                             ItemStack itemStack,
                             boolean invisible,
                             boolean arms) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.invisible = invisible;
        this.arms = arms;
    }

    @Override
    protected Miniature createInstance(ProCosmeticsPlugin plugin, User user, MiniatureBehavior behavior) {
        return new MiniatureImpl(plugin, user, this, behavior);
    }

    @Override
    public boolean hasInvisibility() {
        return invisible;
    }

    @Override
    public boolean hasArms() {
        return arms;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<MiniatureType, MiniatureBehavior, MiniatureType.Builder> implements MiniatureType.Builder {

        private boolean invisible = false;
        private boolean arms = true;

        public BuilderImpl(String key, CosmeticCategory<MiniatureType, MiniatureBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected MiniatureType.Builder self() {
            return this;
        }

        @Override
        public MiniatureType.Builder invisible(boolean invisible) {
            this.invisible = invisible;
            return this;
        }

        @Override
        public MiniatureType.Builder arms(boolean arms) {
            this.arms = arms;
            return this;
        }

        @Override
        public MiniatureType build() {
            return new MiniatureTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    invisible,
                    arms
            );
        }
    }
}