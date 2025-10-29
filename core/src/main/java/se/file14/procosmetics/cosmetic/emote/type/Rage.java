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
package se.file14.procosmetics.cosmetic.emote.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;

public class Rage implements EmoteBehavior {

    @Override
    public void onEquip(CosmeticContext<EmoteType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<EmoteType> context, int frame) {
        if (frame > 8 && frame < 20) {
            Location location = context.getPlayer().getEyeLocation().add(0.0d, 0.1d, 0.0d);
            location.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, location, 1);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<EmoteType> context) {
    }
}
