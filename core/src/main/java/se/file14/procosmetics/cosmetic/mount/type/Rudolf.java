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
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.MathUtil;

public class Rudolf implements MountBehavior {

    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);

    private static final ItemStack SADDLE_ITEM = new ItemStack(Material.SADDLE);
    private static final ItemStack DEAD_BUSH_ITEM = new ItemStack(Material.DEAD_BUSH);
    private final static double HORN_WIDTH_OFFSET = 0.3d;

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private final EntityTrackerImpl tracker = new EntityTrackerImpl();
    private int ticks;

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (entity instanceof Horse horse) {
            horse.setColor(Horse.Color.BROWN);
            horse.setStyle(Horse.Style.NONE);
            horse.setJumpStrength(1.0d);
            horse.setAdult();
            horse.setTamed(true);
            horse.getInventory().setSaddle(SADDLE_ITEM);

            Location location = entity.getLocation();

            for (int i = 0; i < 2; i++) {
                NMSEntity horn = context.getPlugin().getNMSManager().createEntity(entity.getWorld(), EntityType.ARMOR_STAND, tracker);
                if (horn.getBukkitEntity() instanceof ArmorStand armorStand) {
                    armorStand.setInvisible(true);
                    armorStand.setArms(false);
                    armorStand.setMarker(false);
                }
                horn.setHelmet(DEAD_BUSH_ITEM);
                horn.setPositionRotation(location);
                horn.setHeadPose(0.0f, (float) Math.toDegrees(-1.0d + i * 2.0d), (float) Math.toDegrees(1.0d + i * -2.0d));
            }
            tracker.startTracking();
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        int i = 0;

        entity.getLocation(location);

        for (NMSEntity horn : tracker.getEntities()) {
            Location hornLocation = MathUtil.getDirectionalLocation(location, -HORN_WIDTH_OFFSET + i++ * 2.0d * HORN_WIDTH_OFFSET, 0.95d);

            if (horn.getPreviousLocation() != hornLocation) {
                horn.sendPositionRotationPacket(hornLocation);
            }
        }
        if (ticks % 5 == 0) {
            location.getWorld().spawnParticle(Particle.SNOWFLAKE, location, 6, 1.0d, 1.2d, 1.0d, 0.0d);
        }

        location.getWorld().spawnParticle(Particle.DUST,
                MathUtil.getDirectionalLocation(location.add(0.0d, 1.45d, 0.0d), 0.0d, 1.35d),
                0, 0.0d, 0.0d, 0.0d, 0, DUST_OPTIONS
        );

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        tracker.destroy();
    }
}