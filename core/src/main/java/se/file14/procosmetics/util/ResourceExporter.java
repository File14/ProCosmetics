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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceExporter {

    private static final List<String> FILE_TYPES = List.of("json", "nbs");

    public static void export(ProCosmeticsPlugin plugin) {
        try (InputStream inputStream = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().openStream();
             ZipInputStream zip = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry;

            while ((zipEntry = zip.getNextEntry()) != null) {
                String name = zipEntry.getName();

                for (String fileType : FILE_TYPES) {
                    if (name.endsWith(fileType)) {
                        File file = new File(plugin.getDataFolder().toPath().toString(), name);

                        if (!file.exists()) {
                            plugin.saveResource(name, false);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to export a resource.", e);
        }
    }
}
