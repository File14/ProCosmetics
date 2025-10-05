package se.file14.procosmetics.locale;

import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class Resolvers {

    private Resolvers() {
    }

    public static TagResolver singularPlural(String key, long value) {
        return TagResolver.resolver(key, (args, context) -> {
            String singular = args.popOr("Singular expected.").value();
            String plural = args.popOr("Plural expected.").value();

            return Tag.inserting(context.deserialize(value == 1L ? singular : plural));
        });
    }
}
