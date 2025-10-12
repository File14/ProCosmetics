package se.file14.procosmetics.cosmetic.miniature.type;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.item.Heads;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class SantaClaus implements MiniatureBehavior {

    private static final ItemStack HAND_ITEM = Heads.GREEN_PRESENT.getSkull();
    private static final ItemStack CHESTPLATE_ITEM;
    private static final ItemStack LEGGINGS_ITEM;
    private static final ItemStack BOOTS_ITEM;

    static {
        Color color = Color.RED;
        CHESTPLATE_ITEM = new ItemBuilderImpl(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).getItemStack();
        LEGGINGS_ITEM = new ItemBuilderImpl(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color).getItemStack();
        BOOTS_ITEM = new ItemBuilderImpl(Material.LEATHER_BOOTS).setLeatherArmorColor(color).getItemStack();
    }

    @Override
    public void onEquip(CosmeticContext<MiniatureType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity) {
        nmsEntity.setMainHand(HAND_ITEM);
        nmsEntity.setChestplate(CHESTPLATE_ITEM);
        nmsEntity.setLeggings(LEGGINGS_ITEM);
        nmsEntity.setBoots(BOOTS_ITEM);
    }

    @Override
    public void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity, int tick) {
        nmsEntity.getPreviousLocation().getWorld().spawnParticle(Particle.SNOWFLAKE,
                nmsEntity.getPreviousLocation(),
                1,
                0.1d,
                0.0d,
                0.1d,
                0.0d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<MiniatureType> context) {
    }
}