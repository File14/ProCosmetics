package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Bunny implements MorphBehavior {

    private static final Vector UP_FORCE = new Vector(0.0d, 0.4d, 0.0d);

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
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (player.isSneaking()) {
            if (player.isOnGround()) {
                Location location = player.getLocation();

                nmsEntity.sendEntityEventPacket((byte) 1);

                // Set pitch to 0 for horizontal jump
                location.setPitch(0.0f);
                player.setVelocity(location.getDirection().multiply(0.8d).add(UP_FORCE));

                player.getWorld().playSound(player, Sound.ENTITY_RABBIT_JUMP, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.FIREWORK, location, 5, 0.3d, 0.2d, 0.3d, 0.0d);
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}
