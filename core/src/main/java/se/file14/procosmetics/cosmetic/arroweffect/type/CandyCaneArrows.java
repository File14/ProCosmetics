package se.file14.procosmetics.cosmetic.arroweffect.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class CandyCaneArrows implements ArrowEffectBehavior {

    private static final List<ItemStack> ITEMS = List.of(
            new ItemStack(Material.RED_DYE),
            new ItemStack(Material.GREEN_DYE),
            new ItemStack(Material.BONE_MEAL)
    );

    private static final double UPDATE_SPREAD = 0.1d;
    private static final int HIT_AMOUNT = 40;
    private static final double HIT_SPREAD = 5.0d;

    @Override
    public void onEquip(CosmeticContext<ArrowEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ArrowEffectType> context, Location location) {
        for (ItemStack itemStack : ITEMS) {
            location.getWorld().spawnParticle(Particle.ITEM,
                    location,
                    1,
                    UPDATE_SPREAD,
                    UPDATE_SPREAD,
                    UPDATE_SPREAD,
                    0.0d,
                    itemStack
            );
        }
    }

    @Override
    public void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location) {
        for (int i = 0; i < HIT_AMOUNT; i++) {
            for (ItemStack itemStack : ITEMS) {
                location.getWorld().spawnParticle(Particle.ITEM,
                        location,
                        0,
                        MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                        MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                        MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                        0.1d, itemStack
                );
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ArrowEffectType> context) {
    }
}