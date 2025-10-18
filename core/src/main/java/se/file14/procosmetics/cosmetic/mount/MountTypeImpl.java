package se.file14.procosmetics.cosmetic.mount;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.mount.Mount;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.function.Supplier;

public class MountTypeImpl extends CosmeticTypeImpl<MountType, MountBehavior> implements MountType {

    private final EntityType entityType;

    public MountTypeImpl(String key,
                         CosmeticCategory<MountType, MountBehavior, ?> category,
                         Supplier<MountBehavior> behaviorFactory,
                         boolean enabled,
                         boolean findable,
                         boolean purchasable,
                         int cost,
                         CosmeticRarity rarity,
                         ItemStack itemStack,
                         EntityType entityType) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.entityType = entityType;
    }

    @Override
    protected Mount createInstance(ProCosmeticsPlugin plugin, User user, MountBehavior behavior) {
        return new MountImpl(plugin, user, this, behavior);
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<MountType, MountBehavior, MountType.Builder> implements MountType.Builder {

        private EntityType entityType;

        public BuilderImpl(String key, CosmeticCategory<MountType, MountBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected MountType.Builder self() {
            return this;
        }

        public MountType.Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        @Override
        public MountType build() {
            return new MountTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    entityType
            );
        }
    }
}