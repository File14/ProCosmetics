package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.material.Materials;

import javax.annotation.Nullable;

public class Confetti implements GadgetBehavior {

    private static final double SPREAD = 0.5d;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        Location location = player.getEyeLocation();
        Vector vector = player.getLocation().getDirection();

        player.getWorld().playSound(location, Sound.ENTITY_BLAZE_SHOOT, 0.8f, 2.0f);

        for (int i = 0; i < 3; ++i) {
            location.getWorld().spawnParticle(Particle.FIREWORK,
                    location,
                    0,
                    vector.getX(),
                    vector.getY(),
                    vector.getZ(),
                    0.3d
            );
        }

        for (int i = 0; i < 50; ++i) {
            location.getWorld().spawnParticle(Particle.ITEM,
                    location,
                    0,
                    vector.getX() + MathUtil.randomRange(-SPREAD, SPREAD),
                    vector.getY() + MathUtil.randomRange(-SPREAD, SPREAD),
                    vector.getZ() + MathUtil.randomRange(-SPREAD, SPREAD),
                    0.8d,
                    Materials.getRandomWoolItem()
            );
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}