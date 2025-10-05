package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Blaze implements MorphBehavior {

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private int tick;
    private boolean flyable;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        flyable = context.getType().getCategory().getConfig().getBoolean("morphs.settings.flyable");
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (player.isSneaking() && flyable) {
            player.getLocation(location);
            if (tick % 15 == 0) {
                player.getWorld().playSound(player, Sound.ENTITY_BLAZE_AMBIENT, 0.1f, 1.0f);
            }
            player.setVelocity(location.getDirection().multiply(0.6d));

            location.add(0.0d, 0.5d, 0.0d);
            location.getWorld().spawnParticle(Particle.FLAME, location, 0);
            location.getWorld().spawnParticle(Particle.LAVA, location, 0);
            location.getWorld().spawnParticle(Particle.LARGE_SMOKE, location, 0);

            if (++tick >= 360) {
                tick = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        if (flyable) {
            context.getUser().setFallDamageProtection(12);
        }
    }
}
