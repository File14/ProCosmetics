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
package se.file14.procosmetics.cosmetic.gadget;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.cosmetic.gadget.type.GrapplingHook;
import se.file14.procosmetics.event.PlayerPreUseGadgetEventImpl;
import se.file14.procosmetics.menu.menus.purchase.AmmoPurchaseMenu;
import se.file14.procosmetics.util.item.ItemBuilderImpl;
import se.file14.procosmetics.util.item.ItemIdentifier;

import javax.annotation.Nullable;

public class GadgetImpl extends CosmeticImpl<GadgetType, GadgetBehavior> implements Gadget {

    public static final ItemIdentifier GADGET_ID = new ItemIdentifier("GADGET_ITEM");

    private static final int BAR_LENGTH = 50;
    private static final String GREEN_BAR = ChatColor.GREEN + "┃";
    private static final String RED_BAR = ChatColor.RED + "┃";

    protected static final int GADGET_SLOT = ProCosmeticsPlugin.getPlugin().getCategoryRegistries().gadgets().getConfig().getInt("slot");

    public GadgetImpl(ProCosmeticsPlugin plugin, User user, GadgetType type, GadgetBehavior behaviour) {
        super(plugin, user, type, behaviour);
    }

    @Override
    public boolean canEquip() {
        if (user.hasCosmetic(cosmeticType.getCategory())) {
            return true;
        }
        ItemStack itemStack = player.getInventory().getItem(GADGET_SLOT);

        if (itemStack == null) {
            return true;
        }
        if (!GADGET_ID.is(itemStack)) {
            user.sendMessage(user.translate(
                    "cosmetic.gadgets.equip.remove_item",
                    Placeholder.unparsed("slot", String.valueOf(GADGET_SLOT + 1))
            ));
            return false;
        }
        return true;
    }

    @Override
    public void onEquip() {
        setGadgetItemInInventory();
        runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void onUpdate() {
        if (player.getInventory().getHeldItemSlot() == GADGET_SLOT && user.hasCooldown(cosmeticType)) {
            long current = System.currentTimeMillis();
            double cooldown = (user.getCooldown(cosmeticType) - current) / 1000L;
            double progress = cooldown / cosmeticType.getCooldown();
            int greenBars = Math.max(0, BAR_LENGTH - (int) (progress * BAR_LENGTH));
            String text = GREEN_BAR.repeat(Math.max(0, greenBars)) + RED_BAR.repeat(Math.max(0, BAR_LENGTH - greenBars));

            user.sendActionBar(user.translate(
                    "cosmetic.gadgets.cooldown",
                    Placeholder.unparsed("gadget", cosmeticType.getName(user)),
                    Placeholder.unparsed("cooldown_bar", text),
                    Placeholder.unparsed("cooldown_duration", String.valueOf(Math.round(cooldown * 10) / 10.0d))
            ));
        }
        behavior.onUpdate(this);
    }

    @Override
    public void onUnequip() {
        player.getInventory().setItem(GADGET_SLOT, null);
    }

    private boolean canUse() {
        if (user.hasCooldown(cosmeticType)) {
            return false;
        }

        if (!cosmeticType.hasInfinityAmmo() && user.getAmmo(cosmeticType) <= 0) {
            if (cosmeticType.hasPurchasableAmmo()) {
                new AmmoPurchaseMenu(plugin, user, cosmeticType).open();
            }
            return false;
        }
        Location location = player.getLocation();

        if (behavior.requiresGroundOnUse()) {
            location.subtract(0.0d, 1.0d, 0.0d);

            if (location.getBlock().getType().isAir()) {
                user.sendMessage(user.translate("cosmetic.equip.deny.ground"));
                return false;
            }
            location.add(0.0d, 1.0d, 0.0d);
        }

        if (!behavior.isEnoughSpaceToUse(location)) {
            user.sendMessage(user.translate("cosmetic.equip.deny.space"));
            return false;
        }
        PlayerPreUseGadgetEventImpl event = new PlayerPreUseGadgetEventImpl(plugin, user, player, this);
        plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }
        return true;
    }

    private void consume(boolean consumeAmmo, boolean applyCooldown) {
        if (consumeAmmo && !cosmeticType.hasInfinityAmmo()) {
            user.setAmmo(cosmeticType, user.getAmmo(cosmeticType) - 1);
            // TODO: Look into a better solution
            plugin.getDatabase().removeGadgetAmmoAsync(user, cosmeticType, 1);
        }
        if (applyCooldown && cosmeticType.getCooldown() > 0.0d) {
            user.setCooldown(cosmeticType, cosmeticType.getCooldown());

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline() && isEquipped()) {
                    user.sendActionBar(user.translate(
                            "cosmetic.gadgets.cooldown.ready",
                            Placeholder.unparsed("gadget", cosmeticType.getName(user))
                    ));
                }
            }, (long) (20L * cosmeticType.getCooldown() + 1L));
        }

        // We cannot set cooldown for GrapplingHook (fishing rod) because it won't throw the hook then (ugly workaround right now)
        if (!(behavior instanceof GrapplingHook)) {
            // Apply item cooldown if either ammo consumed or cooldown applied
            if ((consumeAmmo || applyCooldown) && cosmeticType.getCooldown() > 0.0d) {
                player.setCooldown(cosmeticType.getItemStack().getType(), (int) (20 * cosmeticType.getCooldown()));
            }
        }
    }

    private GadgetBehavior.InteractionResult onUse(@Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        GadgetBehavior.InteractionResult result = behavior.onInteract(this, clickedBlock, clickedPosition);

        if (result.shouldConsumeAmmo() || result.shouldApplyCooldown()) {
            consume(result.shouldConsumeAmmo(), result.shouldApplyCooldown());
            setGadgetItemInInventory();
        }
        return result;
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || event.getPlayer() != player) {
            return;
        }
        if (GADGET_ID.is(player.getInventory().getItemInMainHand())) {
            if (!canUse()) {
                event.setCancelled(true);
                return;
            }
            GadgetBehavior.InteractionResult result = onUse(event.getClickedBlock(), event.getClickedPosition());

            if (result.shouldCancelEvent()) {
                event.setCancelled(true);
            }
        }
    }

    public void setGadgetItemInInventory() {
        ItemBuilder itemBuilder = getGadgetItem();
        player.getInventory().setItem(GADGET_SLOT, itemBuilder.getItemStack());
    }

    public ItemBuilder getGadgetItem() {
        return GADGET_ID.apply(new ItemBuilderImpl(cosmeticType.getItemStack())
                .setDisplayName(user.translate(
                        "item.gadget.name",
                        Placeholder.unparsed("name", cosmeticType.getName(user)),
                        Placeholder.unparsed("ammo", (!cosmeticType.hasInfinityAmmo() ? String.valueOf(user.getAmmo(cosmeticType)) : ""))))
                .setLoreComponent(user.translateList("item.gadget.desc")));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(GADGET_ID::is);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player) {
            return;
        }

        if (event.getAction() == InventoryAction.HOTBAR_SWAP && event.getHotbarButton() == GADGET_SLOT) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
            return;
        }

        if (GADGET_ID.is(event.getCurrentItem()) || GADGET_ID.is(event.getCursor())) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer() == player && GADGET_ID.is(player.getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getPlayer() == player && GADGET_ID.is(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwapItem(PlayerSwapHandItemsEvent event) {
        if (event.getPlayer() == player && GADGET_ID.is(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }
}
