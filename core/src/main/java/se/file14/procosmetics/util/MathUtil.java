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
package se.file14.procosmetics.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class MathUtil {

    public static final ThreadLocalRandom THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();
    private static final BlockFace[] AXIS = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    public static int randomRangeInt(int minInclusive, int maxInclusive) {
        return THREAD_LOCAL_RANDOM.nextInt(minInclusive, maxInclusive + 1);
    }

    public static double randomRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    private static double offset(Location location, Location location2) {
        return offset(location.toVector(), location2.toVector());
    }

    private static double offset(Vector vector, Vector vector2) {
        return vector.subtract(vector2).length();
    }

    public static void pushEntity(Entity entity, Location location, double d) {
        if (!(entity instanceof Player) || entity.hasMetadata("NPC")) {
            return;
        }
        entity.setVelocity(getPushVector(entity, location, d));
    }

    public static void pushEntity(Entity entity, Location location, double d, double d2) {
        if (!(entity instanceof Player) || entity.hasMetadata("NPC")) {
            return;
        }
        entity.setVelocity(getPushVector(entity, location, d).setY(d2));
    }

    public static void pullEntity(Entity entity, Location location, double d, double d2) {
        if (!(entity instanceof Player) || entity.hasMetadata("NPC")) {
            return;
        }
        entity.setVelocity(getPullVector(entity, location, d).setY(d2));
    }

    public static Vector getPushVector(Entity entity, Location location, double d) {
        Vector vector = entity.getLocation().subtract(location).toVector();

        if (vector.length() == 0) {
            return new Vector(0.0d, 0.0d, 0.0d);
        }
        return vector.normalize().multiply(d);
    }

    public static Vector getPullVector(Entity entity, Location location, double d) {
        Vector vector = entity.getLocation().subtract(location).toVector().multiply(-1);

        if (vector.length() == 0) {
            return new Vector(0.0d, 0.0d, 0.0d);
        }
        return vector.normalize().multiply(d);
    }

    public static Vector rotateAroundAxisX(Vector vector, double angle) {
        double y = vector.getY();
        double z = vector.getZ();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return vector.setY(y * cos - z * sin).setZ(y * sin + z * cos);
    }

    public static Vector rotateAroundAxisY(Vector vector, double angle) {
        double x = vector.getX();
        double z = vector.getZ();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return vector.setX(x * cos + z * sin).setZ(x * -sin + z * cos);
    }

    public static Vector rotateAroundAxisZ(Vector vector, double angle) {
        double x = vector.getX();
        double y = vector.getY();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return vector.setX(x * cos - y * sin).setY(x * sin + y * cos);
    }

    public static BlockFace yawToFace(Location center, Location armorStand) {
        Location origin = center.clone();
        origin.setDirection(armorStand.toVector().subtract(origin.toVector()));

        return yawToFace(origin.getYaw());
    }

    public static BlockFace yawToFace(float yaw) {
        return AXIS[Math.round(yaw / 90f) & 0x3];
    }

    public static List<Block> getIn2DRadius(Location location, double range) {
        List<Block> blocks = new ArrayList<>();
        int maximumRange = (int) range + 1;

        for (int x = -maximumRange; x <= maximumRange; ++x) {
            for (int z = -maximumRange; z <= maximumRange; ++z) {
                Block block = location.getWorld().getBlockAt((int) (location.getX() + x), (int) location.getY(), (int) (location.getZ() + z));

                if (MathUtil.offset(location, block.getLocation().add(0.5d, 0.5d, 0.5d)) <= range) {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static List<Block> getIn3DRadius(Location location, int range) {
        List<Block> blocks = new ArrayList<>();
        int maximumRange = range + 1;
        Location reusableLocation = location.clone();

        for (int x = -maximumRange; x <= maximumRange; x++) {
            for (int y = -maximumRange; y <= maximumRange; y++) {
                for (int z = -maximumRange; z <= maximumRange; z++) {
                    Block block = location.getBlock().getRelative(x, y, z);

                    if (MathUtil.offset(location, block.getLocation(reusableLocation).add(0.5d, 0.5d, 0.5d)) <= range) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Block> getIn3DRadius(Location location, int range, int height) {
        List<Block> blocks = new ArrayList<>();
        int maximumRange = range + 1;
        int maximumHeight = height + 1;

        for (int x = -maximumRange; x <= maximumRange; x++) {
            for (int y = 0; y <= maximumHeight; y++) {
                for (int z = -maximumRange; z <= maximumRange; z++) {
                    Block block = location.getBlock().getRelative(x, y, z);
                    Location tempLoc = location.clone();
                    tempLoc.setY(block.getY());

                    if (MathUtil.offset(tempLoc, block.getLocation()) <= range) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Player> getClosestPlayersFromLocation(Location location, double range) {
        return getClosestPlayersFromLocationSquared(location, range * range);
    }

    public static List<Player> getClosestPlayersFromLocationSquared(Location location, double range) {
        List<Player> players = new ArrayList<>();
        Location reusableLocation = location.clone();

        for (Player player : location.getWorld().getPlayers()) {
            if (player.hasMetadata("NPC")) {
                continue;
            }

            if (player.getLocation(reusableLocation).add(0.0d, 1.0d, 0.0d).distanceSquared(location) <= range) {
                players.add(player);
            }
        }
        return players;
    }

    public static Player getClosestPlayerFromLocation(Location location, double range) {
        double d = range * range;

        for (Player player : location.getWorld().getPlayers()) {
            if (player.getEyeLocation().distanceSquared(location) <= d && !player.hasMetadata("NPC")) {
                return player;
            }
        }
        return null;
    }

    public static Player getClosestVisiblePlayerFromLocation(Player player, Location location, double range) {
        double d = range * range;

        for (Player onlinePlayer : location.getWorld().getPlayers()) {
            if (onlinePlayer.getEyeLocation().distanceSquared(location) <= d && !onlinePlayer.hasMetadata("NPC") && player.canSee(onlinePlayer)) {
                return onlinePlayer;
            }
        }
        return null;
    }

    public static Player getClosestVisiblePlayerFeetFromLocation(Player player, Location location, double range) {
        double d = range * range;

        for (Player onlinePlayer : location.getWorld().getPlayers()) {
            if (onlinePlayer.getLocation().distanceSquared(location) <= d && !onlinePlayer.hasMetadata("NPC") && player.canSee(onlinePlayer)) {
                return onlinePlayer;
            }
        }
        return null;
    }

    public static void findClosestVisiblePlayersFromLocationForPlayer(Player player,
                                                                      Location location,
                                                                      double range,
                                                                      Consumer<Player> consumer) {
        double distanceSquared = range * range;

        for (Player onlinePlayer : location.getWorld().getPlayers()) {
            if (player != onlinePlayer
                    && onlinePlayer.getLocation().distanceSquared(location) <= distanceSquared
                    && !onlinePlayer.hasMetadata("NPC")
                    && onlinePlayer.getGameMode() != GameMode.SPECTATOR
                    && player.canSee(onlinePlayer)) {
                consumer.accept(onlinePlayer);
            }
        }
    }

    public static Location getDirectionalLocation(Location location, double x, double z) {
        double x1 = location.getX() + x;
        double z1 = location.getZ() + z;
        double angle = -(location.getYaw() * Math.PI) / 180.0d;
        Location target = location.clone();
        target.setX(x1);
        target.setZ(z1);

        return location.clone().add(rotateAroundAxisY(target.toVector().subtract(location.toVector()), angle));
    }

    public static Location getLocationAroundCircle(Location center, double radius, double angleInRadian) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY();

        Location loc = new Location(center.getWorld(), x, y, z);
        Vector difference = center.toVector().clone().subtract(loc.toVector());
        loc.setDirection(difference);

        return loc;
    }

    public static void findLocationsInCircle(Location location,
                                             int amount,
                                             double range,
                                             double yOffset,
                                             Consumer<Location> consumer) {
        float radPerLocation = 2 * FastMathUtil.PI / amount;

        for (int i = 0; i < amount; i++) {
            float angle = radPerLocation * i;
            double x = range * FastMathUtil.cos(angle);
            double z = range * FastMathUtil.sin(angle);
            consumer.accept(location.clone().add(x, yOffset, z));
        }
    }

    public static <T> T getRandomElement(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(THREAD_LOCAL_RANDOM.nextInt(list.size()));
    }

    public static void snapToCardinalDirection(Location location) {
        float yaw = location.getYaw();
        location.setYaw(Math.round(yaw / 90.0f) * 90.0f);
    }
}
