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
package se.file14.procosmetics.treasure.loot.type;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.treasure.loot.CustomLoot;
import se.file14.procosmetics.api.treasure.loot.GeneratedLoot;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.number.IntProvider;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.treasure.loot.GeneratedLootImpl;
import se.file14.procosmetics.treasure.loot.LootEntryImpl;

import java.util.List;

public class CustomLootImpl extends LootEntryImpl<IntProvider> implements CustomLoot {

    private final String key;
    private final ItemStack itemStack;
    private final CosmeticRarity rarity;
    private final List<String> commands;

    public CustomLootImpl(IntProvider intProvider,
                          LootCategory category,
                          String key,
                          ItemStack itemStack,
                          CosmeticRarity rarity,
                          List<String> commands) {
        super(intProvider, category);
        this.key = key;
        this.itemStack = itemStack;
        this.rarity = rarity;
        this.commands = commands;
    }

    @Override
    public GeneratedLoot generate() {
        return new GeneratedCustomLoot(this, intProvider.get());
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getNameTranslationKey() {
        return "treasure_chest.loot." + category.getKey() + "." + key;
    }

    @Override
    public CosmeticRarity getRarity() {
        return rarity;
    }

    @Override
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    private static class GeneratedCustomLoot extends GeneratedLootImpl<CustomLoot> {

        public GeneratedCustomLoot(CustomLoot entry, int amount) {
            super(entry, amount);
        }

        @Override
        public void give(Player player, User user) {
            for (String command : entry.getCommands()) {
                PLUGIN.getServer().dispatchCommand(
                        PLUGIN.getServer().getConsoleSender(),
                        command.replace("<player>", player.getName())
                                .replace("<player_name>", player.getName())
                                .replace("<player_uuid>", player.getUniqueId().toString())
                                .replace("<rarity>", getRarity().getName(user))
                );
            }

            PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                    player,
                    getRarity(),
                    "treasure_chest.loot.custom.broadcast",
                    receiverUser -> TagResolver.resolver(
                            Placeholder.unparsed("player", player.getName()),
                            getResolvers(receiverUser)
                    ));
        }
    }
}
