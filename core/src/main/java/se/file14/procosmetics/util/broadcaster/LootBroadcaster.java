package se.file14.procosmetics.util.broadcaster;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.user.User;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class LootBroadcaster extends Broadcaster {

    private final Set<CosmeticRarity> excludes = new HashSet<>();

    public LootBroadcaster(ProCosmetics plugin, Config config, String path) {
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

    public void broadcastMessage(Player player, CosmeticRarity rarity, String key, Function<User, TagResolver[]> resolverFunction) {
        if (!excludes.contains(rarity)) {
            super.broadcastMessage(player, key, resolverFunction);
        }
    }
}
