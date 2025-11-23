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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.event.PlayerPreUseGadgetEvent;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.cosmetic.gadget.type.GrapplingHook;
import se.file14.procosmetics.menu.menus.purchase.AmmoPurchaseMenu;
import se.file14.procosmetics.util.item.ItemBuilderImpl;
import se.file14.procosmetics.util.item.ItemIdentifier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class GadgetImpl extends CosmeticImpl<GadgetType, GadgetBehavior> implements Gadget {

    public static final ItemIdentifier GADGET_ID = new ItemIdentifier("GADGET_ITEM");

    private static final int BAR_LENGTH = 50;
    private static final Component[] COOLDOWN_BAR_CACHE = new Component[BAR_LENGTH + 1];
    private static final Component GREEN_BAR_COMPONENT = Component.text("┃", NamedTextColor.GREEN);
    private static final Component RED_BAR_COMPONENT = Component.text("┃", NamedTextColor.RED);
    private DecimalFormat decimalFormat;

    static {
        // Pre-compute all possible bar combinations
        for (int i = 0; i <= BAR_LENGTH; i++) {
            TextComponent.Builder builder = Component.text();

            // Add green bars
            for (int j = 0; j < i; j++) {
                builder.append(GREEN_BAR_COMPONENT);
            }
            // Add red bars
            for (int j = i; j < BAR_LENGTH; j++) {
                builder.append(RED_BAR_COMPONENT);
            }
            COOLDOWN_BAR_CACHE[i] = builder.build();
        }
    }

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

        // We cache this while the gadget is equipped
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(user.translateRaw("generic.decimal_separator").charAt(0));
        decimalFormat = new DecimalFormat("0.0", symbols);
    }

    @Override
    public void onUpdate() {
        if (player.getInventory().getHeldItemSlot() == GADGET_SLOT && user.hasCooldown(cosmeticType)) {
            long currentTime = System.currentTimeMillis();
            double cooldownEndTime = user.getCooldown(cosmeticType);
            double remainingSeconds = (cooldownEndTime - currentTime) / 1000.0;
            double totalCooldown = cosmeticType.getCooldown();
            double progress = Math.min(1.0, remainingSeconds / totalCooldown);

            // Calculate bar position (0 = all green, BAR_LENGTH = all red)
            int redBars = Math.min(BAR_LENGTH, Math.max(0, (int) (progress * BAR_LENGTH)));
            int greenBars = BAR_LENGTH - redBars;
            Component barComponent = COOLDOWN_BAR_CACHE[greenBars];
            String formattedCooldown = decimalFormat.format(remainingSeconds);

            user.sendActionBar(user.translate(
                    "cosmetic.gadgets.cooldown",
                    Placeholder.unparsed("gadget", cosmeticType.getName(user)),
                    Placeholder.component("cooldown_bar", barComponent),
                    Placeholder.unparsed("cooldown_duration", formattedCooldown)
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
        PlayerPreUseGadgetEvent event = new PlayerPreUseGadgetEvent(plugin, user, player, this);
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
            }, cosmeticType.getCooldownTicks() + 1L);

            // We cannot set cooldown for GrapplingHook (fishing rod) because it won't throw the hook then (ugly workaround right now)
            if (!(behavior instanceof GrapplingHook)) {
                player.setCooldown(cosmeticType.getItemStack().getType(), (int) cosmeticType.getCooldownTicks());
            }
        }
    }

    private GadgetBehavior.InteractionResult onUse(Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        GadgetBehavior.InteractionResult result = behavior.onInteract(this, action, clickedBlock, clickedPosition);

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
            GadgetBehavior.InteractionResult result = onUse(event.getAction(), event.getClickedBlock(), event.getClickedPosition());

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
                .setLore(user.translateList("item.gadget.desc")));
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
