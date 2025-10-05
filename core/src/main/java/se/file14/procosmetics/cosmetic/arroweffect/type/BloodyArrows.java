package se.file14.procosmetics.cosmetic.arroweffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.util.MathUtil;

public class BloodyArrows implements ArrowEffectBehavior {

    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(
            Color.fromRGB(255, 0, 0),
            1
    );
    private static final ItemStack ITEM_STACK = new ItemStack(Material.REDSTONE_BLOCK);
    private static final int HIT_AMOUNT = 20;
    private static final double HIT_SPREAD = 5.0d;

    @Override
    public void onEquip(CosmeticContext<ArrowEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ArrowEffectType> context, Location location) {
        location.getWorld().spawnParticle(Particle.DUST,
                location,
                1,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                DUST_OPTIONS
        );
    }

    @Override
    public void onArrowHit(CosmeticContext<ArrowEffectType> context, Location location) {
        for (int i = 0; i < HIT_AMOUNT; i++) {
            location.getWorld().spawnParticle(Particle.ITEM,
                    location,
                    0,
                    MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    MathUtil.randomRange(-HIT_SPREAD, HIT_SPREAD),
                    0.1d,
                    ITEM_STACK
            );
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ArrowEffectType> context) {
    }
}