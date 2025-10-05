package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.FallingBlocksStructure;

import javax.annotation.Nullable;

public class Swing implements GadgetBehavior {

    private static final ItemStack SEAT_ITEM = new ItemStack(Material.SPRUCE_SLAB);

    private static final double ACCELERATION = 0.01d;
    private static final double MAX_ACCELERATION = 1.0d;

    private FallingBlocksStructure structure;
    private Location center;
    private double angle;
    private NMSEntity seat;
    private int ticks;
    private double amplifier;

    private final EntityTrackerImpl tracker = new EntityTrackerImpl();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new FallingBlocksStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        center = LocationUtil.center(player.getLocation());
        angle = structure.spawn(center);

        ArmorStand seatEntity = center.getWorld().spawn(center, ArmorStand.class, entity -> {
            entity.getEquipment().setHelmet(SEAT_ITEM);
            entity.setBasePlate(false);
            entity.setVisible(false);
            entity.setGravity(false);

            seat = context.getPlugin().getNMSManager().entityToNMSEntity(entity);
            seat.setNoClip(true);

            MetadataUtil.setCustomEntity(entity);
        });
        seatEntity.addPassenger(player);

        for (int i = 0; i < 2; i++) {
            Vector vector = MathUtil.rotateAroundAxisY(new Vector(-0.8d + i * 1.6d, 4.0d, 0.0d), angle);
            Location location = center.clone().add(vector);

            NMSEntity rabbit = context.getPlugin().getNMSManager().createEntity(center.getWorld(), EntityType.RABBIT, tracker);
            rabbit.setPositionRotation(location);
            rabbit.setBaby(true);
            rabbit.setInvisible(true);
            rabbit.setLeashHolder(seat.getBukkitEntity());
        }
        tracker.startTracking();

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (seat != null) {
            Entity entity = seat.getBukkitEntity();

            if (entity.getPassengers().isEmpty()) {
                if (amplifier > 0.0f) {
                    amplifier -= ACCELERATION;
                }
            } else {
                if (amplifier < MAX_ACCELERATION) {
                    amplifier += ACCELERATION;
                }
            }
            // Do not perform the calculations below if it does not have a positive amplifier (if the swing is not moving)
            if (amplifier < 0.0f) {
                return;
            }
            float tickRadians = FastMathUtil.toRadians(ticks * 8.0f);
            float cosValue = FastMathUtil.cos(tickRadians);

            double forward = 3.0f * amplifier * FastMathUtil.sin(tickRadians);
            double height = (2.5f - 2.5f * Math.abs(cosValue)) * amplifier - 1.0f;

            Vector vector = MathUtil.rotateAroundAxisY(new Vector(0.0f, height, forward), angle);
            seat.setPositionRotation(center.clone().add(vector));
            seat.setHeadPose((float) Math.toDegrees(-forward * 0.1), 0.0f, 0.0f);

            ticks++;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        amplifier = 0.0f;

        if (seat != null) {
            seat.getBukkitEntity().remove();
            seat = null;
        }
        tracker.destroy();
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

    @EventHandler
    public void onClickSeat(PlayerInteractAtEntityEvent event) {
        if (seat != null) {
            Entity entity = seat.getBukkitEntity();

            if (event.getRightClicked() == entity && entity.getPassengers().isEmpty()) {
                entity.addPassenger(event.getPlayer());
            }
        }
    }
}