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
package se.file14.procosmetics.locale;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.locale.Translatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatableImpl implements Translatable {

    private final String key;
    private Style style;
    private List<TagResolver> resolvers;
    private Map<String, Object> placeholders;

    TranslatableImpl(String key) {
        this.key = key;
    }

    @Contract("_ -> this")
    public TranslatableImpl style(@Nullable Style style) {
        this.style = style;
        return this;
    }

    @Contract("_, _ -> this")
    public TranslatableImpl placeholder(@TagPattern String key, Object value) {
        if (placeholders == null) {
            placeholders = new HashMap<>();
        }
        placeholders.put(key, value);
        return this;
    }

    @Contract("_ -> this")
    public TranslatableImpl resolver(TagResolver resolver) {
        if (resolvers == null) {
            resolvers = new ArrayList<>();
        }
        resolvers.add(resolver);
        return this;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    @Nullable
    public Style style() {
        return style;
    }

    @Override
    public List<TagResolver> resolvers() {
        if (resolvers == null) {
            return List.of();
        }
        return resolvers;
    }

    @Override
    public Map<String, Object> placeholders() {
        if (placeholders == null) {
            return Map.of();
        }
        return placeholders;
    }
}
