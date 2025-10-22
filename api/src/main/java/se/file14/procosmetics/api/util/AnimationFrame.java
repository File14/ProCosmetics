package se.file14.procosmetics.api.util;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a single frame in an animated cosmetic sequence.
 * Contains an ItemStack to display and the duration of ticks to show it.
 */
public class AnimationFrame {

    private final ItemStack itemStack;
    private final int tickDuration;

    /**
     * Creates a new AnimationFrame with the specified ItemStack and duration.
     *
     * @param itemStack the ItemStack to display
     * @param duration  the duration in ticks
     */
    public AnimationFrame(ItemStack itemStack, int duration) {
        this.itemStack = itemStack;
        this.tickDuration = duration;
    }

    /**
     * Gets the ItemStack to display for this frame.
     *
     * @return the ItemStack to display
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Gets the duration in ticks that this frame should be displayed.
     *
     * @return the duration in ticks
     */
    public int getTickDuration() {
        return tickDuration;
    }

    /**
     * Creates a new AnimationFrame with the specified ItemStack and duration.
     *
     * @param itemStack the ItemStack to display
     * @param duration  the duration in ticks
     * @return a new AnimationFrame instance
     */
    public static AnimationFrame of(ItemStack itemStack, int duration) {
        return new AnimationFrame(itemStack, duration);
    }

    /**
     * Creates a new AnimationFrame with the specified ItemStack and a duration of 1 tick.
     *
     * @param itemStack the ItemStack to display
     * @return a new AnimationFrame instance with 1 tick duration
     */
    public static AnimationFrame of(ItemStack itemStack) {
        return new AnimationFrame(itemStack, 1);
    }
}
