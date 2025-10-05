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
