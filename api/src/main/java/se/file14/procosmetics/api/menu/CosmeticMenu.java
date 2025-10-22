package se.file14.procosmetics.api.menu;

import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a specialized {@link Menu} for displaying and interacting with cosmetics.
 *
 * @param <T> the type of cosmetic displayed in this menu
 * @see Menu
 * @see CosmeticType
 */
public interface CosmeticMenu<T extends CosmeticType<T, ?>> extends Menu {
}
