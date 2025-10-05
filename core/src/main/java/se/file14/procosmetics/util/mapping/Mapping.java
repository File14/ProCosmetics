package se.file14.procosmetics.util.mapping;

import se.file14.procosmetics.util.version.BukkitVersion;
import se.file14.procosmetics.util.version.VersionUtil;

public class Mapping {

    public static MappingType MAPPING_TYPE = MappingType.SPIGOT;

    static {
        try {
            // Check if the server is running Paper
            Class.forName("com.destroystokyo.paper.ParticleBuilder");

            // Starting from 1.20.6 Paper uses Mojang mappings
            if (VersionUtil.isHigherThanOrEqualTo(BukkitVersion.v1_20)) {
                MAPPING_TYPE = MappingType.MOJANG;
            }
        } catch (ClassNotFoundException ignored) {
        }
    }
}
