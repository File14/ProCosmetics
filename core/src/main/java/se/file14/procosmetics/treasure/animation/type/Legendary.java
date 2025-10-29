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
import org.bukkit.Sound;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.treasure.animation.TreasureChestAnimation;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class Legendary extends TreasureChestAnimation {

    private static final double SPEED = 15.0d;
    private static final float RADIUS_INCREASE_PER_TICK = 0.03f;
    private static final double GOAL_RADIUS = 3.5d;

    private final double anglePerChest = 360.0d / platform.getChestLocations().size();
    private float radius;

    public Legendary(ProCosmetics plugin, TreasureChestPlatform platform, TreasureChest treasure, User user) {
        super(plugin, platform, treasure, user);
    }

    @Override
    public void onSecondUpdate() {
        if (animationState == AnimationState.SPAWNING_CHESTS) {
            for (int i = 0; i < platform.getChestLocations().size(); i++) {
                spawnChestArmorstand(Material.ENDER_CHEST, platform.getCenter());
            }
            animationState = AnimationState.BUILT;
        }
    }

    @Override
    public void onTickUpdate() {
        if (animationState == AnimationState.BUILT) {
            Location location = platform.getCenter();
            radius += RADIUS_INCREASE_PER_TICK;

            location.getWorld().playSound(location, Sound.ENTITY_HORSE_LAND, 0.2f, (radius / 2));

            for (int i = 0; i < armorStandChests.size(); i++) {
                NMSEntity nmsEntity = armorStandChests.get(i);
                float angle = FastMathUtil.toRadians(i * anglePerChest + ticks * SPEED);
                Location loc = MathUtil.getLocationAroundCircle(platform.getCenter(), radius, angle);

                nmsEntity.sendPositionRotationPacket(loc);
                loc.getWorld().spawnParticle(Particle.WITCH, loc.add(0.0d, 0.7d, 0.0d), 0);
            }

            if (radius > GOAL_RADIUS) {
                for (Location chestLocation : platform.getChestLocations()) {
                    setChestBlock(Material.ENDER_CHEST, chestLocation);
                }
                despawnChestArmorstands();
                animationState = AnimationState.DONE;
            }
        }
    }
}
