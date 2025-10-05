package se.file14.procosmetics.treasure.loot;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.rarity.CosmeticRarityImpl;

import java.util.List;

public class CustomLoot extends LootTable<CustomLoot> implements LootEntry {

    private final String name;
    private final ItemStack itemStack;
    private final CosmeticRarityImpl rarity;
    private final List<String> commands;

    public CustomLoot(String key,
                      int weight,
                      String name,
                      ItemStack itemStack,
                      CosmeticRarityImpl rarity,
                      List<String> commands) {
        super(key, weight);
        this.name = name;
        this.itemStack = itemStack;
        this.rarity = rarity;
        this.commands = commands;
    }

    @Override
    public CustomLoot getRandomLoot() {
        return this;
    }

    @Override
    public void give(Player player, User user, CustomLoot lootEntry) {
        for (String command : commands) {
            PLUGIN.getServer().dispatchCommand(
                    PLUGIN.getServer().getConsoleSender(),
                    command.replace("<player>", player.getName())
                            .replace("<rarity>", lootEntry.getRarity().getName(user))
                            .replace("<rarity_primary_color>", lootEntry.getRarity().getPrimaryColor())
                            .replace("<rarity_secondary_color>", lootEntry.getRarity().getSecondaryColor())
            );
        }

        PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                player,
                lootEntry.getRarity(),
                "treasure_chest.reward." + getKey(),
                receiverUser -> new TagResolver[]{
                        Placeholder.unparsed("player", player.getName()),
                        Placeholder.unparsed("rarity", lootEntry.getRarity().getName(user)),
                        Placeholder.parsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                        Placeholder.parsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                });
    }

    @Override
    public String getName(Translator translator) {
        return name;
    }

    @Override
    public CosmeticRarityImpl getRarity() {
        return rarity;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
