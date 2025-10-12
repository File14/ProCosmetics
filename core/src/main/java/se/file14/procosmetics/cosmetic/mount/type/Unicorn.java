package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.cosmetic.mount.BlockTrailBehavior;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class Unicorn extends BlockTrailBehavior {

    private static final double FORWARD_OFFSET = 1.1d;
    private static final List<Color> COLORS = List.of(
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.LIME,
            Color.GREEN,
            Color.AQUA,
            Color.BLUE,
            Color.PURPLE
    );
    private static final List<ItemStack> BLOCKS = List.of(
            new ItemStack(Material.PINK_WOOL),
            new ItemStack(Material.PURPLE_WOOL)
    );
    private static final ItemStack BLAZE_ROD_ITEM = new ItemStack(Material.BLAZE_ROD);

    private int ticks;
    private NMSEntity horn;

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.setupEntity(context, entity, nmsEntity);

        if (entity instanceof Horse horse) {
            horse.setColor(Horse.Color.WHITE);
            horse.setStyle(Horse.Style.WHITE);
            horse.setJumpStrength(1.0d);
            horse.setAdult();
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        }
        horn = context.getPlugin().getNMSManager().createEntity(entity.getWorld(), EntityType.ARMOR_STAND);
        if (horn.getBukkitEntity() instanceof ArmorStand armorStand) {
            armorStand.setInvisible(true);
            armorStand.setArms(false);
            armorStand.setMarker(false);
        }
        horn.setHelmet(BLAZE_ROD_ITEM);
        horn.setPositionRotation(entity.getLocation());
        horn.getTracker().startTracking();
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        super.onUpdate(context, entity, nmsEntity);

        entity.getLocation(location);
        location.subtract(0.0d, 0.2d, 0.0d);
        location.setYaw(location.getYaw() + 90.0f);

        horn.sendPositionRotationPacket(MathUtil.getDirectionalLocation(location, FORWARD_OFFSET, 0.28d));

        if (context.getUser().isMoving() && context.getPlayer().getVehicle() == entity) {
            Location location2 = MathUtil.getDirectionalLocation(location, -1.0d, 0.0d);
            location2.add(0.0d, 1.4d, 0.0d);

            for (Color color : COLORS) {
                location2.getWorld().spawnParticle(Particle.DUST, location2, 2, 0.0d, 0.0d, 0.0d, 0.0d, new Particle.DustOptions(color, 1.0f));
                location2.setY(location2.getY() - 0.08d);
            }
        }

        if (ticks % 10 == 0) {
            Location location = entity.getLocation().add(0.0d, 1.0d, 0.0d);
            location.getWorld().spawnParticle(Particle.FIREWORK, location, 5, 1.0d, 1.5d, 1.0d, 0.0d);
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        super.onUnequip(context);

        if (horn != null) {
            horn.getTracker().destroy();
            horn = null;
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