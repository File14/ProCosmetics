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
package se.file14.procosmetics.worldguard;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MetadataUtil;

import java.util.Set;

public class WorldGuardManager {

    private static boolean FACTORY_REGISTERED;

    private static final String COSMETIC_FLAG_NAME = "cosmetics";
    private static final StateFlag COSMETIC_FLAG = new StateFlag(COSMETIC_FLAG_NAME, true);

    private static final String CATEGORY_FLAG_NAME = "blocked-cosmetic-categories";
    private static SetFlag<CosmeticCategory<?, ?, ?>> CATEGORY_FLAG;

    private final WorldGuard worldGuard;
    private final WorldGuardPlugin worldGuardPlugin;
    private BukkitTask task;

    public WorldGuardManager(ProCosmetics plugin) {
        this.worldGuard = WorldGuard.getInstance();
        this.worldGuardPlugin = WorldGuardPlugin.inst();
        FlagRegistry registry = worldGuard.getFlagRegistry();

        if (CATEGORY_FLAG == null) {
            CATEGORY_FLAG = new SetFlag<>(CATEGORY_FLAG_NAME, new CosmeticCategoryFlag("cosmetic-category", plugin));
        }

        if (registry.get(COSMETIC_FLAG_NAME) == null) {
            registry.register(COSMETIC_FLAG);
        }
        if (registry.get(CATEGORY_FLAG_NAME) == null) {
            registry.register(CATEGORY_FLAG);
        }
    }

    public void register(ProCosmetics plugin) {
        if (!FACTORY_REGISTERED) {
            worldGuard.getPlatform().getSessionManager().registerHandler(FACTORY, null);
            FACTORY_REGISTERED = true;
        }
        startVehicleRestrictionTask(plugin);
    }

    private void startVehicleRestrictionTask(ProCosmetics plugin) {
        if (task != null) {
            return;
        }
        // Ideally, WorldGuard should handle this check instead of relying solely on PlayerMoveEvent.
        // Otherwise, players could bypass region restrictions by riding entities (non-horse),
        // pushing them into protected areas, letting pathfinders wander inside,
        // or using plugins that apply velocity to move them across region boundaries.
        task = plugin.getJavaPlugin().getServer().getScheduler().runTaskTimer(plugin.getJavaPlugin(), () -> {
            for (Player player : plugin.getJavaPlugin().getServer().getOnlinePlayers()) {
                Entity vehicle = player.getVehicle();

                if (vehicle == null || !MetadataUtil.isCustomEntity(vehicle)) {
                    continue;
                }
                User user = plugin.getUserManager().getConnected(player);

                if (user == null || !user.isMoving()) {
                    continue;
                }

                if (queryFlag(player, Flags.ENTRY) == StateFlag.State.DENY
                        || !isCosmeticAllow(player, plugin.getCategoryRegistries().mounts())) {
                    vehicle.eject();
                }
            }
        }, 1L, 1L);
    }

    public boolean isCosmeticAllow(Player player, CosmeticCategory<?, ?, ?> cosmeticCategory) {
        if (!flagCheck(player)) {
            return false;
        }
        return categoryFlagCheck(player, cosmeticCategory);
    }

    private boolean flagCheck(Player player) {
        return queryFlag(player, COSMETIC_FLAG) == StateFlag.State.ALLOW;
    }

    private boolean categoryFlagCheck(Player player, CosmeticCategory<?, ?, ?> cosmeticCategory) {
        Set<CosmeticCategory<?, ?, ?>> value = queryFlag(player, CATEGORY_FLAG);

        return value == null || !value.contains(cosmeticCategory);
    }

    private <T> T queryFlag(Player player, Flag<T> flag) {
        LocalPlayer localPlayer = worldGuardPlugin.wrapPlayer(player);
        RegionQuery query = worldGuard.getPlatform().getRegionContainer().createQuery();
        return query.queryValue(localPlayer.getLocation(), localPlayer, flag);
    }

    // https://worldguard.enginehub.org/en/latest/developer/regions/custom-flags
    private static final Factory FACTORY = new Factory();

    private static final class Factory extends Handler.Factory<CosmeticFlagHandler> {

        @Override
        public CosmeticFlagHandler create(Session session) {
            return new CosmeticFlagHandler(session, COSMETIC_FLAG, CATEGORY_FLAG);
        }
    }
}
