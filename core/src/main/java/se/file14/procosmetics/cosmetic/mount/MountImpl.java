package se.file14.procosmetics.cosmetic.mount;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.mount.Mount;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.event.CosmeticEntitySpawnEvent;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.util.MetadataUtil;

public class MountImpl extends CosmeticImpl<MountType, MountBehavior> implements Mount {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    private final boolean rideOnSpawn;
    private final boolean despawnOnDismount;

    protected NMSEntity nmsEntity;
    protected Entity entity;

    public MountImpl(ProCosmeticsPlugin plugin, User user, MountType type, MountBehavior behavior) {
        super(plugin, user, type, behavior);

        Config config = type.getCategory().getConfig();

        this.rideOnSpawn = config.getBoolean("ride_on_spawn");
        this.despawnOnDismount = config.getBoolean("despawn_on_dismount");
    }

    @Override
    protected void onEquip() {
        user.removeCosmetic(plugin.getCategoryRegistries().morphs(), false, true);
        spawn();

        if (rideOnSpawn && entity.getPassengers().contains(player)) {
            entity.addPassenger(player);
        }
        runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    protected void onUpdate() {
        if (nmsEntity != null && entity.isValid()) {
            behavior.onUpdate(this, entity, nmsEntity);

            // Follow player around
            if (entity instanceof LivingEntity && player.getVehicle() != entity && user.isMoving()) {
                nmsEntity.follow(player);
            }
        }
    }

    @Override
    protected void onUnequip() {
        if (nmsEntity != null) {
            nmsEntity = null;
        }

        if (entity != null) {
            entity.eject();
            entity.remove();
            entity = null;
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Entity clickedEntity = event.getRightClicked();

        if (clickedEntity != entity) {
            return;
        }
        Player clicker = event.getPlayer();

        if (clicker.equals(player)) {
            if (!player.isInsideVehicle()) {
                entity.addPassenger(player);
            }
        } else {
            User clickUser = plugin.getUserManager().getConnected(clicker);

            if (clickUser != null) {
                clickUser.sendMessage(clickUser.translate("cosmetic.mounts.equip.not_owner"));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.FALL && event.getEntity() == player
                && player.getVehicle() == entity) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted() == entity && despawnOnDismount) {
            user.removeCosmetic(cosmeticType.getCategory(), false, true);
        }
    }

    // For custom movement so the entity does not move around with its AI while riding it
    @EventHandler(ignoreCancelled = true)
    public void onCustomMount(EntityMountEvent event) {
        if (event.getMount() == entity && event.getMount() instanceof LivingEntity livingEntity) {
            livingEntity.setAI(false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCustomDismount(EntityDismountEvent event) {
        if (event.getDismounted() == entity && event.getDismounted() instanceof LivingEntity livingEntity) {
            livingEntity.setAI(true);
        }
    }

    @Override
    public void spawn() {
        spawn(player.getLocation());
    }

    @Override
    public void spawn(Location location) {
        if (entity != null) {
            entity.remove();
        }
        entity = location.getWorld().spawn(location, cosmeticType.getEntityType().getEntityClass(), entity -> {
            entity.setCustomName(SERIALIZER.serialize(user.translate(
                    "cosmetic.pets.name_tag",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("cosmetic", cosmeticType.getName(user))))
            );

            if (entity instanceof LivingEntity livingEntity) {
                AttributeInstance attribute = livingEntity.getAttribute(Attribute.MAX_HEALTH);

                if (attribute != null) {
                    double health = 2.0d;
                    attribute.setBaseValue(health);
                    livingEntity.setHealth(health);
                }
            }
            nmsEntity = plugin.getNMSManager().entityToNMSEntity(entity);
            MetadataUtil.setCustomEntity(entity);
            behavior.setupEntity(this, entity, nmsEntity);
        });

        CosmeticEntitySpawnEvent event = new CosmeticEntitySpawnEvent(user, player, entity);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public NMSEntity getNMSEntity() {
        return nmsEntity;
    }
}