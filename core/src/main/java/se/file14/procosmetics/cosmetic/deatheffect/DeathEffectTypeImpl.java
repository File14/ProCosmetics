package se.file14.procosmetics.cosmetic.deatheffect;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffect;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class DeathEffectTypeImpl extends CosmeticTypeImpl<DeathEffectType, DeathEffectBehavior> implements DeathEffectType {

    public DeathEffectTypeImpl(String key,
                               CosmeticCategory<DeathEffectType, DeathEffectBehavior, ?> category,
                               Supplier<DeathEffectBehavior> behaviorFactory,
                               boolean enabled,
                               boolean findable,
                               boolean purchasable,
                               int cost,
                               CosmeticRarity rarity,
                               ItemStack itemStack) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
    }

    @Override
    protected DeathEffect createInstance(ProCosmeticsPlugin plugin, User user, DeathEffectBehavior behavior) {
        return new DeathEffectImpl(plugin, user, this, behavior);
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<DeathEffectType, DeathEffectBehavior, DeathEffectType.Builder> implements DeathEffectType.Builder {

        public BuilderImpl(String key, CosmeticCategory<DeathEffectType, DeathEffectBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected DeathEffectType.Builder self() {
            return this;
        }

        @Override
        public DeathEffectType build() {
            return new DeathEffectTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack
            );
        }
    }
}