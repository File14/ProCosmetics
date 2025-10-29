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
package se.file14.procosmetics.api.locale;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents a translatable localization key with optional styling,
 * placeholders, and tag resolvers.
 *
 * @see Translator
 */
public interface Translatable {

    /**
     * Gets the translation key of this message.
     *
     * @return the translation key
     */
    String key();

    /**
     * Gets the applied {@link Style}, if any.
     *
     * @return the Adventure style, or {@code null} if none is applied
     */
    @Nullable
    Style style();

    /**
     * Gets all {@link TagResolver} instances added to this translatable message.
     *
     * @return an immutable list of resolvers, or an empty list if none are set
     */
    List<TagResolver> resolvers();

    /**
     * Gets all placeholders added to this translatable message.
     *
     * @return an immutable map of placeholder key-value pairs, or an empty map if none are set
     */
    Map<String, Object> placeholders();
}
