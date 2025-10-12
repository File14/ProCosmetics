package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class CaveSpider extends BlockTrailBehavior {

    private static final List<ItemStack> BLOCKS = List.of(
            new ItemStack(Material.MOSSY_COBBLESTONE),
            new ItemStack(Material.COBBLESTONE)
    );
    private static final ItemStack COBWEB_ITEM = new ItemStack(Material.COBWEB);
    private static final double ITEM_SPREAD = 0.5d;

    private int ticks;

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.setupEntity(context, entity, nmsEntity);

        entity.setSilent(true);
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        // Custom controls
        if (context.getPlayer().getVehicle() == entity) {
            nmsEntity.moveRide(context.getPlayer());
        }

        if (ticks % 10 == 0) {
            entity.getLocation(location);
            location.getWorld().spawnParticle(Particle.ENCHANTED_HIT, location, 10, 0.7f, 0.8f, 0.7f, 0.0f);

            if (isTossItemsEnabled(context)) {
                NMSEntity itemEntity = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
                itemEntity.setPositionRotation(location);
                itemEntity.setEntityItemStack(COBWEB_ITEM);
                itemEntity.setVelocity(
                        MathUtil.randomRange(-ITEM_SPREAD, ITEM_SPREAD),
                        MathUtil.randomRange(-0.1d, ITEM_SPREAD),
                        MathUtil.randomRange(-ITEM_SPREAD, ITEM_SPREAD)
                );
                itemEntity.getTracker().startTracking();
                itemEntity.getTracker().destroyAfter(30);
            }
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public int getBlockTrailRadius() {
        return 1;
    }

    @Override
    public List<ItemStack> getTrailBlocks() {
        return BLOCKS;
    }
}