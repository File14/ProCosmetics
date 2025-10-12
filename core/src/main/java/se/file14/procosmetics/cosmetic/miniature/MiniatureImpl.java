package se.file14.procosmetics.cosmetic.miniature;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.miniature.Miniature;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.util.FastMathUtil;

public class MiniatureImpl extends CosmeticImpl<MiniatureType, MiniatureBehavior> implements Miniature {

    private static final double MAX_DISTANCE_SQUARED_BEFORE_TELEPORT = 256.0d;
    private static final double DISTANCE_SQUARED_BEFORE_MOVE_TOWARDS_PLAYER = 3.5d;

    protected NMSEntity nmsEntity;
    protected int tick;

    private final Location playerLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location entityLocation = new Location(null, 0.0d, 0.0d, 0.0d);

    public MiniatureImpl(ProCosmeticsPlugin plugin, User user, MiniatureType type, MiniatureBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
        spawnEntity();
        runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    @Override
    public void onUpdate() {
        player.getLocation(playerLocation);
        nmsEntity.getPreviousLocation(entityLocation);

        if (entityLocation.getWorld() != playerLocation.getWorld()) {
            return;
        }
        double distanceSquared = playerLocation.distanceSquared(entityLocation);

        if (distanceSquared > MAX_DISTANCE_SQUARED_BEFORE_TELEPORT) {
            nmsEntity.sendPositionRotationPacket(playerLocation);
            return;
        }
        double yOffset = 0.3d * FastMathUtil.cos(FastMathUtil.toRadians(tick * 6));
        double y = playerLocation.getY() + 0.8d + yOffset;
        entityLocation.setY(y);

        entityLocation.setDirection(playerLocation.add(0.0d, 0.8d, 0.0d)
                .subtract(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ())
                .toVector()
                .normalize());

        if (distanceSquared > DISTANCE_SQUARED_BEFORE_MOVE_TOWARDS_PLAYER) {
            float angle = FastMathUtil.toRadians(entityLocation.getYaw());
            double speed = distanceSquared / 32.0d;
            double x = -FastMathUtil.sin(angle) * speed;
            double z = FastMathUtil.cos(angle) * speed;
            entityLocation.add(x, 0.0d, z);
        }
        nmsEntity.sendPositionRotationPacket(entityLocation);
        nmsEntity.setHeadPose(entityLocation.getPitch(), 0.0f, 0.0f);
        nmsEntity.sendEntityMetadataPacket();

        if (++tick >= 360) {
            tick = 0;
        }
        behavior.onUpdate(this, nmsEntity, tick);
    }

    @Override
    protected void onUnequip() {
        if (nmsEntity != null) {
            nmsEntity.getTracker().destroy();
            nmsEntity = null;
        }
    }

    private void spawnEntity() {
        nmsEntity = plugin.getNMSManager().createEntity(player.getWorld(), EntityType.ARMOR_STAND);
        nmsEntity.setPositionRotation(getSpawnLocation());
        nmsEntity.setHelmet(cosmeticType.getItemStack());

        if (nmsEntity.getBukkitEntity() instanceof ArmorStand armorStand) {
            armorStand.setInvisible(cosmeticType.hasInvisibility());
            armorStand.setBasePlate(false);
            armorStand.setSmall(true);
            armorStand.setArms(cosmeticType.hasArms());
        }
        behavior.setupEntity(this, nmsEntity);
        nmsEntity.getTracker().startTracking();
    }

    private Location getSpawnLocation() {
        player.getLocation(playerLocation).add(0.0d, 0.5d, 0.0d).add(playerLocation.getDirection().multiply(1.5d));
        playerLocation.setDirection(player.getLocation()
                .subtract(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ())
                .toVector()
                .normalize());
        return playerLocation;
    }

    @Override
    public NMSEntity getNMSEntity() {
        return nmsEntity;
    }
}