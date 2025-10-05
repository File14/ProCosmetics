package se.file14.procosmetics.rarity;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.config.YmlConfig;
import se.file14.procosmetics.util.EnumUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CosmeticRarityRegistry {

    private static final Map<String, CosmeticRarityImpl> RARITIES = new HashMap<>();
    public static CosmeticRarityImpl FALL_BACK_RARITY;

    public static void load() {
        Config config = new YmlConfig(ProCosmeticsPlugin.getPlugin(), "rarities");

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

            CosmeticRarityImpl rarity = new CosmeticRarityImpl(key.toUpperCase(), mainColor, secondaryColor, detonations, tickInterval, fireworkEffect);
            register(rarity);

            if (FALL_BACK_RARITY == null) {
                FALL_BACK_RARITY = rarity;
            }
        }

        if (FALL_BACK_RARITY == null) {
            FALL_BACK_RARITY = new CosmeticRarityImpl("default", "", "", 0, 0, null);
        }
    }

    public static void register(CosmeticRarityImpl rarity) {
        RARITIES.put(rarity.getKey().toLowerCase(), rarity);
    }

    @Nullable
    public static CosmeticRarityImpl getBy(String key) {
        return RARITIES.get(key.toLowerCase());
    }


    public static CosmeticRarityImpl getSafelyBy(String key) {
        return RARITIES.getOrDefault(key.toLowerCase(), FALL_BACK_RARITY);
    }
}