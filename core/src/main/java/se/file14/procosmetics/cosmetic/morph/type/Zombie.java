package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

public class Zombie implements MorphBehavior, Listener {

    private static final double RANGE = 24.0d;

    private org.bukkit.entity.Zombie zombie;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity entity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR && zombie == null) {
            Location location = player.getLocation();

            zombie = location.getWorld().spawn(location, org.bukkit.entity.Zombie.class,
                    entity -> {
                        entity.setBaby();
                        entity.setVelocity(location.getDirection().setY(0.6d));

                        MetadataUtil.setCustomEntity(entity);
                    }
            );

            for (Player targetPlayer : MathUtil.getClosestPlayersFromLocation(location, RANGE)) {
                if (targetPlayer == player) {
                    continue;
                }
                zombie.setTarget(targetPlayer);
                break;
            }

            context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), () -> {
                if (zombie != null && zombie.isValid()) {
                    zombie.getLocation(location);

                    for (int i = 0; i < 10; i++) {
                        location.getWorld().spawnParticle(Particle.FIREWORK, location, 0,
                                MathUtil.randomRange(-2.0d, 2.0d),
                                MathUtil.randomRange(-2.0d, 2.0d),
                                MathUtil.randomRange(-2.0d, 2.0d),
                                0.1d
                        );
                    }
                    zombie.getWorld().playSound(location, Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
                    zombie.remove();
                    zombie = null;
                }
            }, 160L);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        if (zombie != null) {
            zombie.remove();
            zombie = null;
        }
    }

    @Override
    public boolean hasAttackAnimation() {
        return true;
    }

    @Override
    public boolean hasItemHoldAnimation() {
        return true;
    }

    @EventHandler
    public void onZombieHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player hitPlayer && event.getDamager() == zombie) {
            Location location = zombie.getLocation();

            MathUtil.pushEntity(hitPlayer, location, 0.6d, 0.5d);

            hitPlayer.playHurtAnimation(0.0f);
            hitPlayer.getWorld().playSound(hitPlayer, Sound.ENTITY_PLAYER_HURT, 0.5f, 1.0f);

            location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);

            zombie.getWorld().playSound(zombie, Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);

            zombie.remove();
            zombie = null;

            event.setCancelled(true);
        }
    }
}