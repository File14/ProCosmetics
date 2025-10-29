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
