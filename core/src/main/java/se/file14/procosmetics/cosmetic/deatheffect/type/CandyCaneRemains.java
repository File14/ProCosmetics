package se.file14.procosmetics.cosmetic.deatheffect.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class CandyCaneRemains implements DeathEffectBehavior {

    private static final List<ItemStack> ITEMS = List.of(
            new ItemStack(Material.RED_DYE),
            new ItemStack(Material.GREEN_DYE),
            new ItemStack(Material.BONE_MEAL)
    );

    private static final int AMOUNT = 50;
    private static final double SPREAD = 5.0d;

    @Override
    public void onEquip(CosmeticContext<DeathEffectType> context) {
    }

    @Override
    public void playEffect(CosmeticContext<DeathEffectType> context, Location location) {
        location.add(0.0d, 1.0d, 0.0d);

        for (int i = 0; i < AMOUNT; i++) {
            for (ItemStack itemStack : ITEMS) {
                location.getWorld().spawnParticle(Particle.ITEM,
                        location,
                        0,
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        0.1d,
                        itemStack
                );
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<DeathEffectType> context) {
    }
}