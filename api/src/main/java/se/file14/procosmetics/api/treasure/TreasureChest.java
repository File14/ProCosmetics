package se.file14.procosmetics.api.treasure;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.animation.AnimationType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.api.util.structure.StructureData;

import java.util.List;

/**
 * Represents a treasure chest definition.
 * <p>
 * Treasure chests allow players to open animated sequences that reward
 * them with cosmetics or other items.
 */
public interface TreasureChest {

    /**
     * Checks if the player has permission to purchase this treasure chest.
     *
     * @param player the player to check
     * @return {@code true} if the player has permission, otherwise {@code false}
     */
    boolean hasPurchasePermission(Player player);

    /**
     * Gets the unique key that identifies this treasure chest.
     *
     * @return the unique chest key
     */
    String getKey();

    /**
     * Gets the translated display name of this treasure chest.
     *
     * @param translator the translator used to localize the name
     * @return the localized display name
     */
    String getName(Translator translator);

    /**
     * Gets the MiniMessage tag resolvers used when formatting treasure messages.
     *
     * @param user the user for whom the placeholders are resolved
     * @return the {@link TagResolver} for MiniMessage formatting
     */
    TagResolver getResolvers(User user);

    /**
     * Checks whether this treasure chest is enabled.
     *
     * @return {@code true} if enabled, otherwise {@code false}
     */
    boolean isEnabled();

    /**
     * Checks whether this treasure chest can be purchased.
     *
     * @return {@code true} if purchasable, otherwise {@code false}
     */
    boolean isPurchasable();

    /**
     * Gets the cost of purchasing this treasure chest.
     *
     * @return the chest cost
     */
    int getCost();

    /**
     * Gets how many chests are opened simultaneously when this treasure is activated.
     *
     * @return the number of chests to open
     */
    int getChestsToOpen();

    /**
     * Gets the animation type used when opening this chest.
     *
     * @return the {@link AnimationType} defining the opening animation
     */
    AnimationType getChestAnimationType();

    /**
     * Gets the structures used for this treasure chest’s layout.
     *
     * @return a list of {@link StructureData} representing the structure
     */
    List<StructureData> getStructures();

    /**
     * Checks whether a broadcast message should be sent when a player opens this chest.
     *
     * @return {@code true} if an opening broadcast should be sent, otherwise {@code false}
     */
    boolean isOpeningBroadcast();

    /**
     * Gets the {@link ItemBuilder} representation of this treasure chest.
     *
     * @return the chest’s {@link ItemBuilder} representation
     */
    ItemBuilder getItemBuilder();

    /**
     * Gets the {@link ItemStack} representation of this treasure chest.
     *
     * @return the chest’s {@link ItemStack}
     */
    ItemStack getItemStack();
}
