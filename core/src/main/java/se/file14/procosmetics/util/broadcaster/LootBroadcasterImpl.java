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
package se.file14.procosmetics.util.broadcaster;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.broadcaster.LootBroadcaster;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class LootBroadcasterImpl extends BroadcasterImpl implements LootBroadcaster {

    private final Set<CosmeticRarity> excludes = new HashSet<>();

    public LootBroadcasterImpl(ProCosmetics plugin, Config config, String path) {
        super(config, path);

        for (String key : config.getStringList(path + ".excludes", true)) {
            CosmeticRarity rarity = plugin.getCosmeticRarityRegistry().get(key);

            // Ignore unknown rarities
            if (rarity == null) {
                continue;
            }
            excludes.add(rarity);
        }
    }

    @Override
    public void broadcastMessage(Player player, CosmeticRarity rarity, String key, Function<User, TagResolver> resolverFunction) {
        if (!excludes.contains(rarity)) {
            super.broadcastMessage(player, key, resolverFunction);
        }
    }
}
