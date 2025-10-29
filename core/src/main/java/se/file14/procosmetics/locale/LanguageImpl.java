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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.locale.Language;

import java.util.*;

public class LanguageImpl implements Language {

    private final String code;
    private final String name;
    private final Map<String, Translation> translations = new HashMap<>();

    public LanguageImpl(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @Nullable
    public Translation getTranslation(String key) {
        return translations.get(key);
    }


    @Override
    public void setStrings(String key, List<String> strings) {
        this.translations.computeIfAbsent(key, TranslationImpl::new).setStringList(strings);
    }

    public static class TranslationImpl implements Translation {

        private final String key;
        private final RenderCache cache = new RenderCache();
        private List<String> strings;
        private String merged;

        private TranslationImpl(String key) {
            this.key = key;
        }

        @Override
        public List<Component> renderList(@Nullable Style style, TagResolver... resolvers) {
            List<Component> components = cache.getList(style, resolvers);

            if (components != null) {
                return components;
            }
            components = new ArrayList<>();

            for (String string : strings) {
                components.add(render(string, style, resolvers));
            }
            return cache.cacheList(components, style, resolvers);
        }

        @Override
        public Component render(@Nullable Style style, TagResolver... resolvers) {
            Component component = cache.get(style, resolvers);

            if (component != null) {
                return component;
            }
            return cache.cache(render(merged, style, resolvers), style, resolvers);
        }

        private Component render(String string, @Nullable Style style, TagResolver... resolvers) {
            Component component = MiniMessage.miniMessage().deserialize(string, resolvers);

            if (style != null && !style.isEmpty()) {
                component = component.applyFallbackStyle(style);
            }
            return component;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public List<String> getStringList() {
            return strings;
        }

        @Override
        public void setStringList(List<String> strings) {
            this.strings = List.copyOf(strings);
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < strings.size(); i++) {
                builder.append(strings.get(i));

                if (i + 1 < strings.size()) {
                    builder.append("\n");
                }
            }
            this.merged = builder.toString();
        }

        @Override
        public String getString() {
            return merged;
        }
    }

    private static class RenderCache {

        private List<Component> render;
        private Style style;
        private TagResolver[] resolvers;
        private boolean isList;

        public List<Component> cacheList(List<Component> render, Style style, TagResolver[] resolvers) {
            this.render = List.copyOf(render);
            this.style = style;
            this.resolvers = resolvers;
            isList = true;
            return this.render;
        }

        public Component cache(Component render, Style style, TagResolver[] resolvers) {
            this.render = List.of(render);
            this.style = style;
            this.resolvers = resolvers;
            isList = false;
            return this.render.getFirst();
        }

        @Nullable
        public List<Component> getList(Style style, TagResolver[] resolvers) {
            if (render != null && isList && Objects.equals(this.style, style) && Arrays.equals(this.resolvers, resolvers)) {
                return render;
            }
            return null;
        }

        @Nullable
        public Component get(Style style, TagResolver[] resolvers) {
            if (render != null && !isList && Objects.equals(this.style, style) && Arrays.equals(this.resolvers, resolvers)) {
                return render.getFirst();
            }
            return null;
        }
    }
}
