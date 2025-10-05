package se.file14.procosmetics.util.version;

import org.bukkit.Bukkit;

import java.util.StringJoiner;

public class VersionUtil {

    public static BukkitVersion VERSION;

    static {
        String versionString = Bukkit.getServer().getBukkitVersion();

        for (BukkitVersion bVersion : BukkitVersion.values()) {
            if (bVersion.isSupported(versionString)) {
                VERSION = bVersion;
                break;
            }
        }
    }

    public static String getUnsupportedMessage() {
        StringJoiner versions = new StringJoiner(", ");

        for (BukkitVersion bukkitVersion : BukkitVersion.values()) {
            for (String minorVersion : bukkitVersion.getSupportedMinorVersions()) {
                versions.add(minorVersion);
            }
        }
        return "This server version is NOT SUPPORTED in this plugin version! Please check the changelog of the plugin" +
                " on SpigotMC. Current supported versions: " + versions;
    }

    public static boolean isSupported() {
        return VERSION != null;
    }

    public static boolean isHigherThan(BukkitVersion bukkitVersion) {
        return VERSION.ordinal() > bukkitVersion.ordinal();
    }

    public static boolean isHigherThanOrEqualTo(BukkitVersion bukkitVersion) {
        return VERSION.ordinal() >= bukkitVersion.ordinal();
    }

    public static boolean isLowerThan(BukkitVersion bukkitVersion) {
        return VERSION.ordinal() < bukkitVersion.ordinal();
    }

    public static boolean isLowerThanOrEqualTo(BukkitVersion bukkitVersion) {
        return VERSION.ordinal() <= bukkitVersion.ordinal();
    }

    public static boolean isEqualsTo(BukkitVersion bukkitVersion) {
        return VERSION.ordinal() == bukkitVersion.ordinal();
    }

    public static boolean isEqualsTo(BukkitVersion... bukkitVersions) {
        for (BukkitVersion bukkitVersion : bukkitVersions) {
            if (VERSION.ordinal() == bukkitVersion.ordinal()) {
                return true;
            }
        }
        return false;
    }
}