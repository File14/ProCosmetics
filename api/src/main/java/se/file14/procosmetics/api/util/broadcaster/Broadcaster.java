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
package se.file14.procosmetics.api.util.broadcaster;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.user.User;

import java.util.function.Function;

/**
 * Interface for broadcasting messages to players based on different modes.
 */
public interface Broadcaster {

    /**
     * Broadcasts a message to players based on the configured mode.
     *
     * @param player           the player who triggers the broadcast
     * @param key              the translation key for the message
     * @param resolverFunction function to generate TagResolver for each user
     */
    void broadcastMessage(Player player, String key, Function<User, TagResolver> resolverFunction);

    /**
     * Broadcasts a message to players based on the configured mode.
     *
     * @param player    the player who triggers the broadcast
     * @param key       the translation key for the message
     * @param resolvers tag resolvers to apply to the message
     */
    void broadcastMessage(Player player, String key, TagResolver... resolvers);
}
