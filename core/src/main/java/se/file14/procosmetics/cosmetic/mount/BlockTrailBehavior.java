package se.file14.procosmetics.cosmetic.mount;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public abstract class BlockTrailBehavior implements MountBehavior {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    protected final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private boolean blockTrailEnabled;

    public BlockTrailBehavior() {
    }

    public abstract int getBlockTrailRadius();

    public abstract List<ItemStack> getTrailBlocks();

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
        blockTrailEnabled = context.getType().getCategory().getConfig().getBoolean("block_trail");
        context.getPlayer().getLocation(location);
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (blockTrailEnabled && context.getPlayer().getVehicle() == entity) {
            if (entity.isOnGround()) {
                for (Block block : MathUtil.getIn3DRadius(entity.getLocation(location).subtract(0.0d, 1.0d, 0.0d), getBlockTrailRadius())) {
                    ItemStack itemStack = MathUtil.getRandomElement(getTrailBlocks());

                    if (itemStack != null) {
                        PLUGIN.getBlockRestoreManager().setFakeBlock(block, itemStack, true, 2);
                    }
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
    }
}
