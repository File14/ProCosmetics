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

import java.util.Arrays;
import java.util.List;

public enum BukkitVersion {

    v1_20("1.20.6"),
    v1_21("1.21.9", "1.21.10");

    private final List<String> supportedMinorVersions;

    BukkitVersion(String... supportedVersions) {
        this.supportedMinorVersions = Arrays.asList(supportedVersions);
    }

    public boolean isSupported(String minorVersion) {
        for (String version : supportedMinorVersions) {
            if (minorVersion.startsWith(version)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getSupportedMinorVersions() {
        return supportedMinorVersions;
    }
}
