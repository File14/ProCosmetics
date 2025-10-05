package se.file14.procosmetics.cosmetic.emote;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.emote.Emote;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.AnimationFrame;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.cosmetic.emote.animation.AnimationController;
import se.file14.procosmetics.cosmetic.emote.animation.AnimationListener;
import se.file14.procosmetics.nms.AbstractNMSEquipment;

import java.util.List;

public class EmoteImpl extends CosmeticImpl<EmoteType, EmoteBehavior> implements Emote, AnimationListener {

    private AbstractNMSEquipment<?> nmsEquipment;
    private AnimationController animationController;

    public EmoteImpl(ProCosmeticsPlugin plugin, User user, EmoteType type, EmoteBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
        user.removeCosmetic(plugin.getCategoryRegistries().banners(), false, true);
        user.removeCosmetic(plugin.getCategoryRegistries().morphs(), false, true);

        nmsEquipment = plugin.getNMSManager().createEquipment(player, true);
        player.playSound(player, Sound.ENTITY_HORSE_ARMOR, 0.5f, 1.0f);

        if (cosmeticType.hasAnimation()) {
            startAnimation();
        } else {
            nmsEquipment.setItemStack(cosmeticType.getItemStack());
            nmsEquipment.sendUpdateToViewers();
        }
    }

    private void startAnimation() {
        List<AnimationFrame> frames = cosmeticType.getFrames();

        animationController = new AnimationController(frames, cosmeticType.getTickInterval());
        animationController.addListener(this);
        animationController.setLooping(true);

        // Start the animation
        animationController.start();
        runTaskTimer(plugin, 0L, cosmeticType.getTickInterval());
    }

    @Override
    protected void onUpdate() {
        if (nmsEquipment == null || !isEquipped()) {
            cancel();
            return;
        }

        if (animationController != null) {
            animationController.tick();
        }
    }

    @Override
    public void onFrameChanged(int frameIndex, AnimationFrame frame) {
        // Update the display when frame changes
        if (nmsEquipment != null && frame != null) {
            nmsEquipment.setItemStack(frame.getItemStack());
            nmsEquipment.sendUpdateToViewers();
        }
        behavior.onUpdate(this, frameIndex);
    }

    @Override
    protected void onUnequip() {
        // Stop the animation
        if (animationController != null) {
            animationController.stop();
            animationController.removeListener(this);
            animationController = null;
        }

        if (nmsEquipment != null) {
            // We must close the inventory otherwise the item stays in helmet slot and 1 tick later to avoid errors
            player.closeInventory();
            nmsEquipment.cancel();
            nmsEquipment.setItemStack(player.getInventory().getHelmet());
            nmsEquipment.sendUpdateToViewers();
        }
        nmsEquipment = null;
    }

    @EventHandler
    public void onHelmetSlotClick(InventoryClickEvent event) {
        if (player == event.getWhoClicked() && event.getRawSlot() == 5
                && player.getGameMode() != GameMode.CREATIVE && nmsEquipment != null) {
            player.updateInventory();
        }
    }
}