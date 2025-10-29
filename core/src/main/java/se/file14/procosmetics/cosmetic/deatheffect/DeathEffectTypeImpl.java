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
