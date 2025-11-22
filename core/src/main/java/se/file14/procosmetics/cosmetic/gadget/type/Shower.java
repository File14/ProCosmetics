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
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.type.BlockDisplayStructure;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class Shower implements GadgetBehavior {

    private static final double DRIP_HEIGHT = 3.15d;

    private BlockDisplayStructure structure;
    private Location center;
    private Location shower;
    private Location eyeLocation;
    private final Set<Location> waterDripLocations = new HashSet<>(4);

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockDisplayStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        center = context.getPlayer().getLocation();

        double angle = structure.spawn(center);
        shower = center.clone().add(MathUtil.rotateAroundAxisY(new Vector(0.0d, 0.0d, -0.9d), angle));
        eyeLocation = shower.clone();

        double offset = 0.23d;

        // Calculate drip locations
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 2; k++) {
                waterDripLocations.add(shower.clone().add(2.0d * i * offset - offset, DRIP_HEIGHT, 2.0d * k * offset - offset));
            }
        }
        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (shower == null) {
            return;
        }
        for (Player onlinePlayer : shower.getWorld().getPlayers()) {
            onlinePlayer.getLocation(eyeLocation).add(0.0d, 1.8d, 0.0d);

            if (Math.abs(eyeLocation.getX() - shower.getX()) < 0.4d
                    && Math.abs(eyeLocation.getZ() - shower.getZ()) < 0.4d
                    && Math.abs(eyeLocation.getY() - shower.getY()) < DRIP_HEIGHT) {
                center.getWorld().spawnParticle(Particle.SPLASH, eyeLocation, 5, 0.0d, 0.3d, 0.1d, 0.3d);
            }
        }
        shower.getWorld().playSound(shower, Sound.WEATHER_RAIN, 0.05f, 1.8f);

        for (Location dripLocation : waterDripLocations) {
            center.getWorld().spawnParticle(Particle.DRIPPING_WATER, dripLocation, 1);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        center = null;
        shower = null;
        waterDripLocations.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return true;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return structure.isEnoughSpace(location);
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}
