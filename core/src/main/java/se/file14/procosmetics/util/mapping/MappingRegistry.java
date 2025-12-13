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
        // Version 1.21.11
        addMapping(CONNECTION, BukkitVersion.v1_21_11, MappingType.MOJANG, "connection");
        addMapping(CONNECTION, BukkitVersion.v1_21_11, MappingType.SPIGOT, "e");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_21_11, MappingType.MOJANG, "EntityType");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_21_11, MappingType.SPIGOT, "EntityTypes");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_21_11, MappingType.MOJANG, "byString");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_21_11, MappingType.SPIGOT, "a");
        addMapping(GUARDIAN, BukkitVersion.v1_21_11, MappingType.MOJANG, "Guardian");
        addMapping(GUARDIAN, BukkitVersion.v1_21_11, MappingType.SPIGOT, "EntityGuardian");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_21_11, MappingType.MOJANG, "LivingEntity");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_21_11, MappingType.SPIGOT, "EntityLiving");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_21_11, MappingType.MOJANG, "ServerboundInteractPacket");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_21_11, MappingType.SPIGOT, "PacketPlayInUseEntity");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_21_11, MappingType.MOJANG, "entityId");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_21_11, MappingType.SPIGOT, "b");
        addMapping(JUMPING, BukkitVersion.v1_21_11, MappingType.MOJANG, "jumping");
        addMapping(JUMPING, BukkitVersion.v1_21_11, MappingType.SPIGOT, "bB");

        // Version 1.21.10
        addMapping(CONNECTION, BukkitVersion.v1_21_10, MappingType.MOJANG, "connection");
        addMapping(CONNECTION, BukkitVersion.v1_21_10, MappingType.SPIGOT, "e");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_21_10, MappingType.MOJANG, "EntityType");
        addMapping(ENTITY_TYPES, BukkitVersion.v1_21_10, MappingType.SPIGOT, "EntityTypes");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_21_10, MappingType.MOJANG, "byString");
        addMapping(ENTITY_TYPE_BY_STRING, BukkitVersion.v1_21_10, MappingType.SPIGOT, "a");
        addMapping(GUARDIAN, BukkitVersion.v1_21_10, MappingType.MOJANG, "Guardian");
        addMapping(GUARDIAN, BukkitVersion.v1_21_10, MappingType.SPIGOT, "EntityGuardian");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_21_10, MappingType.MOJANG, "LivingEntity");
        addMapping(LIVING_ENTITY, BukkitVersion.v1_21_10, MappingType.SPIGOT, "EntityLiving");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_21_10, MappingType.MOJANG, "ServerboundInteractPacket");
        addMapping(SERVERBOUND_INTERACT_PACKET, BukkitVersion.v1_21_10, MappingType.SPIGOT, "PacketPlayInUseEntity");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_21_10, MappingType.MOJANG, "entityId");
        addMapping(SERVERBOUND_INTERACT_PACKET_ID, BukkitVersion.v1_21_10, MappingType.SPIGOT, "b");
        addMapping(JUMPING, BukkitVersion.v1_21_10, MappingType.MOJANG, "jumping");
        addMapping(JUMPING, BukkitVersion.v1_21_10, MappingType.SPIGOT, "bB");

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
