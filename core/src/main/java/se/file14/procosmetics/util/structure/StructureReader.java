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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
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
    private static final String MATERIAL_KEY = "material";
    private static final String BLOCK_FACE_KEY = "facing";
    private static final String ROTATION_FACE_KEY = "rotation";
    private static final String OPEN_KEY = "open";
    private static final String WATER_LOGGED_KEY = "water_logged";
    private static final String ATTACHABLE_KEY = "attachable";
    private static final String BISECTED_KEY = "bisected";
    private static final String SHAPE_KEY = "shape";
    private static final String SLAB_KEY = "slab";
    private static final String FACE_ATTACHABLE_KEY = "face_attachable";
    private static final String MULTIPLE_FACING_KEY = "multiple_facing";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
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
                Material material;
                String materialString = blockObject.get(MATERIAL_KEY).getAsString();

                try {
                    material = Material.valueOf(materialString);
                } catch (IllegalArgumentException e) {
                    PLUGIN.getLogger().log(Level.WARNING, "Invalid material " + materialString + " in structure file " + file.getName() + ". Skipping material.", e);
                    continue;
                }

                if (material.isAir()) {
                    PLUGIN.getLogger().log(Level.WARNING, "Air material found in structure file " + file.getName() + ". Skipping block at " + vector + ".");
                    continue;
                }
                BlockData blockData = material.createBlockData();

                if (blockData instanceof Directional directional && blockObject.has(BLOCK_FACE_KEY)) {
                    directional.setFacing(BlockFace.valueOf(blockObject.get(BLOCK_FACE_KEY).getAsString()));
                }
                if (blockData instanceof Rotatable rotatable && blockObject.has(ROTATION_FACE_KEY)) {
                    rotatable.setRotation(BlockFace.valueOf(blockObject.get(ROTATION_FACE_KEY).getAsString()));
                }
                if (blockData instanceof Openable openable && blockObject.has(OPEN_KEY)) {
                    openable.setOpen(blockObject.get(OPEN_KEY).getAsBoolean());
                }
                if (blockData instanceof Waterlogged waterlogged && blockObject.has(WATER_LOGGED_KEY)) {
                    waterlogged.setWaterlogged(blockObject.get(WATER_LOGGED_KEY).getAsBoolean());
                }
                if (blockData instanceof Attachable attachable && blockObject.has(ATTACHABLE_KEY)) {
                    attachable.setAttached(blockObject.get(ATTACHABLE_KEY).getAsBoolean());
                }
                if (blockData instanceof Bisected bisected && blockObject.has(BISECTED_KEY)) {
                    bisected.setHalf(Bisected.Half.valueOf(blockObject.get(BISECTED_KEY).getAsString()));
                }
                if (blockData instanceof Stairs stairs && blockObject.has(SHAPE_KEY)) {
                    stairs.setShape(Stairs.Shape.valueOf(blockObject.get(SHAPE_KEY).getAsString()));
                }
                if (blockData instanceof Slab slab && blockObject.has(SLAB_KEY)) {
                    slab.setType(Slab.Type.valueOf(blockObject.get(SLAB_KEY).getAsString()));
                }
                if (blockData instanceof FaceAttachable faceAttachable && blockObject.has(FACE_ATTACHABLE_KEY)) {
                    faceAttachable.setAttachedFace(FaceAttachable.AttachedFace.valueOf(blockObject.get(FACE_ATTACHABLE_KEY).getAsString()));
                }
                if (blockData instanceof MultipleFacing multipleFacing && blockObject.has(MULTIPLE_FACING_KEY)) {
                    JsonArray facesArray = blockObject.getAsJsonArray(MULTIPLE_FACING_KEY);

                    for (JsonElement faceElement : facesArray) {
                        multipleFacing.setFace(BlockFace.valueOf(faceElement.getAsString()), true);
                    }
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
                arrayObject.addProperty(MATERIAL_KEY, material.name());

                if (blockData instanceof Directional directional) {
                    arrayObject.addProperty(BLOCK_FACE_KEY, directional.getFacing().name());
                }
                if (blockData instanceof Rotatable rotatable) {
                    arrayObject.addProperty(ROTATION_FACE_KEY, rotatable.getRotation().name());
                }
                if (blockData instanceof Openable openable) {
                    arrayObject.addProperty(OPEN_KEY, openable.isOpen());
                }
                if (blockData instanceof Waterlogged waterlogged) {
                    arrayObject.addProperty(WATER_LOGGED_KEY, waterlogged.isWaterlogged());
                }
                if (blockData instanceof Attachable attachable) {
                    arrayObject.addProperty(ATTACHABLE_KEY, attachable.isAttached());
                }
                if (blockData instanceof Bisected bisected) {
                    arrayObject.addProperty(BISECTED_KEY, bisected.getHalf().name());
                }
                if (blockData instanceof Stairs stairs) {
                    arrayObject.addProperty(SHAPE_KEY, stairs.getShape().name());
                }
                if (blockData instanceof Slab slab) {
                    arrayObject.addProperty(SLAB_KEY, slab.getType().name());
                }
                if (blockData instanceof FaceAttachable faceAttachable) {
                    arrayObject.addProperty(FACE_ATTACHABLE_KEY, faceAttachable.getAttachedFace().name());
                }
                if (blockData instanceof MultipleFacing multipleFacing) {
                    JsonArray facesArray = new JsonArray();
                    for (BlockFace face : multipleFacing.getFaces()) {
                        facesArray.add(face.name());
                    }
                    arrayObject.add(MULTIPLE_FACING_KEY, facesArray);
                }
                blocks.add(arrayObject);
            }
            jsonObject.add(BLOCKS_KEY, blocks);

            GSON.toJson(jsonObject, writer);
        } catch (IOException e) {
            PLUGIN.getLogger().log(Level.WARNING, "Failed to save structure " + file.getName() + ".", e);
        }
    }
}
