package se.file14.procosmetics.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static String getStringFromLocation(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static Location getLocationFromString(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        String[] location = s.split(":");

        return new Location(
                Bukkit.getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float.parseFloat(location[4]),
                Float.parseFloat(location[5])
        );
    }

    public static Location copy(Location source, Location destination) {
        destination.setWorld(source.getWorld());
        destination.setX(source.getX());
        destination.setY(source.getY());
        destination.setZ(source.getZ());
        destination.setYaw(source.getYaw());
        destination.setPitch(source.getPitch());

        return destination;
    }

    public static Location center(Location location) {
        location.setX(location.getBlockX() + 0.5d);
        location.setZ(location.getBlockZ() + 0.5d);

        return location;
    }
}