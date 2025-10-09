package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;

public class MoltenSnake implements MountBehavior {

    private static final int TAILS = 20;
    private static final BlockData BLOCK_DATA = Material.NETHERRACK.createBlockData();

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
        Vector vector = location.getDirection().multiply(-0.5d);

        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix.identity()
                .scale(0.5f)
                //.rotateY(radians)
                .translate(-0.5f, 0.5f, -0.5f);

        for (int i = 0; i < TAILS; i++) {
            NMSEntity tailEntity = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.BLOCK_DISPLAY, tracker);

            if (tailEntity.getBukkitEntity() instanceof BlockDisplay blockDisplay) {
                blockDisplay.setBlock(BLOCK_DATA);
                blockDisplay.setTransformationMatrix(transformationMatrix);
                blockDisplay.setTeleportDuration(1);
            }
            tailEntity.setPositionRotation(location.add(vector));
        }
        tracker.startTracking();
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        entity.getLocation(reusableEntityLocation);
        Location tempLocation;
        Player player = context.getPlayer();

        if (player.getVehicle() == entity) {
            tempLocation = player.getLocation();
            entity.setVelocity(tempLocation.getDirection().multiply(0.7d));
        }
        Location location = reusableEntityLocation;

        for (NMSEntity tailEntity : tracker.getEntities()) {
            Location temp = tailEntity.getPreviousLocation().clone();

            if (temp != reusableEntityLocation) {
                tailEntity.sendPositionRotationPacket(location);
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