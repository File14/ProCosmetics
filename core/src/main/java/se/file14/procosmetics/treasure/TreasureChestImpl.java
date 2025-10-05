package se.file14.procosmetics.treasure;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.animation.AnimationType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.rarity.CosmeticRarityImpl;
import se.file14.procosmetics.rarity.CosmeticRarityRegistry;
import se.file14.procosmetics.treasure.loot.*;
import se.file14.procosmetics.util.EnumUtil;
import se.file14.procosmetics.util.item.ItemBuilderImpl;
import se.file14.procosmetics.util.structure.StructureDataImpl;
import se.file14.procosmetics.util.structure.StructureReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class TreasureChestImpl implements TreasureChest {

    private static final Random RANDOM = new Random();

    private final String key;
    private final boolean enabled;
    private final boolean purchasable;
    private final String purchasePermission;
    private final int cost;
    private final int chestsToOpen;
    private final AnimationType chestAnimationType;
    private final List<StructureData> structures = new ArrayList<>();
    private final boolean openingBroadcast;
    private final ItemBuilder itemBuilder;

    private int totalWeight;
    private final List<LootTable<?>> lootEntries = new ArrayList<>();

    public TreasureChestImpl(ProCosmeticsPlugin plugin, String key) {
        this.key = key;

        Config config = plugin.getConfigManager().getConfig("treasure_chests");

        String path = "treasure_chests." + key;
        enabled = config.getBoolean(path + ".enable");
        purchasable = config.getBoolean(path + ".purchasable");
        purchasePermission = "procosmetics.purchase.treasure_chest." + key;
        cost = config.getInt(path + ".cost");
        chestsToOpen = config.getInt(path + ".chests_to_open");
        chestAnimationType = EnumUtil.getType(AnimationType.class, config.getString(path + ".chest_animation"));

        for (String layout : config.getStringList(path + ".animation_layouts")) {
            StructureDataImpl structureData = StructureReader.loadStructure(layout);

            if (structureData == null) {
                plugin.getLogger().log(Level.WARNING, "Failed to load animation layout " + layout + ". Skipping");
                continue;
            }
            structures.add(structureData);
        }
        openingBroadcast = config.getBoolean(path + ".opening_broadcast");
        itemBuilder = new ItemBuilderImpl(config, path);

        // GADGET AMMO
        List<GadgetType> ammo = new ArrayList<>();

        for (GadgetType ammoType : plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getEnabledTypes()) {
            if (ammoType.isFindable()) {
                ammo.add(ammoType);
            }
        }
        String ammoPath = path + ".rewards.ammo.";
        int weight = config.getInt(ammoPath + "weight");
        if (weight > 0) {
            totalWeight += weight;
            lootEntries.add(new AmmoLoot("ammo",
                    weight,
                    config.getInt(ammoPath + "minimum_amount"),
                    config.getInt(ammoPath + "maximum_amount"),
                    ammo)
            );
        }

        // MONEY
        String moneyPath = path + ".rewards.money.";
        weight = config.getInt(moneyPath + "weight");
        if (weight > 0) {
            totalWeight += weight;
            lootEntries.add(new MoneyLoot("money",
                    weight,
                    config.getInt(moneyPath + "minimum_amount"),
                    config.getInt(moneyPath + "maximum_amount"))
            );
        }

        // COSMETICS
        for (CosmeticCategory<?, ?, ?> cosmeticCategory : plugin.getCategoryRegistries().getCategories()) {
            String path2 = "treasure_chests." + key + ".rewards." + cosmeticCategory.getKey() + ".";
            weight = config.getInt(path2 + "weight");

            if (weight > 0) {
                List<CosmeticType<?, ?>> cosmeticTypes = new ArrayList<>();

                for (CosmeticType<?, ?> cosmeticType : cosmeticCategory.getCosmeticRegistry().getEnabledTypes()) {
                    if (cosmeticType.isFindable()) {
                        cosmeticTypes.add(cosmeticType);
                    }
                }

                if (!cosmeticTypes.isEmpty()) {
                    totalWeight += weight;
                    lootEntries.add(new CosmeticLoot(cosmeticCategory.getKey(), weight, cosmeticTypes));
                }
            }
        }
        // CUSTOM LOOT
        String customRewardsPath = path + ".rewards.custom";
        if (config.getConfigurationSection(customRewardsPath) != null) {
            for (String key2 : config.getConfigurationSection(customRewardsPath).getKeys(false)) {
                String path1 = customRewardsPath + "." + key2 + ".";
                CosmeticRarityImpl rarity = CosmeticRarityRegistry.getSafelyBy(config.getString(path1 + "rarity"));
                weight = config.getInt(path1 + "weight");

                if (weight > 0) {
                    totalWeight += weight;

                    ItemBuilderImpl customItemBuilder = new ItemBuilderImpl(config, path1);

                    lootEntries.add(new CustomLoot(
                            key2,
                            weight,
                            config.getString(path1 + "name"),
                            customItemBuilder.getItemStack(),
                            rarity,
                            config.getStringList(path1 + "commands")
                    ));
                }
            }
        }
        List<String> chances = new ArrayList<>();

        for (LootTable<?> lootTable : lootEntries) {
            if (lootTable.getWeight() == 0) {
                continue;
            }
            double percentage = (double) lootTable.getWeight() / totalWeight * 100;
            //chances.add(ChatColor.GRAY + String.format("%s: %.2f%%", lootTable.getMenuCategory(), percentage));
        }
    }

    @Nullable
    public LootTable<?> getRandomLootTable() {
        int randomValue = RANDOM.nextInt(totalWeight);
        int currentWeight = 0;

        for (LootTable<?> item : lootEntries) {
            currentWeight += item.getWeight();

            if (randomValue < currentWeight) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean hasPurchasePermission(Player player) {
        return player.hasPermission(purchasePermission);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName(Translator translator) {
        return translator.translateRaw("treasure_chest." + key);
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.resolver(
                Placeholder.unparsed("current", String.valueOf(user.getTreasureChests(this))),
                Placeholder.unparsed("cost", String.valueOf(cost)),
                Placeholder.unparsed("amount", String.valueOf(1))
        );
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isPurchasable() {
        return purchasable;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getChestsToOpen() {
        return chestsToOpen;
    }

    @Override
    public AnimationType getChestAnimationType() {
        return chestAnimationType;
    }

    @Override
    public List<StructureData> getStructures() {
        return structures;
    }

    @Override
    public boolean isOpeningBroadcast() {
        return openingBroadcast;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public ItemStack getItemStack() {
        return itemBuilder.getItemStack();
    }
}