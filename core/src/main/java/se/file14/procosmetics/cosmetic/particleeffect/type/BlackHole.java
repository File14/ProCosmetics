package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class BlackHole implements ParticleEffectBehavior {

    private static final Particle.DustOptions BLACK_DUST = new Particle.DustOptions(Color.BLACK, 1);

    private static final double MOVING_HEIGHT_OFFSET = 0.3d;
    private static final int MOVING_PORTAL_COUNT = 4;
    private static final double MOVING_SPREAD = 0.1d;

    private static final double STATIC_HEIGHT_OFFSET = 0.1d;
    private static final int SPIRAL_LINES = 6;
    private static final int SPIRAL_POINTS_PER_LINE = 6;
    private static final float ANGLE_PER_LINE = FastMathUtil.PI * 2.0f / SPIRAL_LINES;
    private static final float SPIRAL_RADIUS = 1.0f;
    private static final float SPIRAL_ROTATION_SPEED = 0.02f;

    private static final int FLAME_SPAWN_INTERVAL = 4;
    private static final double FLAME_MIN_DISTANCE = -1.7d;
    private static final double FLAME_MAX_DISTANCE = 1.7d;
    private static final double FLAME_MIN_HEIGHT = 0.5d;
    private static final double FLAME_MAX_HEIGHT = 0.7d;
    private static final double FLAME_SPEED = 0.08d;
    private static final int PORTAL_PARTICLE_COUNT = 3;
    private static final double PORTAL_SPREAD = 0.6d;

    private int ticks;
    private final Vector vector = new Vector();

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            spawnMovingEffect(location);
        } else {
            spawnStaticEffect(location);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    private void spawnMovingEffect(Location location) {
        location.add(0.0d, MOVING_HEIGHT_OFFSET, 0.0d);

        location.getWorld().spawnParticle(
                Particle.PORTAL,
                location,
                MOVING_PORTAL_COUNT,
                MOVING_SPREAD,
                MOVING_SPREAD,
                MOVING_SPREAD,
                0.0d);

        location.getWorld().spawnParticle(Particle.DUST, location, 1, 0.0d, 0.0d, 0.0d, 0.0d, BLACK_DUST);
    }

    private void spawnStaticEffect(Location location) {
        location.add(0.0d, STATIC_HEIGHT_OFFSET, 0.0d);

        spawnBlackHoleSpiral(location);

        if (ticks % FLAME_SPAWN_INTERVAL == 0) {
            renderSuctionFlames(location);
        }

        if (ticks++ >= 360) {
            ticks = 0;
        }
    }

    private void spawnBlackHoleSpiral(Location location) {
        float rotationAngle = SPIRAL_ROTATION_SPEED * ticks;

        for (int line = 1; line <= SPIRAL_LINES; line++) {
            for (int point = 0; point < SPIRAL_POINTS_PER_LINE; point++) {
                float progress = point / 4.0f;
                float angle = progress + ANGLE_PER_LINE * line;
                float radius = progress * SPIRAL_RADIUS;

                float x = FastMathUtil.cos(angle) * radius;
                float z = FastMathUtil.sin(angle) * radius;

                vector.setX(x).setY(0.0d).setZ(z);
                MathUtil.rotateAroundAxisY(vector, rotationAngle);

                location.add(vector);
                location.getWorld().spawnParticle(
                        Particle.DUST, location, 1, 0.0d, 0.0d, 0.0d, 0.0d, BLACK_DUST);
                location.subtract(vector);
            }
        }
    }

    private void renderSuctionFlames(Location location) {
        double offsetX = MathUtil.randomRange(FLAME_MIN_DISTANCE, FLAME_MAX_DISTANCE);
        double offsetY = MathUtil.randomRange(FLAME_MIN_HEIGHT, FLAME_MAX_HEIGHT);
        double offsetZ = MathUtil.randomRange(FLAME_MIN_DISTANCE, FLAME_MAX_DISTANCE);

        location.add(offsetX, offsetY, offsetZ);

        location.getWorld().spawnParticle(Particle.FLAME, location, 0, -offsetX, -offsetY, -offsetZ, FLAME_SPEED);
        location.subtract(offsetX, offsetY, offsetZ);

        location.getWorld().spawnParticle(
                Particle.PORTAL,
                location,
                PORTAL_PARTICLE_COUNT,
                PORTAL_SPREAD,
                0.0d,
                PORTAL_SPREAD,
                0.0d
        );
    }
}
