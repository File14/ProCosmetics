package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.MathUtil;

public class Skeleton implements MorphBehavior {

    private static final ItemStack ITEM_STACK = new ItemStack(Material.BONE);

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        return InteractionResult.NO_ACTION;
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (!player.isSneaking()) {
            Location location = player.getLocation();
            Location explodeLocation = location.clone().add(0.0d, 0.5d, 0.0d);

            EntityTracker entityTracker = new EntityTrackerImpl();

            for (int i = 0; i < 15; i++) {
                double x = MathUtil.randomRange(-0.8d, 0.8d);
                double y = MathUtil.randomRange(0.1d, 0.7d);
                double z = MathUtil.randomRange(-0.8d, 0.8d);

                NMSEntity itemEntity = context.getPlugin().getNMSManager().createEntity(player.getWorld(), EntityType.ITEM, entityTracker);
                itemEntity.setEntityItemStack(ITEM_STACK);
                itemEntity.setVelocity(x, y, z);
                itemEntity.setPositionRotation(explodeLocation);
            }
            entityTracker.startTracking();
            entityTracker.destroyAfter(60);

            for (Player pushPlayer : MathUtil.getClosestPlayersFromLocation(location, 3.0d)) {
                if (pushPlayer != player) {
                    MathUtil.pushEntity(pushPlayer, location, 1.0d, 0.5d);
                }
            }
            player.getWorld().playSound(player, Sound.ENTITY_SKELETON_HURT, 0.8f, 1.0f);
            location.getWorld().spawnParticle(Particle.CLOUD, location.add(0.0d, 1.0d, 0.0d), 10, 0.3d, 0.3d, 0.3d, 0.0d);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }

    @Override
    public boolean hasAttackAnimation() {
        return true;
    }

    @Override
    public boolean hasItemHoldAnimation() {
        return true;
    }
}