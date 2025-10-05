package se.file14.procosmetics.treasure.loot;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.user.User;

import java.util.List;

public class CosmeticLoot extends LootTable<CosmeticType<?, ?>> {

    private final List<CosmeticType<?, ?>> cosmeticTypes;

    public CosmeticLoot(String category, int weight, List<CosmeticType<?, ?>> cosmeticTypes) {
        super(category, weight);
        this.cosmeticTypes = cosmeticTypes;
    }

    @Override
    public CosmeticType<?, ?> getRandomLoot() {
        return cosmeticTypes.get(RANDOM.nextInt(cosmeticTypes.size()));
    }

    @Override
    public void give(Player player, User user, CosmeticType<?, ?> lootEntry) {
        givePermission(player, lootEntry.getPermission());

        PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                player,
                lootEntry.getRarity(),
                "treasure_chest.reward." + getKey(),
                receiverUser -> new TagResolver[]{
                        Placeholder.unparsed("player", player.getName()),
                        Placeholder.unparsed("cosmetic", lootEntry.getName(user)),
                        Placeholder.unparsed("category", getCategory(user)),
                        Placeholder.unparsed("rarity", lootEntry.getRarity().getName(user)),
                        Placeholder.parsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                        Placeholder.parsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                });
    }

    private void givePermission(Player player, String permission) {
        PLUGIN.getServer().dispatchCommand(
                PLUGIN.getServer().getConsoleSender(),
                PLUGIN.getConfigManager().getMainConfig().getString("settings.permission_add_command")
                        .replace("<player>", player.getName())
                        .replace("<permission>", permission)
        );
    }
}