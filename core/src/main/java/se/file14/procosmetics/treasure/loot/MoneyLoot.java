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
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;

public class MoneyLoot extends LootTable<MoneyLootEntry> {

    private final int min;
    private final int max;

    public MoneyLoot(String key, int weight, int min, int max) {
        super(key, weight);
        this.min = min;
        this.max = max;
    }

    @Override
    public MoneyLootEntry getRandomLoot() {
        int amount = MathUtil.randomRangeInt(min, max);
        return new MoneyLootEntry(amount);
    }

    @Override
    public void give(Player player, User user, MoneyLootEntry lootEntry) {
        PLUGIN.getEconomyManager().getEconomyProvider().addCoinsAsync(user, lootEntry.getAmount()).thenAcceptAsync(result -> {
            if (result.booleanValue()) {
                PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                        player,
                        lootEntry.getRarity(),
                        "treasure_chest.loot." + getKey() + ".broadcast",
                        receiverUser -> new TagResolver[]{
                                Placeholder.unparsed("player", player.getName()),
                                Placeholder.unparsed("cosmetic", lootEntry.getName(user)),
                                Placeholder.unparsed("amount", String.valueOf(lootEntry.getName(user))),
                                Placeholder.unparsed("rarity", lootEntry.getRarity().getName(user)),
                                Placeholder.unparsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                                Placeholder.unparsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                        });
            } else {
                user.sendMessage(user.translate("generic.error.database"));
            }
        }, PLUGIN.getSyncExecutor());
    }
}
