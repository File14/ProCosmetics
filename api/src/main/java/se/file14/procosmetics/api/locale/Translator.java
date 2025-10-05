package se.file14.procosmetics.api.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents an entity that can provide translation services
 */
public interface Translator {

    /**
     * Get the locale for this translation provider
     *
     * @return the locale string (e.g., "en_us")
     */
    String getLocale();

    /**
     * Translate a key to a component
     *
     * @param key       the translation key
     * @param style     optional style to apply
     * @param resolvers tag resolvers for placeholders
     * @return the translated component
     */
    Component translate(String key, @Nullable Style style, TagResolver... resolvers);

    /**
     * Translate a key to a component with no style
     *
     * @param key       the translation key
     * @param resolvers tag resolvers for placeholders
     * @return the translated component
     */
    default Component translate(String key, TagResolver... resolvers) {
        return translate(key, null, resolvers);
    }

    /**
     * Translate a translatable object
     *
     * @param translatable the translatable object
     * @return the translated component
     */
    Component translate(Translatable translatable);

    /**
     * Translate a key to a list of components
     *
     * @param key       the translation key
     * @param style     optional style to apply
     * @param resolvers tag resolvers for placeholders
     * @return the translated component list
     */
    List<Component> translateList(String key, @Nullable Style style, TagResolver... resolvers);

    /**
     * Translate a key to a list of components with no style
     *
     * @param key       the translation key
     * @param resolvers tag resolvers for placeholders
     * @return the translated component list
     */
    default List<Component> translateList(String key, TagResolver... resolvers) {
        return translateList(key, null, resolvers);
    }

    /**
     * Translate a translatable object to a list of components
     *
     * @param translatable the translatable object
     * @return the translated component list
     */
    List<Component> translateList(Translatable translatable);

    /**
     * Translate a key to a raw string
     *
     * @param key the translation key
     * @return the translated string
     */
    String translateRaw(String key);

    /**
     * Translate a key to a list of raw strings
     *
     * @param key the translation key
     * @return the translated string list
     */
    List<String> translateRawList(String key);
}