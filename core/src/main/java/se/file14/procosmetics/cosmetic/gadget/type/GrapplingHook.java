package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;

import javax.annotation.Nullable;

public class GrapplingHook implements GadgetBehavior, Listener {

    private static final Vector MULTIPLIER_VECTOR = new Vector(2.5d, 1.5d, 2.5d);

    private Player player;
    private User user;
    private boolean thrownHook;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (player == null) {
            player = context.getPlayer();
            user = context.getUser();
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        if (!thrownHook) {
            return InteractionResult.SUCCESS_NO_EVENT_CANCEL;
        }
        return InteractionResult.FAILED_NO_EVENT_CANCEL;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return false;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getPlayer() != player
                // || player.getInventory().getHeldItemSlot() != GADGET_SLOT // TODO: Fix later :-)
                || event.getState() == PlayerFishEvent.State.BITE
                || event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            return;
        }

        if (event.getState() == PlayerFishEvent.State.FISHING) {
            thrownHook = true;
        } else {
            Entity hook = event.getHook();

            Vector vector = hook.getLocation().subtract(player.getLocation()).toVector().multiply(1.3d);
            vector.setY(vector.getY() + 0.5d);
            vector.normalize().multiply(MULTIPLIER_VECTOR);

            player.setVelocity(vector);
            user.setFallDamageProtection(8);

            thrownHook = false;

            hook.remove();
            event.setCancelled(true);
        }
    }
}