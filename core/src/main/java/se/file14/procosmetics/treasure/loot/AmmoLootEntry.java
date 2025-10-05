package se.file14.procosmetics.treasure.loot;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.rarity.CosmeticRarityImpl;
import se.file14.procosmetics.rarity.CosmeticRarityRegistry;

public class AmmoLootEntry implements LootEntry {

    private final GadgetType ammo;
    private final int amount;

    public AmmoLootEntry(GadgetType ammo, int amount) {
        this.ammo = ammo;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String getName(Translator translator) {
        return amount + " " + ammo.getName(translator);
    }

    @Override
    public CosmeticRarityImpl getRarity() {
        return CosmeticRarityRegistry.FALL_BACK_RARITY;
    }

    @Override
    public ItemStack getItemStack() {
        return ammo.getItemStack();
    }

    public GadgetType getAmmo() {
        return ammo;
    }
}