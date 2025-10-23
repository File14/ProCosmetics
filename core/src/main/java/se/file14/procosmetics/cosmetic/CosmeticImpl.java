// se/file14/procosmetics/cosmetic/CosmeticImpl.java
package se.file14.procosmetics.cosmetic;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.config.ConfigManagerImpl;
import se.file14.procosmetics.event.PlayerEquipCosmeticEventImpl;
import se.file14.procosmetics.event.PlayerPreEquipCosmeticEventImpl;
import se.file14.procosmetics.event.PlayerUnequipCosmeticEventImpl;
import se.file14.procosmetics.util.AbstractRunnable;

public abstract class CosmeticImpl<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>> extends AbstractRunnable implements Cosmetic<T, B>, Listener, CosmeticContext<T> {

    protected final ProCosmeticsPlugin plugin;
    protected final User user;
    protected final T cosmeticType;
    protected final B behavior;
    protected Player player;
    private boolean equipped;

    public CosmeticImpl(ProCosmeticsPlugin plugin, User user, T cosmeticType, B behavior) {
        this.plugin = plugin;
        this.user = user;
        this.cosmeticType = cosmeticType;
        this.behavior = behavior;
    }

    @Override
    public void equip(boolean silent, boolean saveToDatabase) {
        player = plugin.getServer().getPlayer(user.getUniqueId());

        if (player == null || !player.isOnline()) {
            return;
        }
        Server server = plugin.getServer();
        PluginManager pluginManager = server.getPluginManager();
        PlayerPreEquipCosmeticEventImpl event = new PlayerPreEquipCosmeticEventImpl(plugin, user, player, cosmeticType);
        pluginManager.callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        player.closeInventory();

        if (!cosmeticType.hasPermission(player)) {
            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1.0f, 0.6f);
            unequip(false, true);
            return;
        }
        ConfigManagerImpl configManager = plugin.getConfigManager();

        Config config = configManager.getMainConfig();
        boolean blacklistedWorlds = config.getBoolean("multi_world.blacklisted_worlds");
        boolean inWorld = config.getStringList("multi_world.worlds").contains(player.getWorld().getName());

        if (blacklistedWorlds && inWorld || !blacklistedWorlds && !inWorld) {
            if (!silent) {
                user.sendMessage(user.translate("cosmetic.equip.deny.world"));
            }
            return;
        }
        CosmeticCategory<T, B, ?> category = cosmeticType.getCategory();

        if (plugin.getWorldGuardManager() != null && !plugin.getWorldGuardManager().isCosmeticAllow(player, category)) {
            user.sendMessage(user.translate("cosmetic.equip.deny.region"));
            return;
        }
        if (!canEquip()) {
            return;
        }
        pluginManager.registerEvents(this, plugin);

        if (user.hasCosmetic(category) && user.getCosmetic(category).isEquipped()) {
            user.removeCosmetic(category, true, false);
        }
        user.setCosmetic(category, this);

        onEquip();
        behavior.onEquip(this);

        if (behavior instanceof Listener behaviorListener) {
            pluginManager.registerEvents(behaviorListener, plugin);
        }

        if (!silent) {
            server.getScheduler().runTask(plugin, () -> user.sendMessage(user.translate(
                    "cosmetic." + cosmeticType.getCategory().getKey() + ".equip",
                    Placeholder.unparsed("cosmetic", cosmeticType.getName(user))
            )));
        }
        equipped = true;
        pluginManager.callEvent(new PlayerEquipCosmeticEventImpl(plugin, user, player, cosmeticType));

        if (saveToDatabase) {
            plugin.getDatabase().saveEquippedCosmeticAsync(user, cosmeticType);
        }
    }

    @Override
    public void run() {
        if (player == null || !player.isOnline() || !equipped) {
            cancel();
            return;
        }
        onUpdate();
    }

    @Override
    public void unequip(boolean silent, boolean saveToDatabase) {
        if (!equipped) {
            return;
        }
        cancel();
        HandlerList.unregisterAll(this);
        behavior.onUnequip(this);

        if (behavior instanceof Listener behaviorListener) {
            HandlerList.unregisterAll(behaviorListener);
        }
        onUnequip();

        if (!silent) {
            user.sendMessage(user.translate(
                    "cosmetic." + cosmeticType.getCategory().getKey() + ".unequip",
                    Placeholder.unparsed("cosmetic", cosmeticType.getName(user))
            ));
        }
        plugin.getServer().getPluginManager().callEvent(new PlayerUnequipCosmeticEventImpl(plugin, player, cosmeticType));
        equipped = false;

        if (saveToDatabase) {
            plugin.getDatabase().removeEquippedCosmeticAsync(user, cosmeticType.getCategory());
        }
    }

    protected boolean canEquip() {
        return true;
    }

    protected abstract void onEquip();

    protected abstract void onUpdate();

    protected abstract void onUnequip();

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public T getType() {
        return cosmeticType;
    }

    @Override
    public B getBehavior() {
        return behavior;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isEquipped() {
        return equipped;
    }

    @Override
    public ProCosmetics getPlugin() {
        return plugin;
    }
}