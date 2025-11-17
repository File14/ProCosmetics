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
package se.file14.procosmetics.cosmetic.particleeffect;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffect;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.util.List;
import java.util.function.Supplier;

public class ParticleEffectTypeImpl extends CosmeticTypeImpl<ParticleEffectType, ParticleEffectBehavior> implements ParticleEffectType {

    private final int repeatDelay;

    public ParticleEffectTypeImpl(String key,
                                  CosmeticCategory<ParticleEffectType, ParticleEffectBehavior, ?> category,
                                  Supplier<ParticleEffectBehavior> behaviorFactory,
                                  boolean enabled,
                                  boolean purchasable,
                                  int cost,
                                  CosmeticRarity rarity,
                                  ItemStack itemStack,
                                  List<String> treasureChests,
                                  int repeatDelay) {
        super(key, category, behaviorFactory, enabled, purchasable, cost, rarity, itemStack, treasureChests);
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
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    treasureChests,
                    repeatDelay
            );
        }
    }
}
