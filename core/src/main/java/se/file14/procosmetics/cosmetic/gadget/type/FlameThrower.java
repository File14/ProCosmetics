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
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;

public class FlameThrower implements GadgetBehavior {

    private Location location;
    boolean started;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        location = context.getPlayer().getEyeLocation();
        started = true;

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationTicks()
        );
        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (!started) {
            return;
        }
        location = context.getPlayer().getLocation(location);

        Vector playerDirection = location.getDirection();
        Vector rotatedVector = new Vector(-playerDirection.getZ(), 0, playerDirection.getX());
        Location particleLocation = location.clone().add(rotatedVector.multiply(0.5d)).add(0.0d, 0.95d, 0.0d);

        Vector randomOffset = new Vector(
                Math.random() - Math.random(),
                Math.random() - Math.random(),
                Math.random() - Math.random()
        );
        Vector particlePath = playerDirection.clone().add(randomOffset);
        particlePath.multiply(8.0d);

        Location offsetLocation = particlePath.toLocation(location.getWorld());

        location.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0,
                offsetLocation.getX(), offsetLocation.getY(), offsetLocation.getZ(),
                0.1d
        );
        location.getWorld().playSound(location, Sound.BLOCK_FIRE_AMBIENT, 1.0f, 1.0f);
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        started = false;
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
        return false;
    }
}
