package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class InfernalHorror extends BlockTrailBehavior {

    private static final List<ItemStack> BLOCKS = List.of(new ItemStack(Material.ORANGE_TERRACOTTA), new ItemStack(Material.YELLOW_TERRACOTTA));
    private static final ItemStack ITEM_STACK = new ItemStack(Material.BONE);
    private static final double ITEM_SPREAD = 0.5d;

    private int ticks;

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.setupEntity(context, entity, nmsEntity);

        if (entity instanceof SkeletonHorse horse) {
            horse.setJumpStrength(1.2d);
            horse.setAdult();
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        if (ticks % 10 == 0) {
            entity.getLocation(location).add(0.0d, 1.0d, 0.0d);
            location.getWorld().spawnParticle(Particle.FLAME, location, 5, 0.0d, 0.0d, 0.0d, 0.1d);
            location.getWorld().spawnParticle(Particle.LAVA, location, 0);

            if (isTossItemsEnabled(context)) {
                NMSEntity itemEntity = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
                itemEntity.setPositionRotation(location);
                itemEntity.setEntityItemStack(ITEM_STACK);
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