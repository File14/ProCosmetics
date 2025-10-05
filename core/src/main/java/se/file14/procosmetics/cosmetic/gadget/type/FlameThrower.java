package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;

import javax.annotation.Nullable;

public class FlameThrower implements GadgetBehavior {

    private Location location;
    boolean started;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        location = context.getPlayer().getEyeLocation();
        started = true;

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (!started) {
            return;
        }
        location = context.getPlayer().getLocation(location);

        Vector playerDirection = location.getDirection();
        Vector rotatedVector = new Vector(-playerDirection.getZ(), 0, playerDirection.getX());
        Location particleLocation = location.clone().add(rotatedVector.multiply(0.5d)).add(0.0d, 0.95d, 0.0d);

        Vector randomOffset = new Vector(
                Math.random() - Math.random(),
                Math.random() - Math.random(),
                Math.random() - Math.random()
        );
        Vector particlePath = playerDirection.clone().add(randomOffset);
        particlePath.multiply(8.0d);

        Location offsetLocation = particlePath.toLocation(location.getWorld());

        location.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0,
                offsetLocation.getX(), offsetLocation.getY(), offsetLocation.getZ(),
                0.1d
        );
        location.getWorld().playSound(location, Sound.BLOCK_FIRE_AMBIENT, 1.0f, 1.0f);
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        started = false;
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