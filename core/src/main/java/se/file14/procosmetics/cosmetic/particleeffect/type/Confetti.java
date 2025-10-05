package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;

import java.util.List;
import java.util.Random;

public class Confetti implements ParticleEffectBehavior {

    private static final Random RANDOM = new Random();

    private static final List<BlockData> BLOCK_DATA = List.of(
            Material.RED_CONCRETE.createBlockData(),
            Material.PINK_CONCRETE.createBlockData(),
            Material.PURPLE_CONCRETE.createBlockData(),
            Material.BLUE_CONCRETE.createBlockData(),
            Material.CYAN_CONCRETE.createBlockData(),
            Material.YELLOW_CONCRETE.createBlockData(),
            Material.RED_CONCRETE.createBlockData(),
            Material.GREEN_CONCRETE.createBlockData(),
            Material.LIME_CONCRETE.createBlockData(),
            Material.ORANGE_CONCRETE.createBlockData()
    );

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        BlockData blockData = BLOCK_DATA.get(RANDOM.nextInt(BLOCK_DATA.size()));
        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(
                    Particle.FALLING_DUST,
                    location.add(0.0d, 0.5d, 0.0d),
                    4,
                    0.1f,
                    0.0f,
                    0.1f,
                    blockData
            );
        } else {
            location.getWorld().spawnParticle(
                    Particle.FALLING_DUST,
                    location.add(0.0d, 2.0d, 0.0d),
                    4,
                    0.4f,
                    1.0f,
                    0.4f,
                    blockData
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}