package se.file14.procosmetics.api.cosmetic;

import org.bukkit.FireworkEffect;
import se.file14.procosmetics.api.locale.Translator;

public interface CosmeticRarity {

    String getKey();

    String getName(Translator translator);

    String getPrimaryColor();

    String getSecondaryColor();

    int getDetonations();

    int getTickInterval();

    FireworkEffect getFireworkEffect();
}