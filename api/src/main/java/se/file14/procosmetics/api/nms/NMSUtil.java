package se.file14.procosmetics.api.nms;

import io.netty.channel.Channel;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Provides version-specific NMS utility methods.
 */
public interface NMSUtil {

    /**
     * Gets the netty channel for the given player's connection.
     *
     * @param player the player
     * @return the channel, or null if unavailable
     */
    Channel getChannel(Player player);

    /**
     * Gets the ping of the given player in milliseconds.
     *
     * @param player the player
     * @return the ping in milliseconds
     */
    int getPing(Player player);

    /**
     * Plays a chest animation at the given block.
     *
     * @param block the chest block
     * @param open  true to open, false to close
     */
    void playChestAnimation(Block block, boolean open);
}