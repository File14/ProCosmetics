package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.RGBFade;
import se.file14.procosmetics.util.structure.type.BlockStructure;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MerryGoRound implements GadgetBehavior {

    public static final List<CoasterHorse> COASTER_HORSES = new CopyOnWriteArrayList<>();

    private static final ItemStack SADDLE_ITEM = new ItemStack(Material.SADDLE);
    private static final List<Horse.Color> HORSE_COLORS = List.of(Horse.Color.values());
    private static final int HORSES = 7;
    private static final double RANGE = 4.4d;
    private static final double ANGLE_PER_HORSE = 360.0d / HORSES;
    private static final double LEASH_Y_OFFSET = 6.0d;
    private static final float MAX_SPEED = 4.0f;
    private static final float ACCELERATION = 0.02f;

    private BlockStructure structure;
    private float tick;
    private final EntityTracker tracker = new EntityTrackerImpl();
    private final List<CoasterHorse> coasterHorses = new ArrayList<>();
    private final RGBFade rgbFade = new RGBFade();
    private Location center;
    private float speed;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        center = player.getLocation().getBlock().getLocation().add(0.5d, 0.0d, 0.5d);
        World world = player.getWorld();
        speed = 0.0f;

        structure.spawn(center);

        for (int i = 0; i < HORSES; i++) {
            Location location = getHorseLocation(i);
            Location leashLoc = location.clone().add(0.0d, LEASH_Y_OFFSET, 0.0d);
            location.setY(getYOffset(i));

            ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class, entity -> {
                entity.setGravity(false);
                entity.setVisible(false);

                MetadataUtil.setCustomEntity(entity);
            });
            NMSEntity nmsEntityArmorStand = context.getPlugin().getNMSManager().entityToNMSEntity(armorStand);

            NMSEntity nmsEntityHorse = context.getPlugin().getNMSManager().createEntity(world, EntityType.HORSE, tracker);
            Horse horse = ((Horse) nmsEntityHorse.getBukkitEntity());
            horse.getInventory().setSaddle(SADDLE_ITEM);
            horse.setColor(HORSE_COLORS.get(i % HORSE_COLORS.size()));
            nmsEntityHorse.setPositionRotation(location);

            NMSEntity nmsEntityLeash = context.getPlugin().getNMSManager().createEntity(world, EntityType.BAT, tracker);
            nmsEntityLeash.setPositionRotation(leashLoc);
            nmsEntityLeash.setLeashHolder(nmsEntityHorse.getBukkitEntity());
            if (nmsEntityLeash.getBukkitEntity() instanceof LivingEntity livingEntity) {
                livingEntity.setInvisible(true);
            }

            CoasterHorse coasterHorse = new CoasterHorse(nmsEntityHorse, nmsEntityArmorStand, nmsEntityLeash);
            coasterHorses.add(coasterHorse);
            COASTER_HORSES.add(coasterHorse);
        }
        tracker.startTracking();

        List<Player> nearbyPlayers = MathUtil.getClosestPlayersFromLocation(center, 6.0d);

        if (!nearbyPlayers.isEmpty()) {
            Location teleport = center.clone().add(4.5d, 1.5d, 0.0d);
            teleport.setDirection(player.getLocation().getDirection());

            for (Player closePlayer : nearbyPlayers) {
                closePlayer.teleport(teleport);
            }
        }
        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    private Location getHorseLocation(float angle) {
        Location location = MathUtil.getLocationAroundCircle(center, RANGE, angle);
        location.setYaw(location.getYaw() - 90.0f);
        return location;
    }

    private double getYOffset(float angle) {
        double y = FastMathUtil.sin(angle * 2.0f);
        return center.getY() + y + 2.0d;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (center == null) {
            return;
        }

        for (int i = 0; i < coasterHorses.size(); i++) {
            CoasterHorse coasterHorse = coasterHorses.get(i);
            NMSEntity nmsHorse = coasterHorse.horse();
            NMSEntity nmsArmorStand = coasterHorse.armorStand();
            NMSEntity nmsLeash = coasterHorse.leash();

            float angle = FastMathUtil.toRadians(ANGLE_PER_HORSE * i + tick);
            Location location = getHorseLocation(angle);

            location.add(0.0d, LEASH_Y_OFFSET, 0.0d);
            nmsLeash.sendPositionRotationPacket(location);
            location.subtract(0.0d, LEASH_Y_OFFSET, 0.0d);

            location.setY(getYOffset(angle));
            nmsHorse.sendPositionRotationPacket(location);
            location.setY(location.getY() - 0.3d);
            nmsArmorStand.setPositionRotation(location);

            location.getWorld().spawnParticle(Particle.DUST, location.add(0.0d, 1.0d, 0.0d), 5, 0, 0, 0, 0.0d,
                    new Particle.DustOptions(org.bukkit.Color.fromRGB(rgbFade.getR(), rgbFade.getG(), rgbFade.getB()), 1)
            );
        }

        if (speed < MAX_SPEED) {
            speed += ACCELERATION;
        }
        rgbFade.nextRGB();

        if (tick >= 360) {
            tick = 0;
        }
        tick += speed;
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();

        tracker.destroy();

        for (CoasterHorse coasterHorse : coasterHorses) {
            coasterHorse.armorStand().getBukkitEntity().remove();
            COASTER_HORSES.remove(coasterHorse);
        }
        coasterHorses.clear();
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
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getVehicle() instanceof ArmorStand vehicle) {
            for (CoasterHorse coasterHorse : coasterHorses) {
                Entity armorStand = coasterHorse.armorStand().getBukkitEntity();

                if (vehicle == armorStand) {
                    armorStand.eject();
                    break;
                }
            }
        }
    }

    public record CoasterHorse(NMSEntity horse, NMSEntity armorStand, NMSEntity leash) {
    }
}