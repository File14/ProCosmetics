package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.morph.FlyableMorph;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

public class Wither extends FlyableMorph implements Listener {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private WitherSkull skull;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        super.onEquip(context);
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        if (event.getAction() == Action.LEFT_CLICK_AIR && skull == null) {
            Player player = context.getPlayer();

            skull = player.launchProjectile(WitherSkull.class);
            MetadataUtil.setCustomEntity(skull);
            player.getWorld().playSound(player, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);

            context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), this::despawnSkull, 60L);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        super.onUpdate(context, nmsEntity);
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        super.onUnequip(context);
        despawnSkull();
    }

    @EventHandler
    public void onSkullExplode(EntityExplodeEvent event) {
        if (event.getEntity() == skull) {
            event.setCancelled(true);

            Location location = skull.getLocation();
            location.getWorld().playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 1.0f);
            location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1, 0.1d, 0.1d, 0.1d, 0.1d);

            for (Player hitPlayer : MathUtil.getClosestPlayersFromLocation(location, 2.5d)) {
                User otherUser = PLUGIN.getUserManager().getConnected(hitPlayer);

                if (otherUser != null) {
                    otherUser.setFallDamageProtection(6);
                }
                hitPlayer.setVelocity(new Vector(MathUtil.randomRange(-0.5d, 0.5d),
                        MathUtil.randomRange(0.8d, 1.5d),
                        MathUtil.randomRange(-0.5d, 0.5d)
                ));
            }
            despawnSkull();
        }
    }

    private void despawnSkull() {
        if (skull != null) {
            skull.remove();
            skull = null;
        }
    }
}