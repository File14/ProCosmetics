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

import java.util.List;
import java.util.function.Supplier;

public class ArrowEffectTypeImpl extends CosmeticTypeImpl<ArrowEffectType, ArrowEffectBehavior> implements ArrowEffectType {

    public ArrowEffectTypeImpl(String key,
                               CosmeticCategory<ArrowEffectType, ArrowEffectBehavior, ?> category,
                               Supplier<ArrowEffectBehavior> behaviorFactory,
                               boolean enabled,
                               boolean purchasable,
                               int cost,
                               CosmeticRarity rarity,
                               ItemStack itemStack,
                               List<String> treasureChests) {
        super(key, category, behaviorFactory, enabled, purchasable, cost, rarity, itemStack, treasureChests);
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
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    treasureChests
            );
        }
    }
}
