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
package se.file14.procosmetics.cosmetic.gadget.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoinPartyBomb implements GadgetBehavior, Listener {

    private static final FireworkEffect FIREWORK_EFFECT = FireworkEffect.builder()
            .flicker(false)
            .withColor(Color.YELLOW)
            .with(Type.BURST).
            trail(false).build();

    private static final float Y_MOVEMENT_PER_TICK = FastMathUtil.PI / 90.0f;
    private static final float ROTATION_PER_TICK = FastMathUtil.PI / 45.0f;
    private static final float HEAD_ROTATION_PER_TICK = (float) Math.toDegrees(ROTATION_PER_TICK);
    private static final float Y_OFFSET = 10.0f;

    private static final ItemStack GOLD_ITEM = new ItemStack(Material.GOLD_BLOCK);
    private static final long EXTRA_TIME = 400L;

    private final List<Item> items = new ArrayList<>();
    private NMSEntity nmsEntity;
    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location center = new Location(null, 0.0d, 0.0d, 0.0d);
    private int tick;

    private ProCosmetics plugin;
    private int pickupReward;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (plugin == null) {
            plugin = context.getPlugin();
        }
        CosmeticType<?, ?> type = context.getType();
        pickupReward = type.getCategory().getConfig().getInt("cosmetics." + type.getKey() + ".pickup_reward");
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();

        if (context.getType().hasPurchasableAmmo()) {
            long totalValue = pickupReward * context.getType().getDurationTicks();

            if (totalValue < context.getType().getAmmoCost()) {
                context.getUser().sendMessage(Component.text("Incorrect setup detected! This configuration can lead to profit " +
                        "and infinite money exploits. Please contact a server administrator.", NamedTextColor.RED));
                return InteractionResult.fail();
            }
        }
        player.getLocation(location);
        player.getLocation(center);
        location.setPitch(45.0f); // tilt slightly down

        nmsEntity = context.getPlugin().getNMSManager().createEntity(player.getWorld(), EntityType.BLOCK_DISPLAY);

        if (nmsEntity.getBukkitEntity() instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(GOLD_ITEM.getType().createBlockData());
            Matrix4f transformationMatrix = new Matrix4f();
            transformationMatrix.identity()
                    //.scale(scale)
                    //.rotateY(radians)
                    .translate(-0.5f, 0.0f, -0.5f);
            blockDisplay.setTransformationMatrix(transformationMatrix);
            blockDisplay.setTeleportDuration(1);
        }
        nmsEntity.setPositionRotation(center);
        nmsEntity.getTracker().startTracking();

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), () -> {
            tick = 0;
            despawnBlock();
        }, context.getType().getDurationTicks());

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> {
                    // Make sure it's not running (the player could have started another one)
                    if (tick == 0) {
                        onUnequip(context);
                    }
                },
                context.getType().getDurationTicks() + EXTRA_TIME
        );
        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (nmsEntity == null) {
            return;
        }
        float y = Math.abs(FastMathUtil.sin(Y_MOVEMENT_PER_TICK * tick));
        location.setY(center.getY() + y * Y_OFFSET);
        location.setYaw(HEAD_ROTATION_PER_TICK * tick);

        nmsEntity.sendPositionRotationPacket(location);

        if (tick % 3 == 0) {
            Location location = nmsEntity.getPreviousLocation();

            items.add(location.getWorld().dropItem(location,
                    new ItemBuilderImpl(context.getType().getItemStack().getType()).setMaxSize(1).getItemStack(),
                    entity -> {
                        float angle = ROTATION_PER_TICK * tick;
                        float x = FastMathUtil.cos(angle);
                        float z = FastMathUtil.sin(angle);

                        entity.setVelocity(new Vector(x, MathUtil.randomRange(-3.0d, 0.1d), z));
                        entity.setPickupDelay(20);

                        MetadataUtil.setCustomEntity(entity);
                    }));

            location.getWorld().spawn(location, Firework.class, entity -> {
                FireworkMeta meta = entity.getFireworkMeta();
                meta.addEffect(FIREWORK_EFFECT);
                entity.setFireworkMeta(meta);
                MetadataUtil.setCustomEntity(entity);
            }).detonate();

            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
        }

        if (tick++ > 360) {
            tick = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        tick = 0;
        despawnBlock();
        despawnItems();
    }

    public void despawnBlock() {
        if (nmsEntity != null) {
            nmsEntity.getTracker().destroy();
            nmsEntity = null;
        }
    }

    private void despawnItems() {
        for (Item item : items) {
            location.getWorld().spawnParticle(Particle.LARGE_SMOKE, item.getLocation(location), 0);
            item.remove();
        }
        items.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }

    @EventHandler
    public void onCoinPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player pickupPlayer && items.remove(event.getItem())) {
            event.setCancelled(true);
            event.getItem().remove();

            Location pickupLocation = event.getItem().getLocation().add(0.0d, 0.4d, 0.0d);
            pickupPlayer.getWorld().playSound(pickupLocation, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 2.0f);
            location.getWorld().spawnParticle(Particle.FIREWORK, pickupLocation.add(0.0d, 0.4d, 0.0d), 0);

            User user = plugin.getUserManager().getConnected(pickupPlayer);

            if (user != null) {
                plugin.getEconomyManager().getEconomyProvider().addCoinsAsync(user, pickupReward);
            }
        }
    }
}
