package se.file14.procosmetics.cosmetic.miniature.type;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.item.Heads;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class Bunny implements MiniatureBehavior {

    private static final ItemStack CHESTPLATE_ITEM;
    private static final ItemStack LEGGINGS_ITEM;
    private static final ItemStack BOOTS_ITEM;

    static {
        Color color = Color.GRAY;
        CHESTPLATE_ITEM = new ItemBuilderImpl(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).getItemStack();
        LEGGINGS_ITEM = new ItemBuilderImpl(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color).getItemStack();
        BOOTS_ITEM = new ItemBuilderImpl(Material.LEATHER_BOOTS).setLeatherArmorColor(color).getItemStack();
    }

    @Override
    public void onEquip(CosmeticContext<MiniatureType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity) {
        nmsEntity.setMainHand(Heads.EGG_BASKET.getSkull());
        nmsEntity.setChestplate(CHESTPLATE_ITEM);
        nmsEntity.setLeggings(LEGGINGS_ITEM);
        nmsEntity.setBoots(BOOTS_ITEM);
    }

    @Override
    public void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity, int tick) {
        if (tick % 15 == 0) {
            MathUtil.findLocationsInCircle(nmsEntity.getPreviousLocation(), 8, 0.3d, 0.0d, location ->
                    location.getWorld().spawnParticle(Particle.DUST,
                            location,
                            1,
                            new Particle.DustOptions(
                                    Color.fromRGB(
                                            MathUtil.randomRangeInt(0, 255),
                                            MathUtil.randomRangeInt(0, 255),
                                            MathUtil.randomRangeInt(0, 255)
                                    ),
                                    0.0f
                            )
                    )
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MiniatureType> context) {
    }
}