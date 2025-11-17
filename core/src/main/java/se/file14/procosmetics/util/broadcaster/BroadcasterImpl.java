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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.broadcaster.Broadcaster;
import se.file14.procosmetics.util.EnumUtil;

import java.util.function.Function;

public class BroadcasterImpl implements Broadcaster {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final Mode mode;

    public BroadcasterImpl(Config config, String path) {
        this.mode = EnumUtil.getType(Mode.class, config.getString(path + ".mode"));
    }

    enum Mode {
        ALL((player, key, resolverFunction) -> {
            for (User user : PLUGIN.getUserManager().getAllConnected()) {
                user.sendMessage(user.translate(key, resolverFunction.apply(user)));
            }
        }),
        WORLD((player, key, resolverFunction) -> {
            for (Player onlinePlayer : player.getWorld().getPlayers()) {
                User user = PLUGIN.getUserManager().getConnected(onlinePlayer);

                if (user == null) {
                    continue;
                }
                user.sendMessage(user.translate(key, resolverFunction.apply(user)));
            }
        }),
        NEARBY((player, key, resolverFunction) -> {
            Location location = player.getLocation();
            Location playerLocation = location.clone();
            double rangeSquared = Math.pow(32, 2);

            for (Player onlinePlayer : player.getWorld().getPlayers()) {
                if (onlinePlayer.getLocation(playerLocation).distanceSquared(location) < rangeSquared) {
                    User user = PLUGIN.getUserManager().getConnected(onlinePlayer);

                    if (user == null) {
                        continue;
                    }
                    user.sendMessage(user.translate(key, resolverFunction.apply(user)));
                }
            }
        }),
        SELF((player, key, resolverFunction) -> {
            User user = PLUGIN.getUserManager().getConnected(player);

            if (user == null) {
                return;
            }
            user.sendMessage(user.translate(key, resolverFunction.apply(user)));
        });

        private final TriConsumer<Player, String, Function<User, TagResolver>> broadcaster;

        Mode(TriConsumer<Player, String, Function<User, TagResolver>> broadcaster) {
            this.broadcaster = broadcaster;
        }
    }

    @Override
    public void broadcastMessage(Player player, String key, Function<User, TagResolver> resolverFunction) {
        mode.broadcaster.accept(player, key, resolverFunction);
    }

    @Override
    public void broadcastMessage(Player player, String key, TagResolver... resolvers) {
        broadcastMessage(player, key, user -> TagResolver.resolver(resolvers));
    }

    @FunctionalInterface
    interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }
}
