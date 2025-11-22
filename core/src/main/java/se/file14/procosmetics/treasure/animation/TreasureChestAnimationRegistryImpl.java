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
package se.file14.procosmetics.treasure.animation;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.treasure.animation.TreasureChestAnimationRegistry;
import se.file14.procosmetics.treasure.animation.type.Common;
import se.file14.procosmetics.treasure.animation.type.Legendary;
import se.file14.procosmetics.treasure.animation.type.Rare;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TreasureChestAnimationRegistryImpl implements TreasureChestAnimationRegistry {

    private final Map<String, AnimationFactory> animations = new HashMap<>();

    public TreasureChestAnimationRegistryImpl() {
        registerDefaults();
    }

    private void registerDefaults() {
        register("common", Common::new);
        register("rare", Rare::new);
        register("legendary", Legendary::new);
    }

    @Override
    public void register(String key, AnimationFactory factory) {
        animations.put(key.toLowerCase(), factory);
    }

    @Override
    public @Nullable AnimationFactory get(String key) {
        return animations.get(key.toLowerCase());
    }

    @Override
    public Collection<AnimationFactory> getAnimations() {
        return animations.values();
    }
}
