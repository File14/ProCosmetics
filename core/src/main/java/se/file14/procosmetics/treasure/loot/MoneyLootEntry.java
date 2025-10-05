package se.file14.procosmetics.treasure.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.rarity.CosmeticRarityImpl;
import se.file14.procosmetics.rarity.CosmeticRarityRegistry;

public class MoneyLootEntry implements LootEntry {

    private static final ItemStack ITEM_STACK = new ItemStack(Material.SUNFLOWER);

    private final int amount;

    public MoneyLootEntry(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String getName(Translator translator) {
        return String.valueOf(amount);
    }

    @Override
    public CosmeticRarityImpl getRarity() {
        return CosmeticRarityRegistry.FALL_BACK_RARITY;
    }

    @Override
    public ItemStack getItemStack() {
        return ITEM_STACK;
    }
}