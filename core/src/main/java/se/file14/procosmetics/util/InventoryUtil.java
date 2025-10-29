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

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.util.item.ItemIdentifier;

import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryUtil {

    private InventoryUtil() {
    }

    public static void remove(Inventory inventory, Predicate<ItemStack> predicate) {
        for (ItemStack item : inventory) {
            if (predicate.test(item)) {
                inventory.remove(item);
            }
        }
    }

    public static void remove(Inventory inventory, ItemIdentifier itemId) {
        remove(inventory, itemId::is);
    }

    public static boolean has(Inventory inventory, Predicate<ItemStack> predicate) {
        for (ItemStack item : inventory) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean has(Inventory inventory, ItemIdentifier itemId) {
        return has(inventory, itemId::is);
    }

    public static void replace(Inventory inventory, Predicate<ItemStack> predicate, Function<ItemStack, ItemStack> replacement) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item != null && predicate.test(item)) {
                inventory.setItem(i, replacement.apply(item));
            }
        }
    }

    public static void replace(Inventory inventory, ItemIdentifier itemId, Function<ItemStack, ItemStack> replacement) {
        replace(inventory, itemId::is, replacement);
    }
}
