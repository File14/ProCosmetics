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
package se.file14.procosmetics.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;

/**
 * Represents an object with a unique key and a localizable, resolvable display name.
 */
public interface ResolvableName {

    /**
     * Gets the unique identifier key for this object.
     *
     * @return the unique key
     */
    String getKey();

    /**
     * Gets the translation key used to localize the name of this loot entry.
     *
     * @return the translation key for this loot entry's name
     */
    String getNameTranslationKey();

    /**
     * Gets the localized name of this loot entry.
     *
     * @param translator the translator used to localize the name
     * @return the localized loot name
     */
    default String getName(Translator translator) {
        return translator.translateRaw(getNameTranslationKey());
    }

    /**
     * Gets the tag resolvers for this loot entry to use with MiniMessage formatting.
     *
     * @param user the user to use for the resolvers
     * @return the tag resolver for this loot entry
     */
    TagResolver getResolvers(User user);

    /**
     * Gets the fully resolved name of this loot entry.
     *
     * @param user the user context used for translation and resolver application
     * @return the fully resolved display name as a {@link Component}
     */
    default Component getResolvedName(User user) {
        return user.translate(getNameTranslationKey(), getResolvers(user));
    }
}
