package se.file14.procosmetics.treasure.loot;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;

public class MoneyLoot extends LootTable<MoneyLootEntry> {

    private final int min;
    private final int max;

    public MoneyLoot(String key, int weight, int min, int max) {
        super(key, weight);
        this.min = min;
        this.max = max;
    }

    @Override
    public MoneyLootEntry getRandomLoot() {
        int amount = MathUtil.randomRangeInt(min, max);
        return new MoneyLootEntry(amount);
    }

    @Override
    public void give(Player player, User user, MoneyLootEntry lootEntry) {
        PLUGIN.getEconomyManager().getEconomyProvider().addCoinsAsync(user, lootEntry.getAmount()).thenAcceptAsync(result -> {
            if (result.booleanValue()) {
                PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                        player,
                        lootEntry.getRarity(),
                        "treasure_chest.loot." + getKey() + ".broadcast",
                        receiverUser -> new TagResolver[]{
                                Placeholder.unparsed("player", player.getName()),
                                Placeholder.unparsed("cosmetic", lootEntry.getName(user)),
                                Placeholder.unparsed("amount", String.valueOf(lootEntry.getName(user))),
                                Placeholder.unparsed("rarity", lootEntry.getRarity().getName(user)),
                                Placeholder.unparsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                                Placeholder.unparsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                        });
            } else {
                user.sendMessage(user.translate("generic.error.database"));
            }
        }, PLUGIN.getSyncExecutor());
    }
}