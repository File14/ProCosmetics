package se.file14.procosmetics.api.treasure.loot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;

/**
 * Represents an individual loot entry within a treasure chest loot pool.
 * <p>
 * Each {@code LootEntry} defines a potential loot that players can get
 * when opening a treasure chest. A loot entry may represent a cosmetic item,
 * currency, or other custom loot.
 *
 * @see se.file14.procosmetics.api.treasure.TreasureChest
 * @see CosmeticRarity
 */
public interface LootEntry {

    /**
     * Gets the translation key used to localize the name of this loot entry.
     *
     * @return the translation key for this loot entry’s name
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

    /**
     * Gets the rarity associated with this loot entry.
     *
     * @return the {@link CosmeticRarity} of this loot entry
     */
    CosmeticRarity getRarity();

    /**
     * Gets the {@link ItemStack} associated with this loot entry.
     *
     * @return the {@link ItemStack} representing the loot
     */
    ItemStack getItemStack();
}
