/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.cosmetic.pet;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.pet.Pet;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PetTypeImpl extends CosmeticTypeImpl<PetType, PetBehavior> implements PetType {

    private final EntityType entityType;
    private final boolean baby;
    private final double scale;
    private final Sound spawnSound;
    private final ItemStack tossItem;

    public PetTypeImpl(String key,
                       CosmeticCategory<PetType, PetBehavior, ?> category,
                       Supplier<PetBehavior> behaviorFactory,
                       boolean enabled,
                       boolean findable,
                       boolean purchasable,
                       int cost,
                       CosmeticRarity rarity,
                       ItemStack itemStack,
                       EntityType entityType,
                       boolean baby,
                       double scale,
                       @Nullable Sound spawnSound,
                       @Nullable ItemStack tossItem) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.entityType = entityType;
        this.baby = baby;
        this.scale = scale;
        this.spawnSound = spawnSound;
        this.tossItem = tossItem;
    }

    @Override
    protected Pet createInstance(ProCosmeticsPlugin plugin, User user, PetBehavior behavior) {
        return new PetImpl(plugin, user, this, behavior);
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

    @Override
    @Nullable
    public Sound getSpawnSound() {
        return spawnSound;
    }

    @Override
    @Nullable
    public ItemStack getTossItem() {
        return tossItem;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<PetType, PetBehavior, PetType.Builder> implements PetType.Builder {

        private EntityType entityType;
        private boolean baby;
        private double scale = 1.0d;
        private Sound spawnSound;
        private ItemStack tossItem;

        public BuilderImpl(String key, CosmeticCategory<PetType, PetBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected PetType.Builder self() {
            return this;
        }

        @Override
        public PetType.Builder readFromConfig() {
            super.readFromConfig();

            Config config = category.getConfig();
            String path = getPath();

            entityType = parseEntity(path + "entity");
            baby = config.getBoolean(path + "baby");
            scale = config.getDouble(path + "scale");
            spawnSound = parseSound(path + "spawn_sound");

            String itemPath = path + "toss_item";

            if (config.getBoolean(itemPath + ".enable")) {
                tossItem = new ItemBuilderImpl(config, itemPath).getItemStack();
            }
            return this;
        }

        public PetType.Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        public PetType.Builder baby(boolean baby) {
            this.baby = baby;
            return this;
        }

        public PetType.Builder scale(double scale) {
            this.scale = scale;
            return this;
        }


        public PetType.Builder spawnSound(@Nullable Sound spawnSound) {
            this.spawnSound = spawnSound;
            return this;
        }

        public PetType.Builder tossItem(@Nullable ItemStack tossItem) {
            this.tossItem = tossItem;
            return this;
        }

        @Override
        public PetType build() {
            return new PetTypeImpl(key,
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
                    scale,
                    spawnSound,
                    tossItem
            );
        }
    }
}
