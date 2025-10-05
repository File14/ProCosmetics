package se.file14.procosmetics.placeholder;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractPlaceholder {

    public abstract String setPlaceholders(Player player, String text);

    public abstract List<String> setPlaceholders(Player player, List<String> text);
}