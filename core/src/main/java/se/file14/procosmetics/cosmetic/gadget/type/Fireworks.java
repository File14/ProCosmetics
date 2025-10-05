package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;

public class Fireworks implements GadgetBehavior {

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Location location;

        if (clickedBlock != null) {
            location = clickedBlock.getLocation().add(0.5d, 1.0d, 0.5d);
        } else {
            location = context.getPlayer().getLocation();
        }
        location.getWorld().spawn(location, Firework.class, entity -> {
            FireworkMeta fireworkMeta = entity.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder()
                    .flicker(MathUtil.THREAD_LOCAL_RANDOM.nextBoolean())
                    .withColor(Color.fromBGR(MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255)))
                    .withFade(Color.fromBGR(MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255), MathUtil.randomRangeInt(0, 255)))
                    .with(FireworkEffect.Type.STAR)
                    .build());
            fireworkMeta.setPower(2);
            entity.setFireworkMeta(fireworkMeta);

            MetadataUtil.setCustomEntity(entity);
        });
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
        return false;
    }
}