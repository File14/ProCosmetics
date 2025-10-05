package se.file14.procosmetics.util.broadcaster;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.rarity.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.rarity.CosmeticRarityRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class LootBroadcaster extends Broadcaster {

    private final Set<CosmeticRarity> excludes = new HashSet<>();

    public LootBroadcaster(Config config, String path) {
        super(config, path);

        for (String exclude : config.getStringList(path + ".excludes", true)) {
            CosmeticRarity rarity = CosmeticRarityRegistry.getBy(exclude);

            // Ignore unknown rarities
            if (rarity == null) {
                continue;
            }
            excludes.add(rarity);
        }
    }

    public void broadcastMessage(Player player, CosmeticRarity rarity, String key, Function<User, TagResolver[]> resolverFunction) {
        if (!excludes.contains(rarity)) {
            super.broadcastMessage(player, key, resolverFunction);
        }
    }
}
