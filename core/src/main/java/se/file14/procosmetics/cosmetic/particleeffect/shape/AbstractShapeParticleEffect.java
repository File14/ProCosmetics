package se.file14.procosmetics.cosmetic.particleeffect.shape;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public abstract class AbstractShapeParticleEffect implements ParticleEffectBehavior {

    public enum PositionMode {
        BEHIND_PLAYER,
        BELOW_PLAYER
    }

    protected final int[][] shape;
    protected final ColorProvider colorProvider;
    protected final double spacing;
    protected final double heightOffset;
    protected final double distanceBehind;
    protected final int movingParticleCount;
    protected final double movingParticleSpread;
    protected final boolean shouldRotate;
    protected final float rotationSpeed;
    protected final PositionMode positionMode;

    protected final Vector workingVector = new Vector();
    protected float currentRotation = 0.0f;

    protected AbstractShapeParticleEffect(Builder builder) {
        this.shape = builder.shape;
        this.colorProvider = builder.colorProvider;
        this.spacing = builder.spacing;
        this.heightOffset = builder.heightOffset;
        this.distanceBehind = builder.distanceBehind;
        this.movingParticleCount = builder.movingParticleCount;
        this.movingParticleSpread = builder.movingParticleSpread;
        this.shouldRotate = builder.shouldRotate;
        this.rotationSpeed = builder.rotationSpeed;
        this.positionMode = builder.positionMode;
    }

    public static abstract class Builder {

        private int[][] shape;
        private ColorProvider colorProvider = value -> Color.WHITE;
        private double spacing = 0.2d;
        private double heightOffset = 0.1d;
        private double distanceBehind = 0.4d;
        private int movingParticleCount = 3;
        private double movingParticleSpread = 0.0d;
        private boolean shouldRotate = false;
        private float rotationSpeed = 0.5f;
        private PositionMode positionMode = PositionMode.BEHIND_PLAYER;

        public Builder shape(int[][] shape) {
            this.shape = shape;
            return this;
        }

        public Builder colorProvider(ColorProvider colorProvider) {
            this.colorProvider = colorProvider;
            return this;
        }

        public Builder spacing(double spacing) {
            this.spacing = spacing;
            return this;
        }

        public Builder heightOffset(double heightOffset) {
            this.heightOffset = heightOffset;
            return this;
        }

        public Builder distanceBehind(double distanceBehind) {
            this.distanceBehind = distanceBehind;
            return this;
        }

        public Builder movingParticleCount(int count) {
            this.movingParticleCount = count;
            return this;
        }

        public Builder movingParticleSpread(double spread) {
            this.movingParticleSpread = spread;
            return this;
        }

        public Builder shouldRotate(boolean shouldRotate) {
            this.shouldRotate = shouldRotate;
            return this;
        }

        public Builder rotationSpeed(float speed) {
            this.rotationSpeed = speed;
            return this;
        }

        public Builder positionMode(PositionMode mode) {
            this.positionMode = mode;
            return this;
        }

        public abstract AbstractShapeParticleEffect build();

        protected void validate() {
            if (shape == null) {
                throw new IllegalStateException("Shape must be provided");
            }
        }
    }

    public static Builder builder() {
        throw new UnsupportedOperationException("Use a concrete subclass builder");
    }

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            renderMovingParticles(location);
        } else {
            renderStaticShape(location);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }

    protected void renderMovingParticles(Location location) {
        Color color = colorProvider.getColor(1);
        location.add(0.0d, heightOffset, 0.0d);
        location.getWorld().spawnParticle(Particle.DUST,
                location,
                movingParticleCount,
                movingParticleSpread,
                movingParticleSpread,
                movingParticleSpread,
                0.0d,
                new Particle.DustOptions(color, 1)
        );
        location.subtract(0.0d, heightOffset, 0.0d);
    }

    protected void renderStaticShape(Location location) {
        // Calculate starting positions
        double totalWidth = shape[0].length * spacing;
        double startX = -totalWidth / 2.0 + spacing / 2.0;

        // For ground effects, we want to map the shape to X and Z coordinates
        // For behind player effects, we use Y for height
        double startY, startZ;

        if (positionMode == PositionMode.BELOW_PLAYER) {
            // Ground mode: map shape rows to Z axis (depth)
            double totalDepth = shape.length * spacing;
            startZ = -totalDepth / 2.0 + spacing / 2.0;
            startY = 0; // Keep at ground level
        } else {
            // Behind player mode: map shape rows to Y axis (height)
            startY = shape.length * spacing;
            startZ = -distanceBehind;
        }

        // Calculate rotation angle based on position mode
        double angle = calculateRotationAngle(location);

        // Update rotation if needed
        if (shouldRotate) {
            updateRotation();
        }

        // Render each point in the shape
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                int value = shape[row][col];

                if (value != 0) {
                    double x = startX + (col * spacing);
                    double y, z;

                    if (positionMode == PositionMode.BELOW_PLAYER) {
                        // Ground mode: use Z for shape rows, Y stays at ground level
                        y = startY;
                        z = startZ + (row * spacing);
                    } else {
                        // Behind player mode: use Y for shape rows
                        y = startY - (row * spacing);
                        z = startZ;
                    }
                    workingVector.setX(x).setY(y).setZ(z);

                    // Apply rotation
                    Vector rotated = MathUtil.rotateAroundAxisY(workingVector, angle);

                    location.add(0.0d, heightOffset, 0.0d);
                    location.add(rotated);
                    spawnParticle(location, value);
                    location.subtract(0.0d, heightOffset, 0.0d);
                    location.subtract(rotated);
                }
            }
        }
    }

    protected double calculateRotationAngle(Location location) {
        double baseAngle;

        if (positionMode == PositionMode.BEHIND_PLAYER) {
            baseAngle = -FastMathUtil.toRadians(location.getYaw());
        } else {
            baseAngle = -FastMathUtil.toRadians(location.getYaw() + 180.0f);
        }

        if (shouldRotate) {
            if (positionMode == PositionMode.BELOW_PLAYER) {
                baseAngle = 0.0f;
            }
            baseAngle += FastMathUtil.toRadians(currentRotation);
        }
        return baseAngle;
    }

    protected void updateRotation() {
        currentRotation += rotationSpeed;

        if (currentRotation >= 360) {
            currentRotation = 0.0f;
        }
    }

    protected void spawnParticle(Location location, int value) {
        Color color = colorProvider.getColor(value);

        location.getWorld().spawnParticle(Particle.DUST,
                location,
                0,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                new Particle.DustOptions(color, 1)
        );
    }
}