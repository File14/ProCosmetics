package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

import java.util.UUID;

public class CaveSpider implements MorphBehavior, Listener {

    private static final PotionEffect CONFUSION_EFFECT = new PotionEffect(PotionEffectType.NAUSEA, 180, 0);
    private static final PotionEffect BLINDNESS_EFFECT = new PotionEffect(PotionEffectType.BLINDNESS, 60, 0);

    private static final ItemStack REDSTONE_ITEM = new ItemStack(Material.REDSTONE);
    private static final ItemStack SPIDER_EYE_ITEM = new ItemStack(Material.SPIDER_EYE);

    private UUID lastClicked;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Player clickedPlayer = null;

        if (lastClicked != null) {
            clickedPlayer = context.getPlugin().getJavaPlugin().getServer().getPlayer(lastClicked);
        }

        if (clickedPlayer == null) {
            return InteractionResult.NO_ACTION;
        }
        Player player = context.getPlayer();

        clickedPlayer.addPotionEffect(CONFUSION_EFFECT);
        clickedPlayer.addPotionEffect(BLINDNESS_EFFECT);
        clickedPlayer.playHurtAnimation(0.0f);
        clickedPlayer.getWorld().playSound(clickedPlayer, Sound.ENTITY_PLAYER_HURT, 0.5f, 1.0f);
        player.getWorld().playSound(player, Sound.ENTITY_SPIDER_HURT, 1.0f, 1.0f);

        Location location = clickedPlayer.getLocation();

        location.getWorld().spawnParticle(Particle.ITEM,
                location.add(0.0d, 1.0d, 0.0d),
                30,
                0.3d,
                0.3d,
                0.3d,
                0.0d,
                REDSTONE_ITEM
        );
        location.getWorld().spawnParticle(Particle.ITEM,
                location.add(0.0d, 0.8d, 0.0d),
                20,
                0.3d,
                0.0d,
                0.3d,
                0.1d,
                SPIDER_EYE_ITEM
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }

    @EventHandler
    public void onClickedPlayer(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player player) {
            lastClicked = player.getUniqueId();
        }
    }
}