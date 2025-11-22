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
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BatBlaster implements GadgetBehavior {

    private static final int BAT_AMOUNT = 12;
    private static final double VERTICAL_SPREAD = 0.1d;
    private static final double HORIZONTAL_SPREAD = 0.3d;

    private final List<Bat> bats = new ArrayList<>();
    private Vector direction;
    private Location location;
    private final Vector velocity = new Vector();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        location = context.getPlayer().getEyeLocation();
        direction = location.getDirection().multiply(0.5d);

        for (int i = 0; i < BAT_AMOUNT; i++) {
            bats.add(location.getWorld().spawn(location, Bat.class, MetadataUtil::setCustomEntity));
        }
        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationTicks()
        );
        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (bats.isEmpty()) {
            return;
        }
        Iterator<Bat> iterator = bats.iterator();

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            velocity.setX(direction.getX() + MathUtil.randomRange(-HORIZONTAL_SPREAD, HORIZONTAL_SPREAD));
            velocity.setY(direction.getY() + MathUtil.randomRange(-VERTICAL_SPREAD, VERTICAL_SPREAD));
            velocity.setZ(direction.getZ() + MathUtil.randomRange(-HORIZONTAL_SPREAD, HORIZONTAL_SPREAD));
            entity.setVelocity(velocity);

            Player player = context.getPlayer();
            entity.getLocation(location);

            for (Player hitPlayer : MathUtil.getClosestPlayersFromLocation(location, 1.5d)) {
                if (hitPlayer != player && hitPlayer.isValid()) {
                    MathUtil.pushEntity(hitPlayer, location, 0.3d, 0.2d);
                    location.getWorld().spawnParticle(Particle.LARGE_SMOKE, location, 0);
                    entity.getWorld().playSound(location, Sound.ENTITY_BAT_HURT, 0.3f, 1.0f);
                    entity.remove();
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        for (Entity entity : bats) {
            location.getWorld().spawnParticle(Particle.LARGE_SMOKE, entity.getLocation(location), 0);
            entity.remove();
        }
        bats.clear();
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
}
