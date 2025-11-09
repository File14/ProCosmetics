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
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.treasure.loot.CosmeticLoot;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.menu.menus.purchase.CosmeticPurchaseMenu;

import java.util.List;

public class CosmeticLootImpl extends LootTableImpl<CosmeticType<?, ?>> implements CosmeticLoot {

    private final List<CosmeticType<?, ?>> cosmeticTypes;

    public CosmeticLootImpl(String category, int weight, List<CosmeticType<?, ?>> cosmeticTypes) {
        super(category, weight);
        this.cosmeticTypes = cosmeticTypes;
    }

    @Override
    public CosmeticType<?, ?> getRandomLoot() {
        return cosmeticTypes.get(RANDOM.nextInt(cosmeticTypes.size()));
    }

    @Override
    public void give(Player player, User user, CosmeticType<?, ?> lootEntry) {
        CosmeticPurchaseMenu.grantCosmeticPermission(PLUGIN, player, lootEntry);

        PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                player,
                lootEntry.getRarity(),
                "treasure_chest.loot." + getKey() + ".broadcast",
                receiverUser -> new TagResolver[]{
                        Placeholder.unparsed("player", player.getName()),
                        Placeholder.unparsed("cosmetic", lootEntry.getName(user)),
                        Placeholder.unparsed("category", getCategory(user)),
                        Placeholder.unparsed("rarity", lootEntry.getRarity().getName(user)),
                        Placeholder.parsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                        Placeholder.parsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                });
    }

    @Override
    public List<CosmeticType<?, ?>> getCosmeticTypes() {
        return cosmeticTypes;
    }
}
