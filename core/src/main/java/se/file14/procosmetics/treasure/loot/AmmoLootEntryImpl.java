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
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.treasure.loot.AmmoLootEntry;
import se.file14.procosmetics.api.user.User;

public class AmmoLootEntryImpl implements AmmoLootEntry {

    private static final ProCosmetics PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final GadgetType ammo;
    private final int amount;

    public AmmoLootEntryImpl(GadgetType ammo, int amount) {
        this.ammo = ammo;
        this.amount = amount;
    }

    @Override
    public GadgetType getAmmo() {
        return ammo;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String getNameTranslationKey() {
        return "treasure_chest.loot.ammo";
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.resolver(
                Placeholder.unparsed("ammo", ammo.getName(user)),
                Placeholder.unparsed("amount", String.valueOf(amount))
        );
    }

    @Override
    public CosmeticRarity getRarity() {
        return PLUGIN.getCosmeticRarityRegistry().getFallbackRarity();
    }

    @Override
    public ItemStack getItemStack() {
        return ammo.getItemStack();
    }
}
