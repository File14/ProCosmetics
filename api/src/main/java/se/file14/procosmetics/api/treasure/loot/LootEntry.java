package se.file14.procosmetics.api.treasure.loot;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.rarity.CosmeticRarity;

public interface LootEntry {

    String getName(Translator translator);

    CosmeticRarity getRarity();

    ItemStack getItemStack();
}