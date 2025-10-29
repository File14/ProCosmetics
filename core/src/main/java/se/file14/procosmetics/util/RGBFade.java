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

public class RGBFade {

    private int r = 255;
    private int g = 0;
    private int b = 0;

    public void nextRGB() {
        if (r == 255 && g < 255 && b == 0) {
            g++;
        }
        if (g == 255 && r > 0 && b == 0) {
            r--;
        }
        if (g == 255 && b < 255 && r == 0) {
            b++;
        }
        if (b == 255 && g > 0 && r == 0) {
            g--;
        }
        if (b == 255 && r < 255 && g == 0) {
            r++;
        }
        if (r == 255 && b > 0 && g == 0) {
            b--;
        }
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
