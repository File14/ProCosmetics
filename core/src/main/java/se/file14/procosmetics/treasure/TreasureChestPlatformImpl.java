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
package se.file14.procosmetics.treasure;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.locale.LanguageManager;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.hologram.Hologram;
import se.file14.procosmetics.hologram.Spacing;
import se.file14.procosmetics.util.structure.NamedStructureData;
import se.file14.procosmetics.util.structure.type.BlockStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreasureChestPlatformImpl implements TreasureChestPlatform {

    private static final List<Material> CHEST_TYPES = List.of(
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.ENDER_CHEST
    );

    private final ProCosmeticsPlugin plugin;
    private final int id;
    private final Location center;
    private final NamedStructureData namedStructureData;
    private final BlockStructure blockStructure;

    private final List<Location> chestLocations = new ArrayList<>();
    private Hologram hologram;
    private User user;

    public TreasureChestPlatformImpl(ProCosmeticsPlugin plugin, int id, Location center, NamedStructureData structureData) {
        this.plugin = plugin;
        this.id = id;
        this.center = center;
        this.namedStructureData = structureData;
        this.blockStructure = new BlockStructure(structureData.structureData());

        // We need to do this because the center can have a yaw set
        Location chestLocation = center.clone();
        chestLocation.setPitch(0.0f);
        chestLocation.setYaw(0.0f);

        addChestLocations(
                chestLocation.clone().add(-1.0d, 0.0d, -3.0d),
                chestLocation.clone().add(1.0d, 0.0d, -3.0d),
                chestLocation.clone().add(3.0d, 0.0d, -1.0d),
                chestLocation.clone().add(3.0d, 0.0d, 1.0d),
                chestLocation.clone().add(1.0d, 0.0d, 3.0d),
                chestLocation.clone().add(-1.0d, 0.0d, 3.0d),
                chestLocation.clone().add(-3.0d, 0.0d, 1.0d),
                chestLocation.clone().add(-3.0d, 0.0d, -1.0d)
        );
        chestLocations.forEach(chestLoc -> chestLoc.getBlock().setType(Material.AIR));
        build();
    }

    @Override
    public void build() {
        blockStructure.spawn(center, true);

        if (hologram == null) {
            hologram = new Hologram(center.clone().add(0.0d, -0.2d, 0.0d));
            hologram.addLines(
                    plugin.getLanguageManager().renderList("treasure_chest.platform.hologram", LanguageManager.DEFAULT_LOCALE), // use default for now
                    Spacing.SMALL
            );
        }
        hologram.spawn();
    }

    @Override
    public void destroy() {
        blockStructure.remove();
        hologram.despawn();
    }

    public void destroyChest() {
        for (Block block : blockStructure.getPlacedEntries()) {
            if (CHEST_TYPES.contains(block.getType())) {
                block.setType(Material.AIR);
            }
        }
    }

    private void addChestLocations(Location... location) {
        Collections.addAll(chestLocations, location);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Location getCenter() {
        return center;
    }

    public NamedStructureData getNamedStructureData() {
        return namedStructureData;
    }

    public BlockStructure getBlockStructure() {
        return blockStructure;
    }

    @Override
    public List<Location> getChestLocations() {
        return chestLocations;
    }

    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean isInUse() {
        return user != null;
    }
}
