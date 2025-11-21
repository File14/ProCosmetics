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
package se.file14.procosmetics.treasure.animation.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.treasure.animation.TreasureChestAnimation;
import se.file14.procosmetics.util.MathUtil;

import java.util.Iterator;

public class Common extends TreasureChestAnimation {

    private int chests;

    public Common(ProCosmetics plugin, TreasureChestPlatform platform, TreasureChest treasure, User user) {
        super(plugin, platform, treasure, user);
    }

    @Override
    public void onSecondUpdate() {
        if (animationState == AnimationState.SPAWNING_CHESTS) {
            if (chests < platform.getChestLocations().size()) {
                Location location = platform.getChestLocations().get(chests++).clone().add(0.0d, 3.0d, 0.0d);
                location.setDirection(platform.getCenter().toVector().subtract(location.toVector()));
                MathUtil.snapToCardinalDirection(location);
                location.setPitch(0.0f);

                spawnChest(Material.CHEST, location);
            } else {
                animationState = AnimationState.BUILT;
            }
        }
    }

    @Override
    public void onTickUpdate() {
        if (animationState == AnimationState.SPAWNING_CHESTS || animationState == AnimationState.BUILT) {
            Iterator<NMSEntity> iterator = blockDisplayChests.iterator();

            while (iterator.hasNext()) {
                NMSEntity nmsEntity = iterator.next();
                nmsEntity.getPreviousLocation(location);

                location.getWorld().spawnParticle(Particle.LARGE_SMOKE, location.add(0.0d, 1.0d, 0.0d), 0);
                location.subtract(0.0d, 1.3d, 0.0d);

                nmsEntity.sendPositionRotationPacket(location);

                if (location.getY() < platform.getCenter().getY()) {
                    setChestBlock(Material.CHEST, location.add(0.0d, 1.0d, 0.0d));

                    nmsEntity.getTracker().destroy();
                    iterator.remove();
                }
            }
        }
    }
}
