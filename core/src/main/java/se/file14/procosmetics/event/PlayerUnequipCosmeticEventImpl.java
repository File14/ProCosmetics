package se.file14.procosmetics.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.event.PlayerUnequipCosmeticEvent;

import javax.annotation.Nonnull;

public class PlayerUnequipCosmeticEventImpl extends Event implements PlayerUnequipCosmeticEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ProCosmetics plugin;
    private final Player player;
    private final CosmeticType<?, ?> cosmeticType;

    public PlayerUnequipCosmeticEventImpl(ProCosmetics plugin, Player player, CosmeticType<?, ?> cosmeticType) {
        this.plugin = plugin;
        this.player = player;
        this.cosmeticType = cosmeticType;
    }

    @Override
    public ProCosmetics getPlugin() {
        return plugin;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public CosmeticType<?, ?> getCosmeticType() {
        return cosmeticType;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}