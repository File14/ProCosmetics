package se.file14.procosmetics.cosmetic.miniature.type;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class Cow implements MiniatureBehavior {

    private static final ItemStack HAND_ITEM = new ItemStack(Material.MILK_BUCKET);
    private static final ItemStack CHESTPLATE_ITEM;
    private static final ItemStack LEGGINGS_ITEM;
    private static final ItemStack BOOTS_ITEM;

    static {
        Color color = Color.fromRGB(115, 75, 40);
        CHESTPLATE_ITEM = new ItemBuilderImpl(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).getItemStack();
        LEGGINGS_ITEM = new ItemBuilderImpl(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color).getItemStack();
        BOOTS_ITEM = new ItemBuilderImpl(Material.LEATHER_BOOTS).setLeatherArmorColor(color).getItemStack();
    }

    @Override
    public void onEquip(CosmeticContext<MiniatureType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity entity) {
        entity.setMainHand(HAND_ITEM);
        entity.setChestplate(CHESTPLATE_ITEM);
        entity.setLeggings(LEGGINGS_ITEM);
        entity.setBoots(BOOTS_ITEM);
    }

    @Override
    public void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity entity, int tick) {
    }

    @Override
    public void onUnequip(CosmeticContext<MiniatureType> context) {
    }
}