package se.file14.procosmetics.cosmetic.particleeffect.shape;

import org.bukkit.Color;

@FunctionalInterface
public interface ColorProvider {

    Color getColor(int value);
}