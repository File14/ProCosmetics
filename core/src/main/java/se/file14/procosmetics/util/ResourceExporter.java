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
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceExporter {

    private static final Set<String> EXPORT_DIRECTORIES = Set.of(
            "songs/",
            "data/",
            "structures/",
            "lang/"
    );

    private static final Set<String> EXPORT_FILES = Set.of(
            "languages.json"
    );

    public static void export(ProCosmeticsPlugin plugin) {
        try (InputStream inputStream = plugin.getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .openStream();
             ZipInputStream zip = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            Path dataFolderPath = plugin.getDataFolder().toPath().toAbsolutePath().normalize();

            while ((entry = zip.getNextEntry()) != null) {
                if (!shouldExportEntry(entry)) {
                    zip.closeEntry();
                    continue;
                }
                String entryName = entry.getName();
                Path targetPath = dataFolderPath.resolve(entryName).normalize();

                if (!targetPath.startsWith(dataFolderPath)) {
                    zip.closeEntry();
                    continue;
                }
                File targetFile = targetPath.toFile();

                if (!targetFile.exists()) {
                    plugin.saveResource(entryName, false);
                }
                zip.closeEntry();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to export resources.", e);
        }
    }

    private static boolean shouldExportEntry(ZipEntry entry) {
        if (entry.isDirectory()) {
            return false;
        }
        String name = entry.getName().toLowerCase();

        for (String whitelistedDir : EXPORT_DIRECTORIES) {
            if (name.startsWith(whitelistedDir)) {
                return true;
            }
        }
        return EXPORT_FILES.contains(name);
    }
}
