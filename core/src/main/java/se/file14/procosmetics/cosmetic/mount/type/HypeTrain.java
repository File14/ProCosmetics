package se.file14.procosmetics.cosmetic.mount.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.nms.NMSEntityImpl;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.material.Materials;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HypeTrain implements MountBehavior, Listener {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private static final ItemStack DIAMOND_ITEM = new ItemStack(Material.DIAMOND_BLOCK);

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private final List<TrainData> carriages = new ArrayList<>();
    private int ticks;

    private static Vector getTrajectory2d(Location from, Location to) {
        return getTrajectory2d(from.toVector(), to.toVector());
    }

    private static Vector getTrajectory2d(Vector from, Vector to) {
        return to.clone().subtract(from).normalize();
    }

    private Player player;
    private Entity train;

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
        player = context.getPlayer();
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        nmsEntity.setNoClip(true);
        Player player = context.getPlayer();

        if (entity instanceof ArmorStand armorStand) {
            armorStand.setSmall(true);
            armorStand.getEquipment().setHelmet(DIAMOND_ITEM);
            armorStand.setVisible(false);

            entity.addPassenger(player);

            carriages.add(new TrainData(nmsEntity, player));
        }
        train = entity;
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        Player player = context.getPlayer();
        Location before = null;
        Vector vector = player.getLocation(location).getDirection().multiply(0.3d);

        Location location = entity.getLocation().add(0.0d, 0.7d, 0.0d);
        Material material = location.getBlock().getType();

        if (material.isSolid()) {
            context.getUser().removeCosmetic(context.getType().getCategory(), false, true);
            player.teleport(location.subtract(vector));
            player.getWorld().playSound(location, Sound.ENTITY_CHICKEN_EGG, 0.8f, 1.0f);
            return;
        }
        entity.setVelocity(vector);

        // TODO: Can this be optimized? to avoid .clone()
        for (TrainData trainData : carriages) {
            NMSEntity nmsEntity2 = trainData.nmsEntity();
            Player rider = trainData.player;
            ArmorStand armorStand = (ArmorStand) nmsEntity2.getBukkitEntity();
            armorStand.getLocation(location);

            if (before != null) {
                Location tp = before.clone().add(getTrajectory2d(before, location));
                tp.setPitch(location.getPitch());
                tp.setYaw(location.getYaw());

                tp.setDirection(before.getDirection());

                nmsEntity2.setPositionRotation(tp);
            } else {
                location.setDirection(rider.getLocation().getDirection());
                nmsEntity2.setPositionRotation(location);
            }
            before = location;

            if (ticks % 20 == 0 && rider.isOnline()) {
                User user = context.getPlugin().getUserManager().getConnected(rider);

                if (user != null) {
                    user.sendActionBar(Component.text(context.getType().getName(user) + ": " + carriages.size(), NamedTextColor.YELLOW));
                }
            }
        }

        if (++ticks > 360) {
            ticks = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        context.getUser().setFallDamageProtection(10);
        clearTrain();
    }

    @EventHandler
    public void onClickedCarriage(PlayerInteractAtEntityEvent event) {
        Entity clickedEntity = event.getRightClicked();

        if (clickedEntity instanceof Player || clickedEntity instanceof ArmorStand) {
            Player clicker = event.getPlayer();

            if (clicker.isInsideVehicle() || clicker.isSneaking()) {
                return;
            }

            if (clickedEntity == player) {
                spawnArmorStand(clicker);
            }

            for (TrainData wagon : carriages) {
                if (wagon.nmsEntity.getBukkitEntity() == clickedEntity) {
                    spawnArmorStand(clicker);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        Entity dismounted = event.getDismounted();

        if (dismounted == train) {
            User user = PLUGIN.getUserManager().getConnected(player);

            if (user != null) {
                user.removeCosmetic(PLUGIN.getCategoryRegistries().gadgets(), false, true);
            }
            return;
        }
        Entity entity = event.getEntity();
        dismountTrainWagon(entity, dismounted);
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();

        dismountTrainWagon(player, vehicle);
    }

    private void spawnArmorStand(Player target) {
        Location tail = player.getLocation();
        int carriageAmount = carriages.size();

        if (carriageAmount > 1) {
            tail = carriages.get(carriageAmount - 1).nmsEntity().getBukkitEntity().getLocation();
        }

        if (carriageAmount > 2) {
            tail.add(getTrajectory2d(carriages.get(carriageAmount - 2).nmsEntity().getBukkitEntity().getLocation(),
                    carriages.get(carriageAmount - 1).nmsEntity().getBukkitEntity().getLocation()
            ));
        } else {
            tail.subtract(player.getLocation().getDirection().setY(0));
        }

        ArmorStand armorStand = tail.getWorld().spawn(tail, ArmorStand.class, entity -> {
            entity.setVisible(false);
            entity.setGravity(false);
            entity.setSmall(true);
            entity.setHelmet(Materials.getRandomWoolItem());
            MetadataUtil.setCustomEntity(entity);
        });
        armorStand.teleport(tail);

        tail.getWorld().playSound(tail, Sound.ENTITY_HORSE_SADDLE, 0.5f, 1.0f);

        armorStand.addPassenger(target);
        NMSEntityImpl nmsEntity = PLUGIN.getNMSManager().entityToNMSEntity(armorStand);
        nmsEntity.setNoClip(true);
        carriages.add(new TrainData(nmsEntity, target));
    }

    private void dismountTrainWagon(Entity entity, Entity vehicle) {
        if (entity instanceof Player && vehicle instanceof ArmorStand && entity != player) {
            Optional<TrainData> optionalTrainData = carriages.stream().filter(
                    trainData1 -> trainData1.nmsEntity().getBukkitEntity() == vehicle).findAny();

            if (optionalTrainData.isPresent()) {
                TrainData trainData = optionalTrainData.get();
                carriages.remove(trainData);
                vehicle.eject();
                vehicle.remove();
            }
        }
    }

    public void clearTrain() {
        for (TrainData trainData : carriages) {
            Location location = trainData.nmsEntity().getPreviousLocation();

            if (location != null) {
                location.getWorld().spawnParticle(Particle.CLOUD, location, 3, 0.0, 0.0, 0.0, 0.0);
            }
            trainData.nmsEntity().getBukkitEntity().remove();
        }
        carriages.clear();
    }

    private record TrainData(NMSEntity nmsEntity, Player player) {
    }
}