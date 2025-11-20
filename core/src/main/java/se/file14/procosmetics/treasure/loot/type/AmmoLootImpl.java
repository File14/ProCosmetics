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
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.treasure.loot.AmmoLoot;
import se.file14.procosmetics.api.treasure.loot.GeneratedLoot;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.number.IntProvider;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.treasure.loot.GeneratedLootImpl;
import se.file14.procosmetics.treasure.loot.LootEntryImpl;

public class AmmoLootImpl extends LootEntryImpl implements AmmoLoot {

    private final GadgetType gadgetType;

    public AmmoLootImpl(IntProvider intProvider, LootCategory category, GadgetType gadgetType) {
        super(intProvider, category);
        this.gadgetType = gadgetType;
    }

    @Override
    public String getKey() {
        return gadgetType.getKey();
    }

    @Override
    public String getNameTranslationKey() {
        return gadgetType.getNameTranslationKey();
    }

    @Override
    public CosmeticRarity getRarity() {
        return gadgetType.getAmmoRarity();
    }

    @Override
    public ItemStack getItemStack() {
        return gadgetType.getItemStack();
    }

    @Override
    public GeneratedLoot generate() {
        return new GeneratedAmmoLoot(this, intProvider.get());
    }

    @Override
    public GadgetType getGadgetType() {
        return gadgetType;
    }

    private static class GeneratedAmmoLoot extends GeneratedLootImpl<AmmoLoot> {

        public GeneratedAmmoLoot(AmmoLoot entry, int amount) {
            super(entry, amount);
        }

        @Override
        public void give(Player player, User user) {
            PLUGIN.getDatabase().addGadgetAmmoAsync(user, entry.getGadgetType(), amount).thenAccept(result -> {
                if (result.leftBoolean()) {
                    PLUGIN.getTreasureChestManager().getLootBroadcaster().broadcastMessage(
                            player,
                            getRarity(),
                            "treasure_chest.loot." + getKey() + ".broadcast",
                            receiverUser -> TagResolver.resolver(
                                    Placeholder.unparsed("player", player.getName()),
                                    getResolvers(receiverUser)
                            ));
                } else {
                    user.sendMessage(user.translate("generic.error.database"));
                }
            });
        }
    }
}
