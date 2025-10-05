package se.file14.procosmetics.util.broadcaster;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.EnumUtil;

import java.util.function.Function;

public class Broadcaster {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final Mode mode;

    public Broadcaster(Config config, String path) {
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

        private final TriConsumer<Player, String, Function<User, TagResolver[]>> broadcaster;

        Mode(TriConsumer<Player, String, Function<User, TagResolver[]>> broadcaster) {
            this.broadcaster = broadcaster;
        }
    }

    public void broadcastMessage(Player player, String key, Function<User, TagResolver[]> resolverFunction) {
        mode.broadcaster.accept(player, key, resolverFunction);
    }

    public void broadcastMessage(Player player, String key, TagResolver... resolvers) {
        broadcastMessage(player, key, user -> resolvers);
    }

    @FunctionalInterface
    interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }
}