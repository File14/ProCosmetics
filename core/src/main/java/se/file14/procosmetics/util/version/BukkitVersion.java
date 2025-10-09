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