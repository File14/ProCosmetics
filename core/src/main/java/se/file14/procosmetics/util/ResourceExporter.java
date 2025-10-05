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
