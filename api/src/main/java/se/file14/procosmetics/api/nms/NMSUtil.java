package se.file14.procosmetics.api.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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
     * Gets the GameProfile for the given player.
     *
     * @param player the player
     * @return the player's GameProfile
     */
    GameProfile getProfile(Player player);

    /**
     * Gets the value from a property.
     *
     * @param property the property
     * @return the property value
     */
    String getPropertiesValue(Property property);

    /**
     * Plays a chest animation at the given block.
     *
     * @param block the chest block
     * @param open  true to open, false to close
     */
    void playChestAnimation(Block block, boolean open);
}