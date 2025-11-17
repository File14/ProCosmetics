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
import se.file14.procosmetics.api.treasure.loot.CoinsLoot;
import se.file14.procosmetics.api.treasure.loot.GeneratedLoot;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.number.IntProvider;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.treasure.loot.GeneratedLootImpl;
import se.file14.procosmetics.treasure.loot.LootEntryImpl;

public class CoinsLootImpl extends LootEntryImpl implements CoinsLoot {

    private final CosmeticRarity rarity;

    public CoinsLootImpl(IntProvider intProvider, LootCategory category, CosmeticRarity rarity) {
        super(intProvider, category);
        this.rarity = rarity;
    }

    @Override
    public GeneratedLoot generate() {
        return new GeneratedCoinsLoot(this, intProvider.get());
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.resolver(super.getResolvers(user),
                Placeholder.unparsed("currency", user.translateRaw("generic.currency"))
        );
    }

    @Override
    public CosmeticRarity getRarity() {
        return rarity;
    }

    @Override
    public ItemStack getItemStack() {
        return category.getItemBuilder().getItemStack();
    }

    private static class GeneratedCoinsLoot extends GeneratedLootImpl<CoinsLoot> {

        public GeneratedCoinsLoot(CoinsLoot entry, int amount) {
            super(entry, amount);
        }

        @Override
        public void give(Player player, User user) {
            PLUGIN.getEconomyManager().getEconomyProvider().addCoinsAsync(user, amount).thenAcceptAsync(result -> {
                if (result.booleanValue()) {
                    PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                            player,
                            entry.getRarity(),
                            "treasure_chest.loot." + getKey() + ".broadcast",
                            receiverUser -> TagResolver.resolver(
                                    Placeholder.unparsed("player", player.getName()),
                                    getResolvers(receiverUser)
                            ));
                } else {
                    user.sendMessage(user.translate("generic.error.database"));
                }
            }, PLUGIN.getSyncExecutor());
        }
    }
}
