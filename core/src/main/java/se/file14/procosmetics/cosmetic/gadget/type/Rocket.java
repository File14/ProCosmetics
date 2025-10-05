package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.FallingBlocksStructure;

import javax.annotation.Nullable;

public class Rocket implements GadgetBehavior {

    private static final double Y_COLLISION_CHECK = 10.0d;

    private static final double MAX_SPEED = 0.5d;
    private static final double ACCELERATION = 0.01d;

    private FallingBlocksStructure structure;
    private NMSEntity nmsArmorStand;
    private ArmorStand seat;
    private boolean launching;
    private int tick;
    private double speed;
    private Location seatLocation;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new FallingBlocksStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        seatLocation = player.getLocation();
        structure.spawn(seatLocation);
        seatLocation = seatLocation.add(0.0d, 1.5d, 0.0d);

        seat = seatLocation.getWorld().spawn(seatLocation, ArmorStand.class, entity -> {
            entity.setVisible(false);
            entity.setGravity(false);

            MetadataUtil.setCustomEntity(entity);
        });
        seat.addPassenger(player);
        nmsArmorStand = context.getPlugin().getNMSManager().entityToNMSEntity(seat);
        nmsArmorStand.setNoClip(true);

        context.getUser().setFallDamageProtection(25);

        speed = 0.01d;
        tick = 0;
        return InteractionResult.SUCCESS;
    }

    private void addY(Location location, double amount) {
        location.setY(location.getY() + amount);
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (seat == null) {
            return;
        }
        seat.getLocation(seatLocation);

        if (launching) {
            if (speed < MAX_SPEED) {
                speed += ACCELERATION;
                addY(seatLocation, speed * 1.1d);
                nmsArmorStand.setPositionRotation(seatLocation);
            } else {
                addY(seatLocation, speed);
                nmsArmorStand.setPositionRotation(seatLocation);
            }

            for (NMSEntity entity : structure.getPlacedEntries()) {
                addY(entity.getPreviousLocation(), speed);
                entity.setPositionRotation(entity.getPreviousLocation());
                entity.setVelocity(0.0d, speed, 0.0d);
                entity.sendVelocityPacket();
            }
            addY(seatLocation, -1.0d);
            seatLocation.getWorld().spawnParticle(Particle.FLAME, seatLocation, 10, 0.2f, 0.2f, 0.2f, 0.0d);
            seatLocation.getWorld().spawnParticle(Particle.CLOUD, seatLocation, 10, 0.2f, 0.2f, 0.2f, 0.0d);
            seatLocation.getWorld().spawnParticle(Particle.EXPLOSION, seatLocation, 1, 0.2f, 0.2f, 0.2f, 0.0d);
            seatLocation.getWorld().playSound(seatLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.8f, 0.0f);

            if (tick > 190 && tick < 200) {
                seatLocation.getWorld().playSound(seatLocation, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4f, 0.0f);
                seatLocation.getWorld().playSound(seatLocation, Sound.BLOCK_ANVIL_LAND, 0.4f, 0.0f);
            } else if (tick == 200) {
                explode(context, seatLocation);
            }
            addY(seatLocation, Y_COLLISION_CHECK);
            Material topMaterial = seatLocation.getBlock().getType();

            if (!topMaterial.isAir()) {
                explode(context, seatLocation);
                return;
            }
        } else {
            addY(seatLocation, -1.0d);
            seatLocation.getWorld().playSound(seatLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.1f, 0.0f);

            if (tick % 20 == 0) {
                if (tick == 100) {
                    launching = true;
                    seatLocation.getWorld().playSound(seatLocation, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.0f);
                    seatLocation.getWorld().playSound(seatLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.0f);
                }
                seatLocation.getWorld().spawnParticle(Particle.CLOUD, seatLocation, 20, 0.2f, 0.2f, 0.2f, 0.0d);
            }
        }
        tick++;
    }

    private void explode(CosmeticContext<GadgetType> context, Location location) {
        onUnequip(context);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.0f);
        location.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, location, 0);
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        launching = false;

        if (seat != null) {
            seat.remove();
            seat = null;
            nmsArmorStand = null;
        }
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