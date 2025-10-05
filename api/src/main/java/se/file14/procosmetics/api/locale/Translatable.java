package se.file14.procosmetics.api.locale;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translatable {

    private final String key;
    private Style style;
    private List<TagResolver> resolvers;
    private Map<String, Object> placeholders;

    private Translatable(String key) {
        this.key = key;
    }

    @Contract("!null -> new; null -> null")
    public static Translatable of(String key) {
        if (key == null) {
            return null;
        }
        return new Translatable(key);
    }

    @Contract("_ -> this")
    public Translatable style(@Nullable Style style) {
        this.style = style;
        return this;
    }

    @Contract("_, _ -> this")
    public Translatable placeholder(@TagPattern String key, Object value) {
        if (placeholders == null) {
            placeholders = new HashMap<>();
        }
        placeholders.put(key, value);
        return this;
    }

    @Contract("_ -> this")
    public Translatable resolver(TagResolver resolver) {
        if (resolvers == null) {
            resolvers = new ArrayList<>();
        }
        resolvers.add(resolver);
        return this;
    }

    public String key() {
        return key;
    }

    @Nullable
    public Style style() {
        return style;
    }

    public List<TagResolver> resolvers() {
        if (resolvers == null) {
            return List.of();
        }
        return resolvers;
    }

    public Map<String, Object> placeholders() {
        if (placeholders == null) {
            return Map.of();
        }
        return placeholders;
    }
}
