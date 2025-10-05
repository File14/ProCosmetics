package se.file14.procosmetics.cosmetic.balloon;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.balloon.Balloon;
import se.file14.procosmetics.api.cosmetic.balloon.BalloonBehavior;
import se.file14.procosmetics.api.cosmetic.balloon.BalloonType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.rarity.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class BalloonTypeImpl extends CosmeticTypeImpl<BalloonType, BalloonBehavior> implements BalloonType {

    private final EntityType entityType;
    private final boolean baby;
    private final double scale;

    public BalloonTypeImpl(String key,
                           CosmeticCategory<BalloonType, BalloonBehavior, ?> category,
                           Supplier<BalloonBehavior> behaviorFactory,
                           boolean enabled,
                           boolean findable,
                           boolean purchasable,
                           int cost,
                           CosmeticRarity rarity,
                           ItemStack itemStack,
                           EntityType entityType,
                           boolean baby,
                           double scale) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.entityType = entityType;
        this.baby = baby;
        this.scale = scale;
    }

    @Override
    protected Balloon createInstance(ProCosmeticsPlugin plugin, User user, BalloonBehavior behavior) {
        return new BalloonImpl(plugin, user, this, behavior);
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean isBaby() {
        return baby;
    }

    @Override
    public double getScale() {
        return scale;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<BalloonType, BalloonBehavior, BalloonType.Builder> implements BalloonType.Builder {

        private EntityType entityType;
        private boolean baby;
        private double scale = 1.0d;

        public BuilderImpl(String key, CosmeticCategory<BalloonType, BalloonBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected BalloonType.Builder self() {
            return this;
        }

        @Override
        public BalloonType.Builder readFromConfig() {
            super.readFromConfig();

            Config config = category.getConfig();
            String path = getPath();

            entityType = parseEntity(path + "entity");
            baby = config.getBoolean(path + "baby");
            scale = config.getDouble(path + "scale");

            return this;
        }

        @Override
        public BalloonType.Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        @Override
        public BalloonType.Builder baby(boolean baby) {
            this.baby = baby;
            return this;
        }

        @Override
        public BalloonType.Builder scale(double scale) {
            this.scale = scale;
            return this;
        }

        @Override
        public BalloonType build() {
            return new BalloonTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    entityType,
                    baby,
                    scale
            );
        }
    }
}