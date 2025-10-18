package se.file14.procosmetics.cosmetic.arroweffect;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffect;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class ArrowEffectTypeImpl extends CosmeticTypeImpl<ArrowEffectType, ArrowEffectBehavior> implements ArrowEffectType {

    public ArrowEffectTypeImpl(String key,
                               CosmeticCategory<ArrowEffectType, ArrowEffectBehavior, ?> category,
                               Supplier<ArrowEffectBehavior> behaviorFactory,
                               boolean enabled,
                               boolean findable,
                               boolean purchasable,
                               int cost,
                               CosmeticRarity rarity,
                               ItemStack itemStack) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
    }

    @Override
    protected ArrowEffect createInstance(ProCosmeticsPlugin plugin, User user, ArrowEffectBehavior behavior) {
        return new ArrowEffectImpl(plugin, user, this, behavior);
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<ArrowEffectType, ArrowEffectBehavior, ArrowEffectType.Builder> implements ArrowEffectType.Builder {

        public BuilderImpl(String key, CosmeticCategory<ArrowEffectType, ArrowEffectBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected ArrowEffectType.Builder self() {
            return this;
        }

        @Override
        public ArrowEffectType build() {
            return new ArrowEffectTypeImpl(key,
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