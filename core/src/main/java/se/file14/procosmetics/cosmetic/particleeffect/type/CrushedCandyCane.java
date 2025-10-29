package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;

import java.util.List;

public class CrushedCandyCane implements ParticleEffectBehavior {

    private static final List<ItemStack> CANDY_ITEMS = List.of(
            new ItemStack(Material.RED_DYE),
            new ItemStack(Material.GREEN_DYE),
            new ItemStack(Material.BONE_MEAL));

    private static final double BASE_HEIGHT_OFFSET = 0.6d;

    private static final int MOVING_PARTICLE_COUNT = 2;
    private static final double MOVING_SPREAD = 0.2d;

    private static final double STATIC_HEIGHT_OFFSET = 1.8d;
    private static final float STATIC_ORBIT_RADIUS = 1.0f;
    private static final float STATIC_ROTATION_SPEED = 10.0f;
    private static final int STATIC_PARTICLE_COUNT = 3;
    private static final double STATIC_SPREAD = 0.2d;

    private int rotationTicks = 0;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, BASE_HEIGHT_OFFSET, 0.0d);

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
        for (ItemStack item : CANDY_ITEMS) {
            location.getWorld().spawnParticle(
                    Particle.ITEM,
                    location,
                    MOVING_PARTICLE_COUNT,
                    MOVING_SPREAD,
                    MOVING_SPREAD,
                    MOVING_SPREAD,
                    0.0d,
                    item);
        }
    }

    private void spawnStaticEffect(Location location) {
        float rotationAngle = STATIC_ROTATION_SPEED * FastMathUtil.toRadians(rotationTicks);
        float offsetX = STATIC_ORBIT_RADIUS * FastMathUtil.cos(rotationAngle);
        float offsetZ = STATIC_ORBIT_RADIUS * FastMathUtil.sin(rotationAngle);

        location.add(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);

        for (ItemStack item : CANDY_ITEMS) {
            location
                    .getWorld()
                    .spawnParticle(
                            Particle.ITEM,
                            location,
                            STATIC_PARTICLE_COUNT,
                            STATIC_SPREAD,
                            STATIC_SPREAD,
                            STATIC_SPREAD,
                            0.0d,
                            item);
        }

        location.subtract(offsetX, STATIC_HEIGHT_OFFSET, offsetZ);

        if (rotationTicks++ >= 360) {
            rotationTicks = 0;
        }
    }
}
