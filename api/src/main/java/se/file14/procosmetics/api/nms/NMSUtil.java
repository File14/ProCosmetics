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
