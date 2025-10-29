/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.cosmetic.banner;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.banner.Banner;
import se.file14.procosmetics.api.cosmetic.banner.BannerBehavior;
import se.file14.procosmetics.api.cosmetic.banner.BannerType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.AnimationFrame;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.cosmetic.emote.animation.AnimationController;
import se.file14.procosmetics.cosmetic.emote.animation.AnimationListener;
import se.file14.procosmetics.nms.AbstractNMSEquipment;

import java.util.List;

public class BannerImpl extends CosmeticImpl<BannerType, BannerBehavior> implements Banner, AnimationListener {

    private AbstractNMSEquipment<?> nmsEquipment;
    private AnimationController animationController;

    public BannerImpl(ProCosmeticsPlugin plugin, User user, BannerType type, BannerBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
        user.removeCosmetic(plugin.getCategoryRegistries().emotes(), false, true);
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
        runTaskTimerAsynchronously(plugin, 0L, cosmeticType.getTickInterval());
    }

    @Override
    protected void onUpdate() {
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
        //behavior.onUpdate(this, frameIndex);
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
            // We must close the inventory otherwise the item stays in helmet slot
            player.closeInventory();
            nmsEquipment.cancel();
            nmsEquipment.setItemStack(player.getInventory().getHelmet());
            nmsEquipment.sendUpdateToViewers();
        }
        nmsEquipment = null;
    }

    private void sendDelayedUpdate() {
        if (player.getGameMode() != GameMode.CREATIVE) {
            player.updateInventory();
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (isEquipped() && nmsEquipment != null) {
                nmsEquipment.sendUpdateToPlayer(player);
            }
        }, 3L);
    }

    /**
     * When the player clicks the helmet slot (raw slot 5) in their inventory,
     * we re-send the banner to the client.
     */
    @EventHandler
    public void omHelmetSlotClick(InventoryClickEvent event) {
        if (event.getWhoClicked() == player && event.getRawSlot() == 5 && nmsEquipment != null) {
            sendDelayedUpdate();
        }
    }

    /**
     * If the player right-clicks while holding a helmet, we update the banner again.
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getPlayer() == player && nmsEquipment != null
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            ItemStack itemStack = event.getItem();

            if (itemStack != null && itemStack.getType().toString().contains("HELMET")) {
                sendDelayedUpdate();
            }
        }
    }
}
