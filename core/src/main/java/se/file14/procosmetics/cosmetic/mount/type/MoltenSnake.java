package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;

public class MoltenSnake implements MountBehavior {

    private static final int TAILS = 20;
    private static final ItemStack ITEM_STACK = new ItemStack(Material.NETHERRACK);

    private final EntityTrackerImpl tracker = new EntityTrackerImpl();
    private final Location reusableEntityLocation = new Location(null, 0.0d, 0.0d, 0.0d);

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (entity instanceof MagmaCube cube) {
            cube.setSize(2);
        }
        Location location = context.getPlayer().getLocation();
        Vector vector = location.getDirection().multiply(-0.25d);

        for (int i = 0; i < TAILS; i++) {
            NMSEntity tailEntity = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.ARMOR_STAND, tracker);
            tailEntity.setPositionRotation(location.add(vector));
            tailEntity.setHelmet(ITEM_STACK);
            tailEntity.setInvisible(true);
            tailEntity.setArmorStandBasePlate(false);
            tailEntity.setArmorStandMarker(true);
            tailEntity.setArmorStandArms(false);
            tailEntity.setArmorStandSmall(true);
        }
        tracker.startTracking();
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        entity.getLocation(reusableEntityLocation);
        Location tempLocation = reusableEntityLocation;
        float yaw = tempLocation.getYaw();
        Player player = context.getPlayer();

        if (player.getVehicle() == entity) {
            tempLocation = player.getLocation();
            yaw = tempLocation.getYaw();

            nmsEntity.setYaw(yaw);
            entity.setVelocity(tempLocation.getDirection().multiply(0.7d));
        }
        Location location = reusableEntityLocation.add(0.0d, -0.5d, 0.0d);

        for (NMSEntity tailEntity : tracker.getEntities()) {
            Location temp = tailEntity.getPreviousLocation().clone();

            if (temp != reusableEntityLocation) {
                tailEntity.sendPositionRotationPacket(location);
                tailEntity.setHeadPose(tempLocation.getPitch(), yaw, 0.0f);
                tailEntity.sendMetadataPacket();
                location = temp;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        context.getUser().setFallDamageProtection(10);
        tracker.destroy();
    }
}