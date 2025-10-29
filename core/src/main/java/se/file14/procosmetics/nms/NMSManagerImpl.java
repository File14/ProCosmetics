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
package se.file14.procosmetics.nms;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSManager;
import se.file14.procosmetics.nms.entitytype.CachedEntityType;
import se.file14.procosmetics.nms.entitytype.EntityTypeRegistry;
import se.file14.procosmetics.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class NMSManagerImpl implements NMSManager {

    private final ProCosmeticsPlugin plugin;
    private final Constructor<?> nmsEntityConstructor;
    private final Constructor<?> nmsFallingBlockConstructor;
    private final Constructor<?> realEntityConstructor;
    private final Constructor<?> nmsEquipmentConstructor;
    private NMSUtilImpl nmsUtil;

    public NMSManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        String path = ReflectionUtil.VERSION_CLASS_PATH;
        Class<?> packetEntityClass = ReflectionUtil.getClass(path + "NMSEntity");
        Class<?> equipmentClass = ReflectionUtil.getClass(path + "NMSEquipment");

        nmsEntityConstructor = ReflectionUtil.getConstructor(packetEntityClass, World.class, CachedEntityType.class, EntityTracker.class);
        nmsFallingBlockConstructor = ReflectionUtil.getConstructor(packetEntityClass, World.class, BlockData.class, EntityTracker.class);
        realEntityConstructor = ReflectionUtil.getConstructor(packetEntityClass, Entity.class);
        nmsEquipmentConstructor = ReflectionUtil.getConstructor(equipmentClass, Player.class, boolean.class);

        try {
            nmsUtil = (NMSUtilImpl) ReflectionUtil.getClass(path + "NMSUtil").getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to instantiate NMSUtil. ", e);
        }
    }

    @Override
    public NMSEntityImpl createEntity(World world, EntityType entityType) {
        return createEntity(world, entityType, null);
    }

    @Override
    public NMSEntityImpl createEntity(World world, EntityType entityType, EntityTracker entityTracker) {
        if (entityTracker == null) {
            entityTracker = new EntityTrackerImpl();
        }
        CachedEntityType nmsEntityType = EntityTypeRegistry.getCachedEntityType(entityType);

        try {
            return (NMSEntityImpl) nmsEntityConstructor.newInstance(world, nmsEntityType, entityTracker);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to create NMS entity for type " + entityType.name() + ".", e);
        }
        return null;
    }

    @Override
    public NMSEntityImpl createFallingBlock(World world, BlockData blockData, EntityTracker entityTracker) {
        try {
            return (NMSEntityImpl) nmsFallingBlockConstructor.newInstance(world, blockData, entityTracker);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to create NMS falling block entity.", e);
        }
        return null;
    }

    @Override
    public NMSEntityImpl entityToNMSEntity(Entity entity) {
        try {
            return (NMSEntityImpl) realEntityConstructor.newInstance(entity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to convert entity " + entity.getType().name() + " to NMS entity.", e);
        }
        return null;
    }

    public AbstractNMSEquipment<?> createEquipment(Player player, boolean tracker) {
        try {
            return (AbstractNMSEquipment<?>) nmsEquipmentConstructor.newInstance(player, tracker);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to setup NMS equipment for player " + player.getName() + ".", e);
        }
        return null;
    }

    @Override
    public NMSUtilImpl getNMSUtil() {
        return nmsUtil;
    }
}
