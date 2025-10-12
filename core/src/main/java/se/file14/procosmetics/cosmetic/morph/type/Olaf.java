package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

public class Olaf implements MorphBehavior, Listener {

    private Snowball snowball;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();

        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && snowball == null) {
            Player player = context.getPlayer();
            snowball = player.launchProjectile(Snowball.class);
            player.getWorld().playSound(player, Sound.ENTITY_SNOWBALL_THROW, 0.8f, 1.2f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        if (snowball != null) {
            snowball.remove();
            snowball = null;
        }
    }

    @EventHandler
    public void onBallHit(ProjectileHitEvent event) {
        if (event.getEntity() == snowball) {
            if (event.getHitEntity() instanceof Player player) {
                Location location = snowball.getLocation();
                MathUtil.pushEntity(player, location, 0.8d, 0.6d);
                location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.SNOW_BLOCK);
            }
            snowball = null;
        }
    }
}