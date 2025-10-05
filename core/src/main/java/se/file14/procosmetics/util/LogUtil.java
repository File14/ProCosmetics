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
