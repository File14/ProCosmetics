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
package se.file14.procosmetics.api;

import org.bukkit.plugin.java.JavaPlugin;
import se.file14.procosmetics.api.config.ConfigManager;
import se.file14.procosmetics.api.cosmetic.registry.CategoryRegistries;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRarityRegistry;
import se.file14.procosmetics.api.economy.EconomyManager;
import se.file14.procosmetics.api.locale.LanguageManager;
import se.file14.procosmetics.api.menu.MenuManager;
import se.file14.procosmetics.api.nms.NMSManager;
import se.file14.procosmetics.api.storage.Database;
import se.file14.procosmetics.api.treasure.TreasureChestManager;
import se.file14.procosmetics.api.user.UserManager;

import java.util.concurrent.Executor;

/**
 * Main API interface for ProCosmetics.
 * This interface provides access to all major subsystems including configuration,
 * localization, user management, database operations, cosmetic registries, and more.
 *
 * <p>Access this instance via {@link ProCosmeticsProvider#get()}.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 * ProCosmetics api = ProCosmeticsProvider.get();
 * UserManager userManager = api.getUserManager();
 * User user = userManager.getConnected(player);
 * }</pre>
 */
public interface ProCosmetics {

    /**
     * Gets the configuration manager for accessing plugin configs.
     *
     * @return The configuration manager
     */
    ConfigManager getConfigManager();

    /**
     * Gets the language manager for translations and localization.
     *
     * @return The language manager
     */
    LanguageManager getLanguageManager();

    /**
     * Gets the user manager for retrieving and managing users.
     *
     * @return The user manager
     */
    UserManager getUserManager();

    /**
     * Gets the menu manager for GUI operations.
     *
     * @return The menu manager
     */
    MenuManager getMenuManager();

    /**
     * Gets the database instance for data persistence operations.
     *
     * @return The database instance
     */
    Database getDatabase();

    /**
     * Gets the NMS manager for version-specific entity operations.
     *
     * @return The NMS manager
     */
    NMSManager getNMSManager();

    /**
     * Gets the cosmetic rarity registry.
     *
     * @return The cosmetic rarity registry
     */
    CosmeticRarityRegistry getCosmeticRarityRegistry();

    /**
     * Gets the category registries containing all cosmetic categories.
     *
     * @return The category registries
     */
    CategoryRegistries getCategoryRegistries();

    /**
     * Gets the treasure chest manager for loot operations.
     *
     * @return The treasure chest manager
     */
    TreasureChestManager getTreasureChestManager();

    /**
     * Gets the economy manager for handling currency operations.
     *
     * @return The economy manager
     */
    EconomyManager getEconomyManager();

    /**
     * Gets the underlying Bukkit JavaPlugin instance.
     *
     * @return The JavaPlugin instance
     */
    JavaPlugin getJavaPlugin();

    /**
     * Gets an executor that runs tasks on the main server thread.
     * Use this for operations that must be performed synchronously.
     *
     * @return The synchronous executor
     */
    Executor getSyncExecutor();

    /**
     * Gets an executor that runs tasks asynchronously off the main thread.
     * Use this for operations that can be performed asynchronously, such as
     * database operations or file I/O.
     *
     * @return The asynchronous executor
     */
    Executor getAsyncExecutor();
}
