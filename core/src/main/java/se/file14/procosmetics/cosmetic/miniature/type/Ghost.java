package se.file14.procosmetics.cosmetic.miniature.type;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class Ghost implements MiniatureBehavior {

    private static final ItemStack CHESTPLATE_ITEM = new ItemBuilderImpl(Material.LEATHER_CHESTPLATE)
            .setLeatherArmorColor(Color.WHITE)
            .getItemStack();

    @Override
    public void onEquip(CosmeticContext<MiniatureType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity) {
        nmsEntity.setChestplate(CHESTPLATE_ITEM);
    }

    @Override
    public void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity, int tick) {
        nmsEntity.getPreviousLocation().getWorld().spawnParticle(Particle.ITEM_SNOWBALL,
                nmsEntity.getPreviousLocation().add(0.0d, 0.3d, 0.0d),
                1,
                0.0d,
                0.0d,
                0.0d,
                0.0d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<MiniatureType> context) {
    }
}