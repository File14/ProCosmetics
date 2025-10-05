package se.file14.procosmetics.rarity;

import org.bukkit.FireworkEffect;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.rarity.CosmeticRarity;

public class CosmeticRarityImpl implements CosmeticRarity {

    private final String key;
    private final String mainColor;
    private final String secondaryColor;
    private final int detonations;
    private final int tickInterval;
    private final FireworkEffect fireworkEffect;

    public CosmeticRarityImpl(String key,
                              String mainColor,
                              String secondaryColor,
                              int detonations,
                              int tickInterval,
                              FireworkEffect fireworkEffect) {
        this.key = key.toLowerCase();
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.detonations = detonations;
        this.tickInterval = tickInterval;
        this.fireworkEffect = fireworkEffect;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName(Translator translator) {
        return translator.translateRaw("rarity." + key);
    }

    @Override
    public String getPrimaryColor() {
        return mainColor;
    }

    @Override
    public String getSecondaryColor() {
        return secondaryColor;
    }

    @Override
    public int getDetonations() {
        return detonations;
    }

    @Override
    public int getTickInterval() {
        return tickInterval;
    }

    @Override
    public FireworkEffect getFireworkEffect() {
        return fireworkEffect;
    }
}