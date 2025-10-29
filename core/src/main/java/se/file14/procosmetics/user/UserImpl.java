/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.user;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.morph.Morph;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.status.Status;
import se.file14.procosmetics.api.locale.Translatable;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserImpl implements User {

    private final ProCosmetics plugin;
    private final int databaseId;
    private final UUID uuid;
    private final String name;
    private String locale = "en_us";
    private boolean moving;
    private final Location lastLocation = new Location(null, 0.0d, 0.0d, 0.0d, 0.0f, 0.0f);
    private int coins;
    private boolean selfViewMorph;
    private boolean selfViewStatus;

    private long fallDamageProtection;

    private final Map<CosmeticCategory<?, ?, ?>, Cosmetic<?, ?>> cosmetics = new HashMap<>();
    private final Map<TreasureChest, Integer> treasureChests = new HashMap<>();
    private TreasureChestPlatform currentPlatform;

    private final Map<GadgetType, Long> gadgetCooldowns = new HashMap<>();
    private final Map<GadgetType, Integer> ammo = new HashMap<>();

    public UserImpl(ProCosmetics plugin, int databaseId, UUID uuid, String name) {
        this.plugin = plugin;
        this.databaseId = databaseId;
        this.uuid = uuid;
        this.name = name;
    }

    private Audience getAudience() {
        return ((ProCosmeticsPlugin) plugin).adventure().player(uuid);
    }

    @Override
    public void showTitle(Component title, Component subTitle, Title.Times times) {
        if (!title.equals(Component.empty()) && !subTitle.equals(Component.empty())) {
            getAudience().showTitle(Title.title(title, subTitle, times));
        }
    }

    @Override
    public void sendActionBar(Component component) {
        if (!component.equals(Component.empty())) {
            getAudience().sendActionBar(component);
        }
    }

    @Override
    public void sendMessage(Component component) {
        if (!component.equals(Component.empty())) {
            getAudience().sendMessage(component);
        }
    }

    @Override
    public Component translate(String key, @Nullable Style style, TagResolver... resolvers) {
        return plugin.getLanguageManager().render(key, locale, style, resolvers);
    }

    @Override
    public Component translate(Translatable translatable) {
        return plugin.getLanguageManager().render(translatable, locale);
    }

    @Override
    public List<Component> translateList(String key, @Nullable Style style, TagResolver... resolvers) {
        return plugin.getLanguageManager().renderList(key, locale, style, resolvers);
    }

    @Override
    public List<Component> translateList(Translatable translatable) {
        return plugin.getLanguageManager().renderList(translatable, locale);
    }

    @Override
    public String translateRaw(String key) {
        return plugin.getLanguageManager().translate(key, locale);
    }

    @Override
    public List<String> translateRawList(String key) {
        return plugin.getLanguageManager().translateList(key, locale);
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public @Nullable Player getPlayer() {
        return plugin.getJavaPlugin().getServer().getPlayer(uuid);
    }

    @Override
    public int getDatabaseId() {
        return databaseId;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @Override
    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void setLastLocation(Location location) {
        lastLocation.setWorld(location.getWorld());
        lastLocation.setX(location.getX());
        lastLocation.setY(location.getY());
        lastLocation.setZ(location.getZ());
        lastLocation.setYaw(location.getYaw());
        lastLocation.setPitch(location.getPitch());
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public boolean hasSelfViewMorph() {
        return selfViewMorph;
    }

    public void setSelfViewMorph(boolean visible, boolean update) {
        if (hasSelfViewMorph() == visible) {
            return;
        }
        selfViewMorph = visible;

        if (update) {
            Morph morph = (Morph) getCosmetic(plugin.getCategoryRegistries().morphs());
            Player player = getPlayer();

            if (player != null && morph != null && morph.isEquipped()) {
                NMSEntity nmsEntity = morph.getNMSEntity();

                if (visible) {
                    nmsEntity.removeCollision(player);
                    nmsEntity.getTracker().removeAntiViewer(player);
                } else {
                    nmsEntity.getTracker().addAntiViewer(player);
                    nmsEntity.addCollision(player);
                }
            }
        }
    }

    @Override
    public boolean hasSelfViewStatus() {
        return selfViewStatus;
    }

    public void setSelfViewStatus(boolean visible, boolean update) {
        if (hasSelfViewStatus() == visible) {
            return;
        }
        selfViewStatus = visible;

        if (update) {
            Status status = (Status) getCosmetic(plugin.getCategoryRegistries().statuses());
            Player player = getPlayer();

            if (player != null && status != null && status.isEquipped()) {
                NMSEntity nmsEntity = status.getNMSEntity();

                if (visible) {
                    nmsEntity.getTracker().removeAntiViewer(player);
                } else {
                    nmsEntity.getTracker().addAntiViewer(player);
                }
            }
        }
    }

    @Override
    public boolean hasFallDamageProtection() {
        return fallDamageProtection >= System.currentTimeMillis();
    }

    @Override
    public void setFallDamageProtection(int seconds) {
        fallDamageProtection = Long.max(System.currentTimeMillis() + seconds * 1000L, fallDamageProtection);
    }

    @Override
    public boolean hasCosmetic(CosmeticCategory<?, ?, ?> cosmeticCategory) {
        return cosmetics.containsKey(cosmeticCategory);
    }

    @Override
    public @Nullable Cosmetic<?, ?> getCosmetic(CosmeticCategory<?, ?, ?> cosmeticCategory) {
        return cosmetics.getOrDefault(cosmeticCategory, null);
    }

    @Override
    public void setCosmetic(CosmeticCategory<?, ?, ?> cosmeticCategory, Cosmetic<?, ?> cosmetic) {
        cosmetics.put(cosmeticCategory, cosmetic);
    }

    @Override
    public Map<CosmeticCategory<?, ?, ?>, Cosmetic<?, ?>> getCosmetics() {
        return cosmetics;
    }

    @Override
    public void equipSavedCosmetics(boolean silent) {
        for (Cosmetic<?, ?> cosmeticCategory : cosmetics.values()) {
            if (!cosmeticCategory.isEquipped()) {
                cosmeticCategory.equip(silent, false);
            }
        }
    }

    @Override
    public void unequipCosmetic(CosmeticCategory<?, ?, ?> cosmeticCategory, boolean silent, boolean saveToDatabase) {
        Cosmetic<?, ?> cosmetic = getCosmetic(cosmeticCategory);

        if (cosmetic != null) {
            cosmetic.unequip(silent, saveToDatabase);
        }
    }

    @Override
    public void unequipCosmetics(boolean silent, boolean saveToDatabase) {
        for (CosmeticCategory<?, ?, ?> cosmeticCategory : cosmetics.keySet()) {
            unequipCosmetic(cosmeticCategory, silent, saveToDatabase);
        }
    }

    @Override
    public void removeCosmetic(CosmeticCategory<?, ?, ?> cosmeticCategory, boolean silent, boolean saveToDatabase) {
        unequipCosmetic(cosmeticCategory, silent, saveToDatabase);
        cosmetics.remove(cosmeticCategory);
    }

    @Override
    public void clearAllCosmetics(boolean silent, boolean saveToDatabase) {
        unequipCosmetics(silent, saveToDatabase);
        cosmetics.clear();
    }

    @Override
    public boolean isOpeningTreasure() {
        return currentPlatform != null;
    }

    @Override
    public TreasureChestPlatform getCurrentPlatform() {
        return currentPlatform;
    }

    @Override
    public void setCurrentPlatform(TreasureChestPlatform platform) {
        currentPlatform = platform;
    }

    @Override
    public boolean hasTreasureChests(TreasureChest treasureChest, int amount) {
        return getTreasureChests(treasureChest) >= amount;
    }

    @Override
    public int getTreasureChests(TreasureChest treasureChest) {
        return treasureChests.getOrDefault(treasureChest, 0);
    }

    @Override
    public void setTreasureChests(TreasureChest treasureChest, int amount) {
        treasureChests.put(treasureChest, amount);
    }

    @Override
    public Map<TreasureChest, Integer> getTreasureChests() {
        return treasureChests;
    }

    @Override
    public boolean hasCooldown(GadgetType gadget) {
        return getCooldown(gadget) > System.currentTimeMillis();
    }

    @Override
    public double getCooldown(GadgetType gadget) {
        return gadgetCooldowns.getOrDefault(gadget, 0L);
    }

    @Override
    public void setCooldown(GadgetType gadget, double time) {
        gadgetCooldowns.put(gadget, (long) (System.currentTimeMillis() + time * 1000.0d));
    }

    @Override
    public boolean hasAmmo(GadgetType gadget, int amount) {
        return getAmmo(gadget) >= amount;
    }

    @Override
    public int getAmmo(GadgetType gadget) {
        return ammo.getOrDefault(gadget, 0);
    }

    @Override
    public void setAmmo(GadgetType gadget, int amount) {
        ammo.put(gadget, amount);
    }

    @Override
    public Map<GadgetType, Integer> getAmmo() {
        return ammo;
    }

    @Override
    public String toString() {
        return getDebugName();
    }
}
