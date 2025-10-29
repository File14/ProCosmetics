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
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class AmmoLoot extends LootTable<AmmoLootEntry> {

    private static final GadgetType COIN_PARTY_BOMB = PLUGIN.getCategoryRegistries().gadgets().getCosmeticRegistry().getType("coin_party_bomb");

    private final int min;
    private final int max;
    private final List<GadgetType> ammo;

    public AmmoLoot(String key, int weight, int min, int max, List<GadgetType> ammo) {
        super(key, weight);
        this.min = min;
        this.max = max;
        this.ammo = ammo;
    }

    @Override
    public AmmoLootEntry getRandomLoot() {
        GadgetType gadgetType = ammo.get(RANDOM.nextInt(ammo.size()));
        int amount = MathUtil.randomRangeInt(min, max);

        // Always force the coin party bomb ammo to be 1
        if (gadgetType.equals(COIN_PARTY_BOMB)) {
            amount = 1;
        }
        return new AmmoLootEntry(gadgetType, amount);
    }

    @Override
    public void give(Player player, User user, AmmoLootEntry lootEntry) {
        PLUGIN.getDatabase().addGadgetAmmoAsync(user, lootEntry.getAmmo(), lootEntry.getAmount()).thenAccept(result -> {
            if (result.leftBoolean()) {
                PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                        player,
                        lootEntry.getRarity(),
                        "treasure_chest.loot." + key + ".broadcast",
                        receiverUser -> new TagResolver[]{
                                Placeholder.unparsed("player", player.getName()),
                                Placeholder.unparsed("cosmetic", lootEntry.getName(receiverUser)),
                                Placeholder.unparsed("amount", String.valueOf(lootEntry.getAmount())),
                                Placeholder.unparsed("rarity", lootEntry.getRarity().getName(receiverUser)),
                                Placeholder.parsed("rarity_primary_color", lootEntry.getRarity().getPrimaryColor()),
                                Placeholder.parsed("rarity_secondary_color", lootEntry.getRarity().getSecondaryColor())
                        });
            } else {
                user.sendMessage(user.translate("generic.error.database"));
            }
        });
    }
}
