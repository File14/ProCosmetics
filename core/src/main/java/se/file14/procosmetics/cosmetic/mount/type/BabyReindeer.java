package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;

import java.util.List;

public class BabyReindeer extends BlockTrailBehavior {

    private static final List<ItemStack> BLOCKS = List.of(new ItemStack(Material.SNOW_BLOCK));

    private static final ItemStack SADDLE_ITEM = new ItemStack(Material.SADDLE);

    private int ticks;

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.setupEntity(context, entity, nmsEntity);

        if (entity instanceof Horse horse) {
            horse.setJumpStrength(1.0d);
            horse.setBaby();
            horse.setTamed(true);
            horse.getInventory().setSaddle(SADDLE_ITEM);
            horse.setColor(Horse.Color.BROWN);
            horse.setStyle(Horse.Style.NONE);
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        if (ticks % 5 == 0) {
            entity.getLocation(location).add(0.0d, 1.0d, 0.0d);
            location.getWorld().spawnParticle(Particle.SNOWFLAKE, location, 3, 1.0d, 0.5d, 1.0d, 0.0d);
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