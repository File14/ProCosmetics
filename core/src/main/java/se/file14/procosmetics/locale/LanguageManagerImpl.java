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

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.locale.Language;
import se.file14.procosmetics.api.locale.LanguageManager;
import se.file14.procosmetics.api.locale.Translatable;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class LanguageManagerImpl implements LanguageManager {

    private static final Gson GSON = new GsonBuilder().create();

    private final ProCosmetics plugin;
    private final Map<String, Language> languages = new HashMap<>();
    public String defaultLocale = "en_us";

    public LanguageManagerImpl(ProCosmetics plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        languages.clear();

        try {
            File file = new File(plugin.getJavaPlugin().getDataFolder(), "languages.json");
            JsonArray json = GSON.fromJson(Files.newBufferedReader(file.toPath()), JsonArray.class);

            for (JsonElement element : json) {
                JsonObject languageObject = element.getAsJsonObject();
                String code = languageObject.get("code").getAsString();
                String name = languageObject.get("name").getAsString();

                languages.putIfAbsent(code, new LanguageImpl(code, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadTranslations(plugin.getJavaPlugin());

        defaultLocale = plugin.getConfigManager().getMainConfig().getString("settings.default_language");
    }

    private void loadTranslations(Plugin plugin) {
        File languageFolder = new File(plugin.getDataFolder(), "lang");

        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(languageFolder.toPath(), "*.json")) {
            for (Path path : stream) {
                try {
                    JsonObject json = GSON.fromJson(Files.newBufferedReader(path), JsonObject.class);
                    String code = path.getName(path.getNameCount() - 1).toString().replace(".json", "");
                    Language language = getLanguage(code);

                    if (language == null) {
                        plugin.getLogger().log(Level.WARNING, "Failed to load translation file " + path.getFileName() + ". File not found.");
                        continue;
                    }

                    for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                        JsonElement translationElement = entry.getValue();
                        List<String> strings = new ArrayList<>();

                        if (translationElement.isJsonArray()) {
                            for (JsonElement element : translationElement.getAsJsonArray()) {
                                strings.add(element.getAsString());
                            }
                        } else {
                            strings.add(translationElement.getAsString());
                        }
                        language.setStrings(entry.getKey(), strings);
                    }
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to load translation file " + path.getFileName() + ".", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private Language.Translation translation(String key, String locale) {
        Language language = getLanguage(locale);
        Language.Translation translation = null;

        if (language != null) {
            translation = language.getTranslation(key);
        }

        if (translation == null && !locale.equals(defaultLocale)) {
            language = getLanguage(defaultLocale);

            if (language != null) {
                translation = language.getTranslation(key);
            }
        }
        return translation;
    }

    @Override
    public List<String> translateList(String key, String locale) {
        Language.Translation translation = translation(key, locale);

        if (translation != null) {
            return translation.getStringList();
        }
        return List.of(unknownKey(key));
    }

    @Override
    public String translate(String key, String locale) {
        Language.Translation translation = translation(key, locale);

        if (translation != null) {
            return translation.getString();
        }
        return unknownKey(key);
    }

    @Override
    public List<Component> renderList(String key, String locale, @Nullable Style style, TagResolver... resolvers) {
        Language.Translation translation = translation(key, locale);

        if (translation != null) {
            return translation.renderList(style, resolvers);
        }
        return List.of(Component.text(unknownKey(key)));
    }

    @Override
    public List<Component> renderList(String key, String locale, TagResolver... resolvers) {
        return renderList(key, locale, null, resolvers);
    }

    @Override
    public List<Component> renderList(Translatable translatable, String locale) {
        return renderList(translatable.key(), locale, translatable.style(), getResolvers(translatable, locale));
    }

    @Override
    public Component render(String key, String locale, @Nullable Style style, TagResolver... resolvers) {
        Language.Translation translation = translation(key, locale);

        if (translation != null) {
            return translation.render(style, resolvers);
        }
        return Component.text(unknownKey(key));
    }

    @Override
    public Component render(String key, String locale, TagResolver... resolvers) {
        return render(key, locale, null, resolvers);
    }

    @Override
    public Component render(Translatable translatable, String locale) {
        return render(translatable.key(), locale, translatable.style(), getResolvers(translatable, locale));
    }

    private TagResolver[] getResolvers(Translatable translatable, String locale) {
        List<TagResolver> resolvers = new ArrayList<>(translatable.resolvers());

        for (Map.Entry<String, Object> placeholder : translatable.placeholders().entrySet()) {
            String key = placeholder.getKey();
            Object value = placeholder.getValue();

            TagResolver resolver = switch (value) {
                case ComponentLike componentPlaceholder -> Placeholder.component(key, componentPlaceholder);
                case Translatable translatablePlaceholder ->
                        Placeholder.component(key, render(translatablePlaceholder, locale));
                case TagResolver resolverPlaceholder -> resolverPlaceholder;
                default -> Placeholder.unparsed(key, String.valueOf(value));
            };

            resolvers.add(resolver);
        }
        return resolvers.toArray(new TagResolver[0]);
    }

    @Nullable
    public Language getLanguage(String code) {
        return languages.get(code);
    }

    @Override
    public List<Language> getLanguages() {
        return List.copyOf(languages.values());
    }

    private String unknownKey(String key) {
        return "Unknown string: " + key;
    }

    @Override
    public String getLocale() {
        return defaultLocale;
    }

    @Override
    public void setLocale(String locale) {
        this.defaultLocale = locale;
    }

    @Override
    public Component translate(String key, @Nullable Style style, TagResolver... resolvers) {
        return render(key, defaultLocale, style, resolvers);
    }

    @Override
    public Component translate(Translatable translatable) {
        return render(translatable, defaultLocale);
    }

    @Override
    public List<Component> translateList(String key, @Nullable Style style, TagResolver... resolvers) {
        return renderList(key, defaultLocale, style, resolvers);
    }

    @Override
    public List<Component> translateList(Translatable translatable) {
        return renderList(translatable, defaultLocale);
    }

    @Override
    public String translateRaw(String key) {
        return translate(key, defaultLocale);
    }

    @Override
    public List<String> translateRawList(String key) {
        return translateList(key, defaultLocale);
    }
}
