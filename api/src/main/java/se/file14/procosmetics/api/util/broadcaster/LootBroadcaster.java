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
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.user.User;

import java.util.function.Function;

/**
 * Specialized broadcaster for announcing cosmetic loot obtained from treasure chests.
 *
 * @see Broadcaster
 * @see CosmeticRarity
 */
public interface LootBroadcaster extends Broadcaster {

    /**
     * Broadcasts a loot message with rarity-specific configuration.
     *
     * @param player           the player who obtained the loot
     * @param rarity           the rarity of the cosmetic obtained
     * @param key              the translation key for the loot message
     * @param resolverFunction function to generate TagResolver for each user receiving the message
     */
    void broadcastMessage(Player player, CosmeticRarity rarity, String key, Function<User, TagResolver> resolverFunction);
}
