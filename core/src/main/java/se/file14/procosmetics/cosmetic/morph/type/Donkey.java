package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Donkey implements MorphBehavior {

    private boolean jumped = false;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        context.getPlayer().setWalkSpeed(0.4f);
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (nmsEntity.getBukkitEntity() instanceof org.bukkit.entity.Donkey) {
            ((org.bukkit.entity.Donkey) nmsEntity.getBukkitEntity()).setCarryingChest(true);
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            Player player = context.getPlayer();
            player.getWorld().playSound(player, Sound.ENTITY_DONKEY_ANGRY, 0.8f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (!player.isOnGround() && !player.isFlying() && !jumped) {
            // Player is in the air, set donkey standing
            nmsEntity.setHorseStanding(true);
            jumped = true;
            nmsEntity.sendEntityMetadataPacket();
        } else if ((player.isOnGround() || player.isFlying()) && jumped) {
            // Player is on the ground, unset donkey standing
            nmsEntity.setHorseStanding(false);
            jumped = false;
            nmsEntity.sendEntityMetadataPacket();
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        context.getPlayer().setWalkSpeed(0.2f);
    }
}