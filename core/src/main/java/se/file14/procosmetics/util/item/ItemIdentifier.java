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

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.util.item.ItemBuilder;

public class ItemIdentifier {

    public static final NamespacedKey ITEM_ID_KEY = new NamespacedKey("procosmetics", "id");
    private final String key;

    public ItemIdentifier(String key) {
        this.key = key;
    }

    public <M extends ItemMeta> ItemBuilder apply(ItemBuilder itemBuilder) {
        return itemBuilder.setCustomData(ITEM_ID_KEY, PersistentDataType.STRING, key);
    }

    public ItemStack apply(ItemStack item) {
        return apply(ItemBuilderImpl.of(item)).getItemStack();
    }

    @Contract("null -> false")
    public boolean is(@Nullable ItemStack item) {
        return item != null
                && item.hasItemMeta()
                && key.equals(item.getItemMeta().getPersistentDataContainer().get(ITEM_ID_KEY, PersistentDataType.STRING));
    }

    public String getKey() {
        return key;
    }

    public String toString() {
        return key;
    }

    @Contract("null -> null")
    public static @Nullable ItemIdentifier get(@Nullable ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        } else {
            String id = item.getItemMeta().getPersistentDataContainer().get(ITEM_ID_KEY, PersistentDataType.STRING);
            return id != null ? new ItemIdentifier(id) : null;
        }
    }
}
