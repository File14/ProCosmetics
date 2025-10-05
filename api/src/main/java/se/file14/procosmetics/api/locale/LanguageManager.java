package se.file14.procosmetics.api.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Manages language translations and localization.
 */
public interface LanguageManager extends Translator {

    /**
     * The default locale used as fallback when translations are not found.
     */
    String DEFAULT_LOCALE = "en_us";

    /**
     * Translates a key to a list of strings in the specified locale.
     * Falls back to the default locale if translation is not found.
     *
     * @param key    The translation key
     * @param locale The target locale
     * @return A list of translated strings, or a list containing an unknown key message if not found
     */
    List<String> translateList(String key, String locale);

    /**
     * Translates a key to a single string in the specified locale.
     * Falls back to the default locale if translation is not found.
     *
     * @param key    The translation key
     * @param locale The target locale
     * @return The translated string, or an unknown key message if not found
     */
    String translate(String key, String locale);

    /**
     * Renders a translation key as a list of Adventure Components with optional styling and resolvers.
     *
     * @param key       The translation key
     * @param locale    The target locale
     * @param style     Optional style to apply to the components
     * @param resolvers Tag resolvers for placeholder replacement
     * @return A list of rendered components
     */
    List<Component> renderList(String key, String locale, @Nullable Style style, TagResolver... resolvers);

    /**
     * Renders a translation key as a list of Adventure Components with resolvers.
     *
     * @param key       The translation key
     * @param locale    The target locale
     * @param resolvers Tag resolvers for placeholder replacement
     * @return A list of rendered components
     */
    List<Component> renderList(String key, String locale, TagResolver... resolvers);

    /**
     * Renders a Translatable object as a list of Adventure Components.
     *
     * @param translatable The translatable object containing key, style, and resolvers
     * @param locale       The target locale
     * @return A list of rendered components
     */
    List<Component> renderList(Translatable translatable, String locale);

    /**
     * Renders a translation key as a single Adventure Component with optional styling and resolvers.
     *
     * @param key       The translation key
     * @param locale    The target locale
     * @param style     Optional style to apply to the component
     * @param resolvers Tag resolvers for placeholder replacement
     * @return A rendered component
     */
    Component render(String key, String locale, @Nullable Style style, TagResolver... resolvers);

    /**
     * Renders a translation key as a single Adventure Component with resolvers.
     *
     * @param key       The translation key
     * @param locale    The target locale
     * @param resolvers Tag resolvers for placeholder replacement
     * @return A rendered component
     */
    Component render(String key, String locale, TagResolver... resolvers);

    /**
     * Renders a Translatable object as a single Adventure Component.
     *
     * @param translatable The translatable object containing key, style, and resolvers
     * @param locale       The target locale
     * @return A rendered component
     */
    Component render(Translatable translatable, String locale);

    /**
     * Retrieves a language by its code.
     *
     * @param code The language code
     * @return The language object, or null if not found
     */
    @Nullable
    Language getLanguage(String code);

    /**
     * Gets all available languages.
     *
     * @return An immutable list of all loaded languages
     */
    List<Language> getLanguages();
}
