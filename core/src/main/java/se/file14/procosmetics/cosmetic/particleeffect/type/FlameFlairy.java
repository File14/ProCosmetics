package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class FlameFlairy implements ParticleEffectBehavior {

    private final Vector velocity = new Vector();
    private final Vector tempVector = new Vector();
    private final Location currentLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location goalLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location tempLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private int idleTicksRemaining;
    private int ticksSinceGoalChange;

    private static final double MAX_SPEED = 3.0d;
    private static final double IDLE_SPEED = 0.01d;
    private static final double ACCELERATION = 0.01d;
    private static final double STEERING_FORCE = 0.2d;
    private static final double ORBIT_RADIUS = 2.5d;
    private static final double HEIGHT_OFFSET = 1.0d;
    private static final double DISTANCE_BEHIND = 1.5d;

    private static final double GOAL_REACHED_DIST_SQ = 0.25d;
    private static final double TOO_FAR_DIST_SQ = 64.0d;
    private static final double IDLE_CHECK_DIST_SQ = 16.0d;
    private static final double IDLE_CANCEL_DIST_SQ = 25.0d;
    private static final double SLOW_DOWN_DIST_SQ = 2.25d;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
        context.getPlayer().getLocation(tempLocation);
        currentLocation.setWorld(tempLocation.getWorld());
        currentLocation.setX(tempLocation.getX());
        currentLocation.setY(tempLocation.getY() + HEIGHT_OFFSET);
        currentLocation.setZ(tempLocation.getZ());

        velocity.zero();
        updateGoalLocation(context);
        idleTicksRemaining = 0;
        ticksSinceGoalChange = 0;
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        context.getPlayer().getLocation(tempLocation);
        World world = tempLocation.getWorld();

        if (currentLocation.getWorld() != world) {
            currentLocation.setWorld(world);
            currentLocation.setX(tempLocation.getX());
            currentLocation.setY(tempLocation.getY() + HEIGHT_OFFSET);
            currentLocation.setZ(tempLocation.getZ());
            velocity.zero();
        }

        if (goalLocation.getWorld() != world) {
            updateGoalLocation(context);
        }
        tempLocation.add(0, HEIGHT_OFFSET, 0);

        double distanceToPlayerSq = tempLocation.distanceSquared(currentLocation);
        double distanceToGoalSq = currentLocation.distanceSquared(goalLocation);

        ticksSinceGoalChange++;

        // Pick new goal if reached or too far from player or stayed too long
        if (distanceToGoalSq < GOAL_REACHED_DIST_SQ || distanceToPlayerSq > TOO_FAR_DIST_SQ || ticksSinceGoalChange > 80) {
            updateGoalLocation(context);
            ticksSinceGoalChange = 0;
        }
        // Random idle behavior
        boolean isIdling = idleTicksRemaining > 0;

        if (!isIdling && distanceToPlayerSq < IDLE_CHECK_DIST_SQ && MathUtil.randomRange(0.0d, 1.0d) > 0.97d) {
            idleTicksRemaining = (int) MathUtil.randomRange(10, 40);
        }

        if (isIdling && distanceToPlayerSq > IDLE_CANCEL_DIST_SQ) {
            idleTicksRemaining = 0;
        }

        if (idleTicksRemaining > 0) {
            idleTicksRemaining--;
        }
        // Calculate steering force towards goal
        tempVector.setX(goalLocation.getX() - currentLocation.getX());
        tempVector.setY(goalLocation.getY() - currentLocation.getY());
        tempVector.setZ(goalLocation.getZ() - currentLocation.getZ());

        double distance = tempVector.length();
        if (distance > 0.001d) {
            tempVector.normalize();

            // Calculate desired speed based on distance - further = faster
            double desiredSpeed = isIdling ? IDLE_SPEED : Math.min(MAX_SPEED, distance * ACCELERATION);

            // Slow down when very close to goal
            if (distanceToGoalSq < SLOW_DOWN_DIST_SQ && distanceToPlayerSq < IDLE_CHECK_DIST_SQ) {
                desiredSpeed *= (distance / 1.5d);
            }
            tempVector.multiply(desiredSpeed);

            // Steering = desired - current
            tempVector.subtract(velocity);
            tempVector.multiply(STEERING_FORCE);

            // Apply steering force (acceleration)
            velocity.add(tempVector);

            // Limit velocity
            double speed = velocity.length();

            if (speed > MAX_SPEED) {
                velocity.normalize().multiply(MAX_SPEED);
            }
        }
        // Apply velocity to position
        currentLocation.add(velocity.getX(), velocity.getY(), velocity.getZ());

        world.spawnParticle(Particle.FLAME,
                currentLocation.getX(),
                currentLocation.getY(),
                currentLocation.getZ(),
                1, 0.02d, 0.02d, 0.02d, 0.0d);

        world.spawnParticle(Particle.LAVA,
                currentLocation.getX(),
                currentLocation.getY(),
                currentLocation.getZ(),
                1, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    private void updateGoalLocation(CosmeticContext<ParticleEffectType> context) {
        context.getPlayer().getLocation(tempLocation);

        double yaw = tempLocation.getYaw();
        double yawRadians = FastMathUtil.toRadians(yaw + 180.0f);

        // Random offset behind the player
        double angleOffset = MathUtil.randomRange(-Math.PI * 0.4d, Math.PI * 0.4d);
        double totalAngle = yawRadians + angleOffset;

        // Random distance behind
        double distance = MathUtil.randomRange(DISTANCE_BEHIND, DISTANCE_BEHIND + ORBIT_RADIUS);

        // Calculate horizontal position behind player
        double offsetX = -Math.sin(totalAngle) * distance;
        double offsetZ = Math.cos(totalAngle) * distance;

        // Random height offset
        double heightVariation = MathUtil.randomRange(-0.5d, 1.0d);

        goalLocation.setWorld(tempLocation.getWorld());
        goalLocation.setX(tempLocation.getX() + offsetX);
        goalLocation.setY(tempLocation.getY() + HEIGHT_OFFSET + heightVariation);
        goalLocation.setZ(tempLocation.getZ() + offsetZ);
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}
