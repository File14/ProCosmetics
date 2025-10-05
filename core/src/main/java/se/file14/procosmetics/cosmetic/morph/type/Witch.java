package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

public class Witch implements MorphBehavior, Listener {

    private static final double POTION_RANGE = 3.5d;

    private ThrownPotion potion;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR && potion == null) {
            potion = player.launchProjectile(ThrownPotion.class);
            potion.setVelocity(player.getLocation().getDirection().multiply(1.1d));

            player.getWorld().playSound(player, Sound.ENTITY_WITCH_THROW, 1.0f, 1.0f);
            player.getWorld().playSound(player, Sound.ENTITY_WITCH_AMBIENT, 1.0f, 1.0f);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        if (potion == null) {
            return;
        }

        potion.remove();
        potion = null;
    }

    @Override
    public boolean hasAttackAnimation() {
        return false;
    }

    @Override
    public boolean hasItemHoldAnimation() {
        return true;
    }

    @EventHandler
    public void onPotionHit(ProjectileHitEvent event) {
        if (event.getEntity() == potion) {
            Location location = event.getEntity().getLocation();

            for (Player hitPlayer : MathUtil.getClosestPlayersFromLocation(location, POTION_RANGE)) {
                hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1));
            }
            potion = null;
        }
    }
}