package se.file14.procosmetics.cosmetic.morph;


import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.morph.Morph;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.rarity.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class MorphTypeImpl extends CosmeticTypeImpl<MorphType, MorphBehavior> implements MorphType {

    private final double cooldown;
    private final EntityType entityType;
    private final boolean ability;

    public MorphTypeImpl(String key,
                         CosmeticCategory<MorphType, MorphBehavior, ?> category,
                         Supplier<MorphBehavior> behaviorFactory,
                         boolean enabled,
                         boolean findable,
                         boolean purchasable,
                         int cost,
                         CosmeticRarity rarity,
                         ItemStack itemStack,
                         double cooldown,
                         EntityType entityType,
                         boolean ability) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.cooldown = cooldown;
        this.entityType = entityType;
        this.ability = ability;
    }

    @Override
    protected Morph createInstance(ProCosmeticsPlugin plugin, User user, MorphBehavior behavior) {
        return new MorphImpl(plugin, user, this, behavior);
    }

    @Override
    public double getCooldown() {
        return cooldown;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean hasAbility() {
        return ability;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<MorphType, MorphBehavior, MorphType.Builder> implements MorphType.Builder {

        private double cooldown;
        private EntityType entityType;
        private boolean ability = false;

        public BuilderImpl(String key, CosmeticCategory<MorphType, MorphBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected MorphType.Builder self() {
            return this;
        }

        @Override
        public MorphType.Builder readFromConfig() {
            super.readFromConfig();

            Config config = category.getConfig();
            String path = getPath();

            cooldown = Math.max(0.1d, config.getDouble(path + "cooldown"));
            ability = config.getBoolean(path + "ability");

            return self();
        }

        @Override
        public MorphType.Builder cooldown(double cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        @Override
        public MorphType.Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        @Override
        public MorphType.Builder ability(boolean ability) {
            this.ability = ability;
            return this;
        }

        @Override
        public MorphType build() {
            return new MorphTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    cooldown,
                    entityType,
                    ability
            );
        }
    }
}