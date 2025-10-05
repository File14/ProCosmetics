package se.file14.procosmetics.api.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a language/locale with its associated translations.
 * Each language contains a collection of translation entries that can be
 * retrieved by key and rendered with MiniMessage formatting and placeholders.
 */
public interface Language {

    /**
     * Gets the language code (e.g., "en_us", "es_es").
     *
     * @return The language code
     */
    String getCode();

    /**
     * Gets the human-readable name of the language.
     *
     * @return The language name
     */
    String getName();

    /**
     * Retrieves a translation for the given key.
     *
     * @param key The translation key
     * @return The translation object, or null if not found
     */
    @Nullable
    Translation getTranslation(String key);

    @ApiStatus.Internal
    void setStrings(String key, List<String> strings);

    /**
     * API interface for a translation entry within a language.
     * Represents a specific translation with rendering capabilities.
     */
    interface Translation {

        /**
         * Renders the translation as a list of Adventure Components.
         * Uses caching for performance optimization.
         *
         * @param style     Optional style to apply to the components
         * @param resolvers Tag resolvers for placeholder replacement
         * @return A list of rendered components
         */
        List<Component> renderList(@Nullable Style style, TagResolver... resolvers);

        /**
         * Renders the translation as a single Adventure Component.
         * Uses caching for performance optimization.
         *
         * @param style     Optional style to apply to the component
         * @param resolvers Tag resolvers for placeholder replacement
         * @return A rendered component
         */
        Component render(@Nullable Style style, TagResolver... resolvers);

        /**
         * Gets the translation key.
         *
         * @return The translation key
         */
        String getKey();

        /**
         * Gets the raw translation strings as a list.
         *
         * @return An immutable list of translation strings
         */
        List<String> getStringList();

        @ApiStatus.Internal
        void setStringList(List<String> strings);

        /**
         * Gets the translation as a single merged string.
         * Multiple strings are joined with newlines.
         *
         * @return The merged translation string
         */
        String getString();
    }
}