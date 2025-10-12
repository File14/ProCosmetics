package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Bat implements MorphBehavior {

    private static final ItemStack COCO_BEANS_ITEM = new ItemStack(Material.COCOA_BEANS);

    private boolean flyable;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        flyable = context.getType().getCategory().getConfig().getBoolean("morphs.settings.flyable");
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (nmsEntity.getBukkitEntity() instanceof org.bukkit.entity.Bat bat) {
            bat.setAwake(true);
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();

        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && flyable) {
            Player player = context.getPlayer();
            Location location = player.getLocation();

            player.setVelocity(location.getDirection().multiply(0.4d).setY(0.6d));
            player.getWorld().playSound(location, Sound.ENTITY_BAT_TAKEOFF, 0.6f, 1.5f);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        Player player = context.getPlayer();
        Location location = player.getLocation();

        player.getWorld().playSound(location, Sound.ENTITY_BAT_AMBIENT, 0.6f, 1.0f);

        NMSEntity droppedItem = context.getPlugin().getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
        droppedItem.setEntityItemStack(COCO_BEANS_ITEM);
        droppedItem.setPositionRotation(location.add(0.0d, 0.1d, 0.0d));
        droppedItem.getTracker().startTracking();
        droppedItem.getTracker().destroyAfter(80);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}