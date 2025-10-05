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

    private static final List<ItemStack> ITEMS = List.of(
            new ItemStack(Material.RED_DYE),
            new ItemStack(Material.GREEN_DYE),
            new ItemStack(Material.BONE_MEAL)
    );

    private static final float SPEED = 10.0f;
    private int ticks;

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, 0.6d, 0.0d);

        if (context.getUser().isMoving()) {
            for (ItemStack itemStack : ITEMS) {
                location.getWorld().spawnParticle(
                        Particle.ITEM,
                        location,
                        2,
                        0.2d,
                        0.2d,
                        0.2d,
                        0.0d,
                        itemStack
                );
            }
        } else {
            float angle = SPEED * FastMathUtil.toRadians(ticks);
            location.add(FastMathUtil.cos(angle), 1.8d, FastMathUtil.sin(angle));

            for (ItemStack itemStack : ITEMS) {
                location.getWorld().spawnParticle(
                        Particle.ITEM,
                        location,
                        3,
                        0.2d,
                        0.2d,
                        0.2d,
                        0.0d,
                        itemStack
                );
            }

            ticks++;
            if (ticks > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}