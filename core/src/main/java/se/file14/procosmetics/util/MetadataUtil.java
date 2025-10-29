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

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import se.file14.procosmetics.ProCosmeticsPlugin;

public class MetadataUtil {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    public static final String ENTITY_METADATA = "PROCOSMETICS_ENTITY";
    public static final String BLOCK_METADATA = "PROCOSMETICS_BLOCK";

    private static final FixedMetadataValue ENTITY_METADATA_VALUE = new FixedMetadataValue(PLUGIN, ENTITY_METADATA);
    private static final FixedMetadataValue BLOCK_METADATA_VALUE = new FixedMetadataValue(PLUGIN, BLOCK_METADATA);

    public static boolean isCustomEntity(Entity entity) {
        return entity.hasMetadata(ENTITY_METADATA);
    }

    public static void setCustomEntity(Entity entity) {
        entity.setMetadata(ENTITY_METADATA, ENTITY_METADATA_VALUE);

        setEntityProperties(entity);
    }

    private static void setEntityProperties(Entity entity) {
        entity.setPersistent(false);
        entity.setInvulnerable(true);
    }

    public static boolean isCustomBlock(Block block) {
        return block.hasMetadata(BLOCK_METADATA);
    }

    public static void setCustomBlock(Block block) {
        block.setMetadata(BLOCK_METADATA, BLOCK_METADATA_VALUE);
    }

    public static void removeCustomBlock(Block block) {
        block.removeMetadata(BLOCK_METADATA, PLUGIN);
    }
}
