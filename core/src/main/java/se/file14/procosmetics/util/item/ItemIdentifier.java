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
