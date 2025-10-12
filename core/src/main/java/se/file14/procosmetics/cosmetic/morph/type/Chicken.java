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

public class Chicken implements MorphBehavior {

    private static final ItemStack EGG_ITEM = new ItemStack(Material.EGG);

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
            player.getWorld().playSound(player, Sound.ENTITY_CHICKEN_AMBIENT, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        if (event.isSneaking()) {
            Player player = context.getPlayer();
            Location location = player.getLocation();

            NMSEntity eggEntity = context.getPlugin().getNMSManager().createEntity(player.getWorld(), EntityType.ITEM);
            eggEntity.setEntityItemStack(EGG_ITEM);
            eggEntity.setPositionRotation(location.add(0.0d, 0.1d, 0.0d));
            eggEntity.getTracker().startTracking();
            eggEntity.getTracker().destroyAfter(80);

            player.getWorld().playSound(player, Sound.ENTITY_CHICKEN_EGG, 0.8f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}