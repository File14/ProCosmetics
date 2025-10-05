package se.file14.procosmetics.cosmetic.particleeffect;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffect;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.rarity.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class ParticleEffectTypeImpl extends CosmeticTypeImpl<ParticleEffectType, ParticleEffectBehavior> implements ParticleEffectType {

    private final int repeatDelay;

    public ParticleEffectTypeImpl(String key,
                                  CosmeticCategory<ParticleEffectType, ParticleEffectBehavior, ?> category,
                                  Supplier<ParticleEffectBehavior> behaviorFactory,
                                  boolean enabled,
                                  boolean findable,
                                  boolean purchasable,
                                  int cost,
                                  CosmeticRarity rarity,
                                  ItemStack itemStack,
                                  int repeatDelay) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.repeatDelay = repeatDelay;
    }

    @Override
    protected ParticleEffect createInstance(ProCosmeticsPlugin plugin, User user, ParticleEffectBehavior behavior) {
        return new ParticleEffectImpl(plugin, user, this, behavior);
    }

    @Override
    public int getRepeatDelay() {
        return repeatDelay;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<ParticleEffectType, ParticleEffectBehavior, ParticleEffectType.Builder> implements ParticleEffectType.Builder {

        private int repeatDelay = 1;

        public BuilderImpl(String key, CosmeticCategory<ParticleEffectType, ParticleEffectBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected ParticleEffectType.Builder self() {
            return this;
        }

        @Override
        public ParticleEffectType.Builder repeatDelay(int repeatDelay) {
            this.repeatDelay = repeatDelay;
            return this;
        }

        @Override
        public ParticleEffectType build() {
            return new ParticleEffectTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    repeatDelay
            );
        }
    }
}