package se.file14.procosmetics.treasure.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.loot.LootEntry;

public class MoneyLootEntry implements LootEntry {

    private static final ProCosmetics PLUGIN = ProCosmeticsPlugin.getPlugin();
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
    public CosmeticRarity getRarity() {
        return PLUGIN.getCosmeticRarityRegistry().getFallbackRarity();
    }

    @Override
    public ItemStack getItemStack() {
        return ITEM_STACK;
    }
}