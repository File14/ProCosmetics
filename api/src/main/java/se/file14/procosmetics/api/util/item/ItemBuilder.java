package se.file14.procosmetics.api.util.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple builder interface for creating and modifying Bukkit ItemStacks.
 * <p>
 * This interface provides a convenient way to construct and customize items with
 * various properties such as display names, lore, enchantments, and metadata.
 * All modification methods return the builder instance for method chaining.
 * </p>
 */
public interface ItemBuilder {

    /**
     * Sets the material type of the item.
     *
     * @param material the new material type
     * @return this builder instance for chaining
     */
    ItemBuilder setType(Material material);

    /**
     * Sets the stack size of the item.
     * <p>
     * The amount will be clamped to a minimum of 1 to ensure a valid stack.
     * </p>
     *
     * @param amount the desired stack size (minimum 1)
     * @return this builder instance for chaining
     */
    ItemBuilder setAmount(int amount);

    /**
     * Stores custom data in the item's persistent data container.
     * <p>
     * This data persists across server restarts and can be used to store
     * plugin-specific information on the item.
     * </p>
     *
     * @param key   the namespaced key identifying the data
     * @param type  the persistent data type
     * @param value the value to store
     * @param <T>   the primitive type
     * @param <Z>   the complex type
     * @return this builder instance for chaining
     */
    <T, Z> ItemBuilder setCustomData(NamespacedKey key, PersistentDataType<T, Z> type, Z value);

    /**
     * Sets the display name of the item using a Component.
     * <p>
     * The component will be serialized to legacy format for storage.
     * Passing null will clear the display name.
     * </p>
     *
     * @param displayName the display name component, or null to clear
     * @return this builder instance for chaining
     */
    ItemBuilder setDisplayName(@Nullable Component displayName);

    /**
     * Sets the display name of the item using a legacy string.
     * <p>
     * Passing null will clear the display name.
     * </p>
     *
     * @param displayName the display name string, or null to clear
     * @return this builder instance for chaining
     */
    ItemBuilder setDisplayName(@Nullable String displayName);

    /**
     * Retrieves the current display name as a legacy string.
     *
     * @return the display name, or an empty string if none exists
     */
    String getDisplayName();

    /**
     * Retrieves the current display name as a Component.
     *
     * @return the display name component, or an empty component if none exists
     */
    Component getDisplayNameComponent();

    /**
     * Sets the lore of the item using a list of Components.
     * <p>
     * Each component will be serialized to legacy format for storage.
     * Passing null will clear the lore.
     * </p>
     *
     * @param lore the lore components, or null to clear
     * @return this builder instance for chaining
     */
    ItemBuilder setLoreComponent(@Nullable List<Component> lore);

    /**
     * Appends a single line to the item's lore.
     *
     * @param loreLine the component to add to the lore
     * @return this builder instance for chaining
     */
    ItemBuilder addLore(Component loreLine);

    /**
     * Appends multiple lines to the item's lore.
     *
     * @param loreLines the components to add to the lore
     * @return this builder instance for chaining
     */
    ItemBuilder addLore(List<Component> loreLines);

    /**
     * Retrieves the current lore as a list of legacy strings.
     *
     * @return the lore lines, or an empty list if none exists
     */
    List<String> getLore();

    /**
     * Retrieves the current lore as a list of Components.
     *
     * @return the lore components, or an empty list if none exists
     */
    List<Component> getLoreComponents();

    /**
     * Makes the item unbreakable and hides the unbreakable flag.
     *
     * @return this builder instance for chaining
     */
    ItemBuilder setUnbreakable();

    /**
     * Sets the unbreakable state of the item.
     * <p>
     * When set to true, also hides the unbreakable flag from the tooltip.
     * When set to false, removes the hide flag.
     * </p>
     *
     * @param unbreakable whether the item should be unbreakable
     * @return this builder instance for chaining
     */
    ItemBuilder setUnbreakable(boolean unbreakable);

    /**
     * Overrides the enchantment glint visibility for the item.
     * <p>
     * This allows items to show or hide the enchantment glint effect
     * regardless of whether they have enchantments.
     * </p>
     *
     * @param override whether to override the default glint behavior
     * @return this builder instance for chaining
     */
    ItemBuilder setGlintOverride(boolean override);

    /**
     * Adds an enchantment to the item, bypassing level restrictions.
     *
     * @param enchantment the enchantment to add
     * @param level       the enchantment level
     * @return this builder instance for chaining
     */
    ItemBuilder addEnchant(Enchantment enchantment, int level);

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment the enchantment to add
     * @param level       the enchantment level
     * @param unsafe      whether to bypass level restrictions
     * @return this builder instance for chaining
     */
    ItemBuilder addEnchant(Enchantment enchantment, int level, boolean unsafe);

    /**
     * Removes an enchantment from the item.
     *
     * @param enchantment the enchantment to remove
     * @return this builder instance for chaining
     */
    ItemBuilder removeEnchant(Enchantment enchantment);

    /**
     * Sets the color of leather armor.
     * <p>
     * This method only has effect if the item is a leather armor piece.
     * </p>
     *
     * @param color the color to apply
     * @return this builder instance for chaining
     */
    ItemBuilder setLeatherArmorColor(Color color);

    /**
     * Adds a custom potion effect to the item.
     * <p>
     * This method only has effect if the item is a potion.
     * </p>
     *
     * @param effect    the potion effect to add
     * @param overwrite whether to overwrite existing effects of the same type
     * @return this builder instance for chaining
     */
    ItemBuilder addPotionEffect(PotionEffect effect, boolean overwrite);

    /**
     * Sets the owner of the skull item to the specified player.
     *
     * @param player the player whose skin will be applied to the skull
     * @return this builder instance for chaining
     */
    ItemBuilder setSkullOwner(Player player);

    /**
     * Sets a custom skull texture on the item.
     *
     * @param texture the texture string to apply
     * @return this builder instance for chaining
     */
    ItemBuilder setSkullTexture(String texture);

    /**
     * Adds item flags to hide specific attributes in the tooltip.
     *
     * @param flags the flags to add
     * @return this builder instance for chaining
     */
    ItemBuilder addFlags(ItemFlag... flags);

    /**
     * Removes item flags, showing previously hidden attributes.
     *
     * @param flags the flags to remove
     * @return this builder instance for chaining
     */
    ItemBuilder removeFlags(ItemFlag... flags);

    /**
     * Hides all possible item attributes in the tooltip.
     * <p>
     * This applies all available item flags to minimize tooltip information.
     * </p>
     *
     * @return this builder instance for chaining
     */
    ItemBuilder hide();

    /**
     * Modifies the item's metadata using a custom consumer.
     * <p>
     * This provides direct access to the ItemMeta for advanced customization.
     * </p>
     *
     * @param modifier the consumer to modify the metadata
     * @return this builder instance for chaining
     */
    ItemBuilder modifyItemMeta(Consumer<ItemMeta> modifier);

    /**
     * Modifies the underlying ItemStack using a custom consumer.
     * <p>
     * This provides direct access to the ItemStack for advanced customization.
     * </p>
     *
     * @param modifier the consumer to modify the item stack
     * @return this builder instance for chaining
     */
    ItemBuilder modifyItemStack(Consumer<ItemStack> modifier);

    /**
     * Checks if the item has a specific enchantment.
     *
     * @param enchantment the enchantment to check
     * @return true if the item has the enchantment
     */
    boolean hasEnchant(Enchantment enchantment);

    /**
     * Gets the level of a specific enchantment on the item.
     *
     * @param enchantment the enchantment to query
     * @return the enchantment level, or 0 if not present
     */
    int getEnchantLevel(Enchantment enchantment);

    /**
     * Checks if the item has any enchantments.
     *
     * @return true if the item has one or more enchantments
     */
    boolean hasEnchants();

    /**
     * Gets all enchantments on the item.
     *
     * @return a map of enchantments to their levels
     */
    Map<Enchantment, Integer> getEnchants();

    /**
     * Gets the material type of the item.
     *
     * @return the item's material
     */
    Material getType();

    /**
     * Gets the stack size of the item.
     *
     * @return the item amount
     */
    int getAmount();

    /**
     * Checks if the item has a custom display name.
     *
     * @return true if a display name is set
     */
    boolean hasDisplayName();

    /**
     * Checks if the item has lore.
     *
     * @return true if lore is set
     */
    boolean hasLore();

    /**
     * Checks if the item is unbreakable.
     *
     * @return true if the item is unbreakable
     */
    boolean isUnbreakable();

    /**
     * Builds and returns the final ItemStack.
     *
     * @return the constructed item stack
     */
    ItemStack getItemStack();

    /**
     * Gets the inventory slot assigned to this item.
     *
     * @return the slot number
     */
    int getSlot();

    /**
     * Sets the inventory slot for this item.
     *
     * @param slot the slot number
     */
    void setSlot(int slot);
}