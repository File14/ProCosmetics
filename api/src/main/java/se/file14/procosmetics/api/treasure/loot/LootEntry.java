package se.file14.procosmetics.api.treasure.loot;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.locale.Translator;

/**
 * Represents an individual loot entry within a treasure chest reward pool.
 * <p>
 * Each {@code LootEntry} defines a potential reward that players can get
 * when opening a treasure chest. A loot entry may represent a cosmetic item,
 * currency, or other custom reward, and typically includes a name, rarity,
 * and display item.
 *
 * @see se.file14.procosmetics.api.treasure.TreasureChest
 * @see CosmeticRarity
 */
public interface LootEntry {

    /**
     * Gets the localized display name of this loot entry.
     *
     * @param translator the translator used to localize the name
     * @return the localized loot name
     */
    String getName(Translator translator);

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
