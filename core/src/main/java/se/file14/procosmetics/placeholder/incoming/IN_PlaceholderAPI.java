package se.file14.procosmetics.placeholder.incoming;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import se.file14.procosmetics.placeholder.AbstractPlaceholder;

import java.util.List;

public class IN_PlaceholderAPI extends AbstractPlaceholder {

    @Override
    public String setPlaceholders(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public List<String> setPlaceholders(Player player, List<String> text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}