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
package se.file14.procosmetics.util.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;

public class ItemUtil {

    public static boolean isSimilar(@Nullable ItemStack item1, @Nullable ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false;
        }
        if (item1.getType() != item2.getType()) {
            return false;
        }
        if (item1.getAmount() != item2.getAmount()) {
            return false;
        }
        ItemMeta itemMeta1 = item1.getItemMeta();
        ItemMeta itemMeta2 = item2.getItemMeta();

        if (itemMeta1 == null && itemMeta2 != null || itemMeta1 != null && itemMeta2 == null) {
            return false;
        }
        if (itemMeta1.hasDisplayName() && !itemMeta1.getDisplayName().equals(itemMeta2.getDisplayName())) {
            return false;
        }
        if (itemMeta1.hasLore() && !itemMeta1.getLore().equals(itemMeta2.getLore())) {
            return false;
        }
        if (itemMeta1.hasCustomModelData() && itemMeta2.hasCustomModelData() && itemMeta1.getCustomModelData() != itemMeta2.getCustomModelData()) {
            return false;
        }
        return true;
    }
}
