package se.file14.procosmetics.cosmetic.balloon;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.balloon.Balloon;
import se.file14.procosmetics.api.cosmetic.balloon.BalloonBehavior;
import se.file14.procosmetics.api.cosmetic.balloon.BalloonType;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.MathUtil;

import java.util.Random;

public class BalloonImpl extends CosmeticImpl<BalloonType, BalloonBehavior> implements Balloon {

    private static final Random RANDOM = new Random();
    private static final float DEGREES_180 = FastMathUtil.toRadians(180.0f);

    private static final Vector OFFSET_VECTOR = new Vector(-0.8d, 0.0d, 0.2d);
    private static final double LEASH_LENGTH = 2.5d;
    private static final double Y_INERTIA = 0.1d;
    private static final double INERTIA_DECAY = 0.95d;
    private static final float ROTATION_DECAY = 0.98f;
    private static final float MIN_ROTATION_SPEED = 1.0f;
    private static final float ROTATION_MULTIPLIER = 1.0f;

    private static final double WIND_STRENGTH = 0.01d;
    private static final double TURBULENCE_FREQUENCY = 0.08d;

    private final EntityTracker entityTracker = new EntityTrackerImpl();
    private NMSEntity entity;
    private NMSEntity leashEntity;

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location targetLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Vector offsetVector = OFFSET_VECTOR.clone();
    private final Vector inertia = new Vector(0.0d, 0.2d, 0.0d);
    private final Vector temp = new Vector(0.0d, 0.0d, 0.0d);
    private final Vector windDirection = new Vector(1.0d, 0.0d, 0.3d);
    private final Vector windForce = new Vector();

    private float rotationSpeed = RANDOM.nextFloat();
    private double windPhase = RANDOM.nextDouble() * Math.PI * 2;
    private double angle = 0.0;

    public BalloonImpl(ProCosmeticsPlugin plugin, User user, BalloonType type, BalloonBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
        user.removeCosmetic(plugin.getCategoryRegistries().morphs(), false, true);
        updateLocation();
        LocationUtil.copy(targetLocation, location);

        createBalloonEntity();
        createLeashEntity();

        // Set as passenger to avoid disconnect between balloon and leash
        if (leashEntity != null) {
            entity.getBukkitEntity().addPassenger(leashEntity.getBukkitEntity());
        }
        entityTracker.startTracking();
        runTaskTimerAsynchronously(plugin, 5L, 1L);
    }

    @Override
    protected void onUpdate() {
        updatePhysics();

        entity.sendPositionRotationPacket(location);

        behavior.onUpdate(this, location);
    }

    @Override
    protected void onUnequip() {
        spawnPopEffects();
        entityTracker.destroy();

        if (entity.getBukkitEntity() instanceof Mob) {
            entity.addCollision(player);
        }
    }

    @Override
    public EntityTracker getTracker() {
        return entityTracker;
    }

    @Override
    public NMSEntity getNMSEntity() {
        return entity;
    }

    @Override
    public NMSEntity getLeashEntity() {
        return leashEntity;
    }

    private void createBalloonEntity() {
        entity = plugin.getNMSManager().createEntity(player.getWorld(), cosmeticType.getEntityType(), entityTracker);
        Entity bukkitEntity = entity.getBukkitEntity();

        applyEntityScale(bukkitEntity);
        configureDisplayEntity(bukkitEntity);

        if (cosmeticType.isBaby() && bukkitEntity instanceof Ageable ageable) {
            ageable.setBaby();
        }
        entity.setPositionRotation(location);

        if (bukkitEntity instanceof Mob) {
            entity.setLeashHolder(player);
            entity.removeCollision(player);
        }
    }

    private void applyEntityScale(Entity bukkitEntity) {
        if (bukkitEntity instanceof LivingEntity livingEntity) {
            AttributeInstance attribute = livingEntity.getAttribute(Attribute.SCALE);

            if (attribute != null) {
                attribute.setBaseValue(cosmeticType.getScale());
            }
        }
    }

    private void configureDisplayEntity(Entity bukkitEntity) {
        if (!(bukkitEntity instanceof Display display)) {
            return;
        }
        float scale = (float) cosmeticType.getScale();
        Matrix4f transformationMatrix = new Matrix4f();

        if (display instanceof ItemDisplay itemDisplay) {
            itemDisplay.setItemStack(getType().getItemStack());
            transformationMatrix.identity()
                    .scale(scale)
                    .rotateY(DEGREES_180)
                    .translate(0.0f, 0.5f, 0.0f);
        } else if (display instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(getType().getItemStack().getType().createBlockData());
            transformationMatrix.identity()
                    .scale(scale)
                    .rotateY(DEGREES_180)
                    .translate(-0.5f, 0.0f, -0.5f);
        }
        display.setTransformationMatrix(transformationMatrix);
        display.setTeleportDuration(1);
    }

    private void createLeashEntity() {
        // Some entities (e.g., item displays) can't be leashed directly, so create a separate leash entity
        if (!(entity.getBukkitEntity() instanceof Mob)) {
            leashEntity = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.RABBIT, entityTracker);

            if (leashEntity.getBukkitEntity() instanceof Ageable ageable) {
                ageable.setBaby();
                ageable.setInvisible(true);
            }
            leashEntity.setLeashHolder(player);
            leashEntity.setPositionRotation(location);
        }
    }

    private void updateLocation() {
        double xOffset = player.getMainHand() == MainHand.RIGHT ? OFFSET_VECTOR.getX() : -OFFSET_VECTOR.getX();
        offsetVector.setX(xOffset);
        offsetVector.setY(OFFSET_VECTOR.getY());
        offsetVector.setZ(OFFSET_VECTOR.getZ());

        player.getLocation(targetLocation)
                .add(MathUtil.rotateAroundAxisY(offsetVector, -FastMathUtil.toRadians(targetLocation.getYaw())));
        targetLocation.setPitch(0.0f);

        location.setWorld(targetLocation.getWorld());
    }

    private void updatePhysics() {
        updateLocation();

        updateRotation();
        storePreviousPosition();
        applyInertia();
        applyWindAndTurbulence();
        updatePosition();
        constrainToLeashLength(targetLocation);
        calculateNewInertia();
        updateRotationFromMovement();
    }

    private void updateRotation() {
        rotationSpeed *= ROTATION_DECAY;

        if (Math.abs(rotationSpeed) < MIN_ROTATION_SPEED) {
            rotationSpeed = rotationSpeed >= 0.0f ? MIN_ROTATION_SPEED : -MIN_ROTATION_SPEED;
        }
        angle += rotationSpeed;
    }

    private void storePreviousPosition() {
        temp.setX(location.getX());
        temp.setY(location.getY());
        temp.setZ(location.getZ());
    }

    private void applyInertia() {
        inertia.multiply(INERTIA_DECAY);

        if (inertia.getY() < Y_INERTIA) {
            inertia.setY(Math.min(Y_INERTIA, inertia.getY() + Y_INERTIA));
        }
    }

    private void applyWindAndTurbulence() {
        windPhase += TURBULENCE_FREQUENCY;
        double windMagnitude = Math.sin(windPhase) * WIND_STRENGTH;

        windForce.setX(windDirection.getX() * windMagnitude);
        windForce.setY(windDirection.getY() * windMagnitude);
        windForce.setZ(windDirection.getZ() * windMagnitude);

        // Random turbulence
        add(windForce,
                (RANDOM.nextDouble() - 0.5) * WIND_STRENGTH,
                (RANDOM.nextDouble() - 0.5) * WIND_STRENGTH,
                (RANDOM.nextDouble() - 0.5) * WIND_STRENGTH
        );
        inertia.add(windForce);
    }

    private void updatePosition() {
        location.add(inertia.getX(), inertia.getY(), inertia.getZ());
        location.setYaw((float) angle);
    }

    private void constrainToLeashLength(Location targetLocation) {
        double distance = targetLocation.distance(location);

        if (distance > LEASH_LENGTH) {
            double percentage = LEASH_LENGTH / distance;
            location.setX(linearInterpolate(targetLocation.getX(), location.getX(), percentage));
            location.setY(linearInterpolate(targetLocation.getY(), location.getY(), percentage));
            location.setZ(linearInterpolate(targetLocation.getZ(), location.getZ(), percentage));
        }
    }

    private void calculateNewInertia() {
        inertia.setX(location.getX() - temp.getX());
        inertia.setY(location.getY() - temp.getY());
        inertia.setZ(location.getZ() - temp.getZ());
    }

    private void updateRotationFromMovement() {
        rotationSpeed += (float) ((inertia.getX() - inertia.getZ()) * ROTATION_MULTIPLIER);
    }

    private void spawnPopEffects() {
        if (entity == null) {
            return;
        }
        Location effectLocation = entity.getPreviousLocation();
        effectLocation.setY(effectLocation.getY() + entity.getBukkitEntity().getHeight() * 0.5);

        effectLocation.getWorld().playSound(effectLocation, Sound.ENTITY_CHICKEN_EGG, 0.5f, 0.0f);
        effectLocation.getWorld().spawnParticle(Particle.CLOUD, effectLocation, 10, 0.15f, 0.15f, 0.15f, 0.05f);
    }

    private static Vector add(Vector vector, double x, double y, double z) {
        vector.setX(vector.getX() + x);
        vector.setY(vector.getY() + y);
        vector.setZ(vector.getZ() + z);
        return vector;
    }

    private static Vector sub(Vector vector, double x, double y, double z) {
        return add(vector, -x, -y, -z);
    }

    private static double linearInterpolate(double y1, double y2, double mu) {
        return y1 == y2 ? y1 : y1 * (1 - mu) + y2 * mu;
    }
}