package se.file14.procosmetics.cosmetic.registry;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRarityRegistry;
import se.file14.procosmetics.config.YmlConfig;
import se.file14.procosmetics.cosmetic.CosmeticRarityImpl;
import se.file14.procosmetics.util.EnumUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CosmeticRarityRegistryImpl implements CosmeticRarityRegistry {

    private final Map<String, CosmeticRarity> rarities = new HashMap<>();
    private final ProCosmeticsPlugin plugin;
    private CosmeticRarity fallbackRarity;

    public CosmeticRarityRegistryImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    @Override
    public void load() {
        rarities.clear();
        fallbackRarity = null;

        Config config = new YmlConfig(plugin, "rarities");

        for (String key : config.getConfigurationSection("rarities").getKeys(false)) {
            String path = "rarities." + key + ".";
            String mainColor = config.getString(path + "colors.primary");
            String secondaryColor = config.getString(path + "colors.secondary");
            int detonations = config.getInt(path + "firework.detonations");
            int tickInterval = config.getInt(path + "firework.tick_interval");

            FireworkEffect fireworkEffect = FireworkEffect.builder()
                    .with(EnumUtil.getType(FireworkEffect.Type.class, config.getString(path + "firework.type")))
                    .withColor(Color.fromRGB(config.getInt(path + "firework.color")))
                    .withFade(Color.fromRGB(config.getInt(path + "firework.fade")))
                    .flicker(config.getBoolean(path + "firework.flicker"))
                    .trail(config.getBoolean(path + "firework.trail"))
                    .build();

            CosmeticRarityImpl rarity = new CosmeticRarityImpl(
                    key.toUpperCase(),
                    mainColor,
                    secondaryColor,
                    detonations,
                    tickInterval,
                    fireworkEffect
            );
            register(rarity);

            if (fallbackRarity == null) {
                fallbackRarity = rarity;
            }
        }

        if (fallbackRarity == null) {
            fallbackRarity = new CosmeticRarityImpl("default", "", "", 0, 0, null);
        }
    }

    @Override
    public void register(CosmeticRarity rarity) {
        rarities.put(rarity.getKey().toLowerCase(), rarity);
    }

    @Override
    @Nullable
    public CosmeticRarity get(String key) {
        return rarities.get(key.toLowerCase());
    }

    @Override
    public CosmeticRarity getSafely(String key) {
        return rarities.getOrDefault(key.toLowerCase(), fallbackRarity);
    }

    @Override
    public CosmeticRarity getFallbackRarity() {
        return fallbackRarity;
    }
}