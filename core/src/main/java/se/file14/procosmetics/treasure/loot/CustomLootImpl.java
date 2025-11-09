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
package se.file14.procosmetics.treasure.loot;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.treasure.loot.CustomLoot;
import se.file14.procosmetics.api.user.User;

import java.util.List;

public class CustomLootImpl extends LootTableImpl<CustomLoot> implements CustomLoot {

    private final ItemStack itemStack;
    private final CosmeticRarity rarity;
    private final List<String> commands;

    public CustomLootImpl(String key,
                          int weight,
                          ItemStack itemStack,
                          CosmeticRarity rarity,
                          List<String> commands) {
        super(key, weight);
        this.itemStack = itemStack;
        this.rarity = rarity;
        this.commands = commands;
    }

    @Override
    public CustomLootImpl getRandomLoot() {
        return this;
    }

    @Override
    public void give(Player player, User user, CustomLoot lootEntry) {
        for (String command : commands) {
            PLUGIN.getServer().dispatchCommand(
                    PLUGIN.getServer().getConsoleSender(),
                    command.replace("<player_name>", player.getName())
                            .replace("<player_uuid>", player.getUniqueId().toString())
                            .replace("<rarity>", lootEntry.getRarity().getName(user))
                            .replace("<rarity_primary_color>", lootEntry.getRarity().getPrimaryColor())
                            .replace("<rarity_secondary_color>", lootEntry.getRarity().getSecondaryColor())
            );
        }

        PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                player,
                lootEntry.getRarity(),
                "treasure_chest.loot.custom.broadcast",
                receiverUser -> new TagResolver[]{
                        Placeholder.unparsed("loot", lootEntry.getName(user)),
                        Placeholder.unparsed("player", player.getName()),
                        Placeholder.unparsed("rarity", lootEntry.getRarity().getName(user)),
                        Placeholder.parsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                        Placeholder.parsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                });
    }

    @Override
    public String getCategory(User user) {
        return user.translateRaw("treasure_chest.category.custom");
    }

    @Override
    public String getNameTranslationKey() {
        return "treasure_chest.loot.custom." + key;
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.empty();
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
}
