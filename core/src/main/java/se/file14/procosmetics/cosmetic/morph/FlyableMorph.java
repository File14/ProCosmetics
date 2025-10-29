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
package se.file14.procosmetics.cosmetic.morph;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public abstract class FlyableMorph implements MorphBehavior {

    private static final Vector UP_FORCE = new Vector(0.0d, 1.0d, 0.0d);

    public boolean flyable;
    private boolean defaultFlight;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        flyable = context.getType().getCategory().getConfig().getBoolean("morphs.settings.flyable");

        if (flyable) {
            Player player = context.getPlayer();

            if (player.getAllowFlight()) {
                defaultFlight = true;
            } else {
                player.setAllowFlight(true);
            }
        }
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (flyable) {
            Player player = context.getPlayer();

            if (player.isOnGround()) {
                player.setVelocity(UP_FORCE);
                player.setAllowFlight(true);
                player.setFlying(true);
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        if (flyable) {
            Player player = context.getPlayer();

            if (defaultFlight) {
                player.setAllowFlight(true);
            } else {
                context.getUser().setFallDamageProtection(10);
                player.setAllowFlight(false);
            }
        }
    }
}
