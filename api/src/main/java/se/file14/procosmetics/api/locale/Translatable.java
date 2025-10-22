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
