package se.file14.procosmetics.cosmetic.mount.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class EtherealDragon implements MountBehavior {

    private static final double SPEED_MULTIPLIER = 0.6d;

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    @Override
    public void onEquip(CosmeticContext<MountType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        nmsEntity.setNoClip(false);
        nmsEntity.setHurtTicks(-1);

        entity.addPassenger(context.getPlayer());
        entity.setSilent(true);
    }

    @Override
    public void onUpdate(CosmeticContext<MountType> context, Entity entity, NMSEntity nmsEntity) {
        if (entity.getPassengers().isEmpty()) {
            context.getUser().removeCosmetic(context.getType().getCategory(), false, true);
            return;
        }
        context.getPlayer().getLocation(location);
        nmsEntity.move(location.getDirection().multiply(SPEED_MULTIPLIER));
        nmsEntity.setYaw(location.getYaw() - 180.0f);
        nmsEntity.setPitch(location.getPitch());

        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(location), 70, 2.0d, 2.0d, 2.0d, 0.5d);
    }

    @Override
    public void onUnequip(CosmeticContext<MountType> context) {
        context.getUser().setFallDamageProtection(10);
    }
}