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
import org.bukkit.World;
import se.file14.procosmetics.nms.EntityTrackerImpl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Hologram {

    // NOTE: Simple implementation at the moment. In the future, we'll make it so holograms are personal!

    private final EntityTrackerImpl tracker = new EntityTrackerImpl();
    private final List<HologramLine> lines = new ArrayList<>();
    private final Reference<World> world;
    private double x;
    private double y;
    private double z;

    public Hologram(Location location) {
        world = new WeakReference<>(location.getWorld());
        x = location.getX();
        y = location.getY();
        z = location.getZ();
    }

    public void spawn() {
        tracker.startTracking();
    }

    public void despawn() {
        tracker.stopTracking();
    }

    public HologramLine getLine(int index) {
        return index >= lines.size() || index < 0 ? null : lines.get(index);
    }

    public Hologram addLines(List<Component> lines, Spacing spacing) {
        for (Component line : lines) {
            addLine(line, spacing);
        }
        return this;
    }

    public Hologram addLine(Component text, Spacing spacing) {
        return insertLine(lines.size(), text, spacing);
    }

    private Hologram insertLine(int index, Component text, Spacing spacing) {
        return addLine(index, new HologramLine(this, text, spacing, tracker));
    }

    private Hologram addLine(int index, HologramLine line) {
        lines.add(Math.min(Math.max(index, 0), lines.size()), line);
        updatePosition();
        return this;
    }

    private void updatePosition() {
        double y = getY();

        for (int i = lines.size() - 1; i >= 0; i--) {
            HologramLine line = lines.get(i);
            y += line.getSpacing().getValue();
            line.setY(y);
            y += line.getHeight();
        }
    }

    public World getWorld() {
        return world.get();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        updatePosition();
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }
}
