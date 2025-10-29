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

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.material.Materials;

import javax.annotation.Nullable;

public class PartyPopper implements GadgetBehavior {

    private static final int ITEM_DESPAWN = 20 * 3;

    private boolean started;
    private int i;
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        location = context.getPlayer().getLocation();
        started = true;

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), () -> {
                    started = false;
                    i = 0;
                },
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (started && i < 90) {
            Player player = context.getPlayer();
            player.getWorld().playSound(player.getLocation(location), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);

            location.add(0.0d, 1.8d, 0.0d);
            location.getWorld().spawnParticle(Particle.FIREWORK, location, 0,
                    MathUtil.randomRange(-1.0d, 1.0d),
                    MathUtil.randomRange(0.5d, 1.0d),
                    MathUtil.randomRange(-1.0d, 1.0d), 0.5d
            );
            dropDespawningItem(context.getPlugin(), location, Materials.getRandomWoolItem());
            i++;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        i = 0;
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

    protected void dropDespawningItem(ProCosmetics plugin, Location location, ItemStack itemStack) {
        NMSEntity nmsEntity = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
        nmsEntity.setEntityItemStack(itemStack);
        nmsEntity.setVelocity(
                MathUtil.randomRange(-0.3d, 0.3d),
                MathUtil.randomRange(0.5d, 1.0d),
                MathUtil.randomRange(-0.3d, 0.3d)
        );
        nmsEntity.setPositionRotation(location);
        nmsEntity.getTracker().startTracking();
        nmsEntity.getTracker().destroyAfter(ITEM_DESPAWN);
    }
}
