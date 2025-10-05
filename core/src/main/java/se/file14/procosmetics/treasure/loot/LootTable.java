package se.file14.procosmetics.treasure.loot;

import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;

import java.util.Random;

public abstract class LootTable<T extends LootEntry> {

    protected static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    protected static final Random RANDOM = new Random();

    protected final String key;
    protected final int weight;

    public LootTable(String key, int weight) {
        this.key = key;
        this.weight = weight;
    }

    public abstract T getRandomLoot();

    public abstract void give(Player player, User user, T lootEntry);

    public String getCategory(User user) {
        return user.translateRaw("treasure_chest.category." + key);
    }

    public String getKey() {
        return key;
    }

    public int getWeight() {
        return weight;
    }
}
