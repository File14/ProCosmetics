package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class MagmaCube implements MorphBehavior {

    private static final Vector UP_FORCE = new Vector(0.0d, 0.6d, 0.0d);

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (nmsEntity.getBukkitEntity() instanceof Slime slime) {
            slime.setSize(2);
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (player.isSneaking() && player.isOnGround()) {
            Location location = player.getLocation();
            Vector vector = location.getDirection().multiply(0.8d).add(UP_FORCE);

            if (vector.getY() < 0.5d) {
                vector.setY(0.5d);
            }
            player.setVelocity(vector);
            player.getWorld().playSound(player, Sound.ENTITY_MAGMA_CUBE_JUMP, 0.8f, 1.0f);
            player.getWorld().spawnParticle(Particle.LAVA, location, 12, 1.0d, 0.8d, 1.0d, 0.0d);

            context.getUser().setFallDamageProtection(5);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}
