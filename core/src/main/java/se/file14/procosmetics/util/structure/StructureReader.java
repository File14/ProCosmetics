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
package se.file14.procosmetics.util.structure;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class StructureReader {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private static final String BLOCKS_KEY = "blocks";
    private static final String VECTOR_KEY = "vector";
    private static final String DATA_KEY = "data";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public StructureReader() {
    }

    @Nullable
    public static StructureDataImpl loadStructure(String name) {
        File file = new File(PLUGIN.getDataFolder().toPath().resolve("structures").toString(), name + ".json");

        if (file.exists()) {
            return readStructure(file);
        }
        return null;
    }

    private static StructureDataImpl readStructure(File file) {
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
            JsonArray blocks = jsonObject.getAsJsonArray(BLOCKS_KEY);

            Map<Vector, BlockData> placement = new HashMap<>();

            for (JsonElement element : blocks) {
                JsonObject blockObject = element.getAsJsonObject();
                JsonObject vectorObject = blockObject.getAsJsonObject(VECTOR_KEY);

                double x = vectorObject.get("x").getAsDouble();
                double y = vectorObject.get("y").getAsDouble();
                double z = vectorObject.get("z").getAsDouble();
                Vector vector = new Vector(x, y, z);
                BlockData blockData;

                if (blockObject.has(DATA_KEY)) {
                    blockData = Bukkit.createBlockData(blockObject.get(DATA_KEY).getAsString());
                } else {
                    PLUGIN.getLogger().log(Level.WARNING, "Missing block data in " + file.getName() + ".");
                    continue;
                }
                placement.put(vector, blockData);
            }
            return new StructureDataImpl(placement);
        } catch (IOException e) {
            PLUGIN.getLogger().log(Level.WARNING, "Failed to load structure " + file.getName() + ".", e);
        }
        return null;
    }

    public static StructureDataImpl createStructureData(Location center, Location start, Location end) {
        Map<Vector, BlockData> placement = new HashMap<>();

        int startX = Math.min(start.getBlockX(), end.getBlockX());
        int endX = Math.max(start.getBlockX(), end.getBlockX());
        int startY = Math.min(start.getBlockY(), end.getBlockY());
        int endY = Math.max(start.getBlockY(), end.getBlockY());
        int startZ = Math.min(start.getBlockZ(), end.getBlockZ());
        int endZ = Math.max(start.getBlockZ(), end.getBlockZ());

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    Vector worldPos = new Vector(x, y, z);
                    Vector relativePos = worldPos.subtract(new Vector(center.getBlockX(), center.getBlockY(), center.getBlockZ()));

                    Location location = new Location(start.getWorld(), x, y, z);
                    Block block = location.getBlock();
                    BlockData blockData = block.getBlockData();
                    Material material = blockData.getMaterial();

                    if (!material.isAir()) {
                        placement.put(relativePos, blockData);
                    }
                }
            }
        }
        return new StructureDataImpl(placement);
    }

    public static void writeStructure(StructureDataImpl data, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            JsonObject jsonObject = new JsonObject();
            JsonArray blocks = new JsonArray();

            for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
                JsonObject arrayObject = new JsonObject();
                Vector vector = entry.getKey();
                BlockData blockData = entry.getValue();

                JsonObject vectorObj = new JsonObject();
                vectorObj.addProperty("x", vector.getX());
                vectorObj.addProperty("y", vector.getY());
                vectorObj.addProperty("z", vector.getZ());

                arrayObject.add(VECTOR_KEY, vectorObj);

                Material material = blockData.getMaterial();

                if (material.isAir()) {
                    continue;
                }
                arrayObject.addProperty(DATA_KEY, blockData.getAsString());
                blocks.add(arrayObject);
            }
            jsonObject.add(BLOCKS_KEY, blocks);

            GSON.toJson(jsonObject, writer);
        } catch (IOException e) {
            PLUGIN.getLogger().log(Level.WARNING, "Failed to save structure " + file.getName() + ".", e);
        }
    }
}
