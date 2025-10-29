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
package se.file14.procosmetics.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;

public class HologramLine {

    private final Hologram hologram;
    private final Spacing spacing;
    private NMSEntity entity;

    HologramLine(Hologram hologram, Component text, Spacing spacing, EntityTrackerImpl tracker) {
        this.hologram = hologram;
        this.spacing = spacing;

        if (!text.equals(Component.empty())) {
            entity = ProCosmeticsPlugin.getPlugin().getNMSManager().createEntity(hologram.getWorld(), EntityType.ARMOR_STAND, tracker);
            entity.setCustomName(text);

            if (entity.getBukkitEntity() instanceof ArmorStand armorStand) {
                armorStand.setInvisible(true);
                armorStand.setSmall(true);
                armorStand.setArms(false);
            }
        }
    }

    public Spacing getSpacing() {
        return spacing;
    }

    public double getHeight() {
        return 0.25d;
    }

    void setY(double y) {
        if (entity != null) {
            entity.setPositionRotation(new Location(hologram.getWorld(), hologram.getX(), y, hologram.getZ()));
        }
    }
}
