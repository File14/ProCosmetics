package se.file14.procosmetics.worldguard;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;

import java.util.Set;

public class CosmeticFlagHandler extends Handler {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final StateFlag cosmeticsFlag;
    private final SetFlag<CosmeticCategory<?, ?, ?>> categoryFlag;

    protected CosmeticFlagHandler(Session session, StateFlag cosmeticsFlag, SetFlag<CosmeticCategory<?, ?, ?>> categoryFlag) {
        super(session);
        this.cosmeticsFlag = cosmeticsFlag;
        this.categoryFlag = categoryFlag;
    }

    // FROM com.sk89q.worldguard.session.handler.FlagValueChangeHandler
    @Override
    public boolean onCrossBoundary(LocalPlayer player,
                                   Location from,
                                   Location to,
                                   ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered,
                                   Set<ProtectedRegion> exited,
                                   MoveType moveType) {
        if (entered.isEmpty() && exited.isEmpty() && from.getExtent().equals(to.getExtent())) {
            return true;
        }
        Player bukkitPlayer = PLUGIN.getServer().getPlayer(player.getUniqueId());

        if (bukkitPlayer == null) {
            return true;
        }
        User user = PLUGIN.getUserManager().getConnected(bukkitPlayer);

        if (user == null) {
            return true;
        }
        // Test cosmetics flag
        State cosmeticsValue = toSet.queryState(player, cosmeticsFlag);

        if (cosmeticsValue == State.DENY) {
            boolean hadEquippedCosmetics = false;
            for (Cosmetic<?, ?> cosmetic : user.getCosmetics().values()) {
                if (cosmetic != null && cosmetic.isEquipped()) {
                    hadEquippedCosmetics = true;
                    break;
                }
            }

            if (hadEquippedCosmetics) {
                user.sendMessage(user.translate("cosmetic.unequip.region"));
            }
            user.unequipCosmetics(true, false);
            return true;
        }
        // Test cosmetic category flag
        Set<CosmeticCategory<?, ?, ?>> categoryValue = toSet.queryValue(player, categoryFlag);

        if (categoryValue != null) {
            boolean blocked = false;

            for (CosmeticCategory<?, ?, ?> category : categoryValue) {
                Cosmetic<?, ?> cosmetic = user.getCosmetic(category);

                if (cosmetic != null && cosmetic.isEquipped()) {
                    user.unequipCosmetic(category, true, false);
                    blocked = true;
                }
            }

            if (blocked) {
                user.sendMessage(user.translate("cosmetic.unequip.region"));
            }
        }
        return true;
    }
}