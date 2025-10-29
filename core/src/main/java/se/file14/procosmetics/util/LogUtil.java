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

import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.util.version.VersionUtil;

import java.io.File;
import java.util.logging.Logger;

public class LogUtil {

    private static final Logger LOGGER = ProCosmeticsPlugin.getPlugin().getLogger();

    public static void logMissingKey(String variableType, String key, File file) {
        log(String.format("Missing %s in %s with key %s. This appears to be an issue with YOUR configuration! Please delete %s and RESTART your server before requesting support!",
                          variableType, file.getName(), key, file.getName()
        ));
    }

    public static void log(String message) {
        printEmptyLines(1);
        LOGGER.severe(message);
        printEmptyLines(1);
    }

    private static void printEmptyLines(int amount) {
        for (int i = 0; i < amount; i++) {
            LOGGER.info(" ");
        }
    }

    public static void printUnsupported() {
        log(VersionUtil.getUnsupportedMessage());
    }
}
