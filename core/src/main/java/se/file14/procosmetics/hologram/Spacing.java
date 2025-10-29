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

public class Spacing {

    public static final Spacing NONE = new Spacing(0.0d);
    public static final Spacing SMALL = new Spacing(0.0625d);
    public static final Spacing FULL = new Spacing(0.25d);

    private final double value;

    private Spacing(double value) {
        this.value = value;
    }

    public static Spacing of(double value) {
        return new Spacing(value);
    }

    public double getValue() {
        return value;
    }
}
