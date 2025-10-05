package se.file14.procosmetics.api.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;

import java.util.Map;
import java.util.UUID;

public interface User extends Translator {

    void showTitle(Component title, Component subTitle, Title.Times times);

    void sendActionBar(Component component);

    void sendMessage(Component component);

    int getDatabaseId();

    UUID getUniqueId();

    String getName();

    @Nullable
    Player getPlayer();

    boolean isMoving();

    @ApiStatus.Internal
    void setMoving(boolean moving);

    @ApiStatus.Internal
    Location getLastLocation();

    @ApiStatus.Internal
    void setLastLocation(Location location);

    int getCoins();

    @ApiStatus.Internal
    void setCoins(int coins);

    boolean hasSelfViewMorph();

    void setSelfViewMorph(boolean visible, boolean update);

    boolean hasSelfViewStatus();

    void setSelfViewStatus(boolean visible, boolean update);

    boolean hasFallDamageProtection();

    void setFallDamageProtection(int seconds);

    boolean hasCosmetic(CosmeticCategory<?, ?, ?> category);

    @Nullable
    Cosmetic<?, ?> getCosmetic(CosmeticCategory<?, ?, ?> category);

    @ApiStatus.Internal
    void setCosmetic(CosmeticCategory<?, ?, ?> cosmeticCategory, Cosmetic<?, ?> cosmetic);

    @ApiStatus.Internal
    Map<CosmeticCategory<?, ?, ?>, Cosmetic<?, ?>> getCosmetics();

    void equipSavedCosmetics(boolean silent);

    void unequipCosmetic(CosmeticCategory<?, ?, ?> category, boolean silent, boolean saveToDatabase);

    void unequipCosmetics(boolean silent, boolean saveToDatabase);

    void removeCosmetic(CosmeticCategory<?, ?, ?> category, boolean silent, boolean saveToDatabase);

    void clearAllCosmetics(boolean silent, boolean saveToDatabase);

    boolean isOpeningTreasure();

    @Nullable
    TreasureChestPlatform getCurrentPlatform();

    @ApiStatus.Internal
    void setCurrentPlatform(TreasureChestPlatform platform);

    boolean hasTreasureChests(TreasureChest treasureChest, int amount);

    int getTreasureChests(TreasureChest treasureChest);

    @ApiStatus.Internal
    void setTreasureChests(TreasureChest treasureChest, int amount);

    @ApiStatus.Internal
    Map<TreasureChest, Integer> getTreasureChests();

    boolean hasCooldown(GadgetType gadget);

    double getCooldown(GadgetType gadget);

    void setCooldown(GadgetType gadget, double time);

    boolean hasAmmo(GadgetType gadget, int amount);

    int getAmmo(GadgetType gadget);

    @ApiStatus.Internal
    void setAmmo(GadgetType gadget, int amount);

    @ApiStatus.Internal
    Map<GadgetType, Integer> getAmmo();

    default String toDisplayString() {
        return getName() + " (" + getDatabaseId() + ", " + getUniqueId().toString() + ")";
    }
}
