package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.FallingBlocksStructure;

import javax.annotation.Nullable;

public class Cannon implements GadgetBehavior {

    private static final double PARTICLE_OFFSET = 0.5d;

    private FallingBlocksStructure structure;
    private Location center;
    private Location shootLocation;
    private Entity fireball;
    private int tick;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new FallingBlocksStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        center = context.getPlayer().getLocation();

        double angle = structure.spawn(center);
        shootLocation = center.clone().add(MathUtil.rotateAroundAxisY(new Vector(0.0d, 1.0d, 4.6d), angle));
        shootLocation.add(0.0, PARTICLE_OFFSET, 0.0d);

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (shootLocation == null) {
            return;
        }
        tick++;

        if (tick < 100) {
            shootLocation.getWorld().spawnParticle(Particle.LARGE_SMOKE, shootLocation, 0);
        } else if (tick == 100) {
            shootLocation.subtract(0.0, PARTICLE_OFFSET, 0.0d);

            fireball = shootLocation.getWorld().spawn(shootLocation, Fireball.class, entity -> {
                entity.setShooter(context.getPlayer());
                entity.setVisualFire(false);

                MetadataUtil.setCustomEntity(entity);
            });
            shootLocation.getWorld().spawnParticle(Particle.EXPLOSION, shootLocation, 1);
            shootLocation.getWorld().playSound(shootLocation, Sound.ENTITY_GHAST_SHOOT, 0.5f, 1.0f);
        }

        if (fireball != null && fireball.getTicksLived() > 200) {
            fireball.remove();
            fireball = null;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();

        if (fireball != null) {
            fireball.remove();
            fireball = null;
        }
        tick = 0;
        shootLocation = null;
        center = null;
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return structure.isEnoughSpace(location);
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}