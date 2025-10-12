package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

public class Pig implements MorphBehavior {

    private static final double Y_KNOCKBACK = 0.3d;
    private int tick;

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
            player.getWorld().playSound(player, Sound.ENTITY_PIG_AMBIENT, 0.8f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        // Don't allow the player to knockback other players while riding an entity
        if (player.getVehicle() != null) {
            return;
        }

        if (tick % 20 == 0) { // Some cooldown
            MathUtil.findClosestVisiblePlayersFromLocationForPlayer(player, player.getLocation(), 1.5d, hitPlayer -> {
                MathUtil.pushEntity(hitPlayer, player.getLocation(), 0.1d, Y_KNOCKBACK);
                MathUtil.pushEntity(player, hitPlayer.getLocation(), 0.5d, Y_KNOCKBACK);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIG_AMBIENT, 0.6f, 1.0f);
            });
        }

        if (++tick >= 360) {
            tick = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}
