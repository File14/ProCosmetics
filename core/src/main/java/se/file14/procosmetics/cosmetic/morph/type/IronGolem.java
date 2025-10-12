package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;

public class IronGolem implements MorphBehavior {

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            Player player = context.getPlayer();
            Location location = player.getLocation();

            Location attackLocation = location.clone().add(location.getDirection().multiply(2.0d));
            attackLocation.setY(location.getY());

            nmsEntity.sendEntityEventPacket((byte) 4);

            // Find and push nearby players
            for (Player pushPlayer : MathUtil.getClosestPlayersFromLocation(attackLocation, 3.0d)) {
                if (pushPlayer != player) {
                    MathUtil.pushEntity(pushPlayer, location, 0.3d, 0.9d);
                    pushPlayer.getWorld().spawnParticle(Particle.CLOUD,
                            pushPlayer.getLocation().add(0.0d, 0.3d, 0.0d),
                            5,
                            0.1f,
                            0.1f,
                            0.1f,
                            0.0d
                    );
                    ProCosmeticsPlugin plugin = ProCosmeticsPlugin.getPlugin();
                    User otherUser = plugin.getUserManager().getConnected(pushPlayer);

                    if (otherUser != null) {
                        otherUser.setFallDamageProtection(4);
                    }
                }
            }
            player.getWorld().playSound(location, Sound.ENTITY_IRON_GOLEM_HURT, 0.6f, 1.2f);

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
}