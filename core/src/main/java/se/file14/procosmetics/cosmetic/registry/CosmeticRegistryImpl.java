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
package se.file14.procosmetics.cosmetic.registry;

import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CosmeticRegistryImpl<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>,
        U extends CosmeticType.Builder<T, B, U>>
        implements CosmeticRegistry<T, B, U> {

    private final Map<String, T> types = new HashMap<>();
    private final Map<String, T> enabledTypes = new HashMap<>();

    private final CosmeticCategory<T, B, U> category;
    private final BiFunction<String, CosmeticCategory<T, B, U>, U> builderFactory;

    public CosmeticRegistryImpl(CosmeticCategory<T, B, U> category, BiFunction<String, CosmeticCategory<T, B, U>, U> builderFactory) {
        this.category = category;
        this.builderFactory = builderFactory;
    }

    @Override
    public T register(T type) {
        String key = type.getKey().toLowerCase();

        if (types.containsKey(key)) {
            throw new IllegalArgumentException("Cosmetic type with key " + type.getKey() + " already registered!");
        }

        if (type.isEnabled()) {
            enabledTypes.put(key, type);
        }
        types.put(key, type);
        return type;
    }

    @Override
    public U builder(String key) {
        return builderFactory.apply(key, category);
    }

    @Override
    public T getType(String key) {
        return types.get(key.toLowerCase());
    }

    @Override
    public T getEnabledType(String key) {
        return enabledTypes.get(key.toLowerCase());
    }

    @Override
    public Collection<T> getTypes() {
        return types.values();
    }

    @Override
    public Collection<T> getEnabledTypes() {
        return enabledTypes.values();
    }

    @Override
    public boolean isRegistered(String key) {
        return types.containsKey(key.toLowerCase());
    }

    @Override
    public boolean unregister(String key) {
        return types.remove(key.toLowerCase()) != null;
    }

    @Override
    public void clear() {
        types.clear();
    }
}
