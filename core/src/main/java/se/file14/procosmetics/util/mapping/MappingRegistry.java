package se.file14.procosmetics.util.mapping;

import se.file14.procosmetics.util.version.BukkitVersion;
import se.file14.procosmetics.util.version.VersionUtil;

import java.util.HashMap;
import java.util.Map;

public class MappingRegistry {

    private static final Map<String, String> MAPPINGS = new HashMap<>();

    public static final String CONNECTION = "connection";
    public static final String ENTITY_TYPES = "entity_types";
    public static final String ENTITY_TYPE_BY_STRING = "entity_types_by_string";
    public static final String GUARDIAN = "guardian";
    public static final String LIVING_ENTITY = "living_entity";

    public static final String SERVERBOUND_INTERACT_PACKET = "ServerboundInteractPacket";
    public static final String SERVERBOUND_INTERACT_PACKET_ID = "ServerboundInteractPacket_id";
    public static final String JUMPING = "jumping";

    static {
        // Version 1.21
        addMapping(CONNECTION, BukkitVersion.v1_21, MappingType.MOJANG, "connection");
        addMapping(CONNECTION, BukkitVersion.v1_21, MappingType.SPIGOT, "e");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_21, MappingType.MOJANG, "EntityType");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_21, MappingType.SPIGOT, "EntityTypes");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_21, MappingType.MOJANG, "byString");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_21, MappingType.SPIGOT, "a");
        addMapping(GUARDIAN, BukkitVersion.v1_21, MappingType.MOJANG, "Guardian");
        addMapping(GUARDIAN, BukkitVersion.v1_21, MappingType.SPIGOT, "EntityGuardian");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_21, MappingType.MOJANG, "LivingEntity");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_21, MappingType.SPIGOT, "EntityLiving");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_21, MappingType.MOJANG, "ServerboundInteractPacket");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_21, MappingType.SPIGOT, "PacketPlayInUseEntity");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_21, MappingType.MOJANG, "entityId");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_21, MappingType.SPIGOT, "b");
        addMapping(JUMPING, BukkitVersion.v1_21, MappingType.MOJANG, "jumping");
        addMapping(JUMPING, BukkitVersion.v1_21, MappingType.SPIGOT, "bB");

        // Version 1.20
        addMapping(CONNECTION, BukkitVersion.v1_20, MappingType.MOJANG, "connection");
        addMapping(CONNECTION, BukkitVersion.v1_20, MappingType.SPIGOT, "e");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_20, MappingType.MOJANG, "EntityType");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_20, MappingType.SPIGOT, "EntityTypes");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_20, MappingType.MOJANG, "byString");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_20, MappingType.SPIGOT, "a");
        addMapping(GUARDIAN, BukkitVersion.v1_20, MappingType.MOJANG, "Guardian");
        addMapping(GUARDIAN, BukkitVersion.v1_20, MappingType.SPIGOT, "EntityGuardian");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_20, MappingType.MOJANG, "LivingEntity");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_20, MappingType.SPIGOT, "EntityLiving");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_20, MappingType.MOJANG, "ServerboundInteractPacket");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_20, MappingType.SPIGOT, "PacketPlayInUseEntity");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_20, MappingType.MOJANG, "entityId");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_20, MappingType.SPIGOT, "b");
        addMapping(JUMPING, BukkitVersion.v1_20, MappingType.MOJANG, "jumping");
        addMapping(JUMPING, BukkitVersion.v1_20, MappingType.SPIGOT, "bn");
    }

    private static void addMapping(String fieldName,
                                   BukkitVersion version,
                                   MappingType mappingType,
                                   String mappedName) {
        if (version == VersionUtil.VERSION && Mapping.MAPPING_TYPE == mappingType) {
            MAPPINGS.put(fieldName, mappedName);
        }
    }

    public static String getMappedFieldName(String fieldName) {
        return MAPPINGS.get(fieldName);
    }
}
