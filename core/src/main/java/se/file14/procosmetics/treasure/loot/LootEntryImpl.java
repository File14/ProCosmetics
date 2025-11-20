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
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.treasure.loot.number.IntProvider;
import se.file14.procosmetics.api.user.User;

public abstract class LootEntryImpl<T extends IntProvider> implements LootEntry {

    protected static ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    protected final T intProvider;
    protected final LootCategory category;

    public LootEntryImpl(T intProvider, LootCategory category) {
        this.intProvider = intProvider;
        this.category = category;
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.resolver(
                Placeholder.unparsed("loot", getName(user)),
                Placeholder.unparsed("category", category.getName(user)),
                getRarity().getResolvers(user)
        );
    }

    @Override
    public String getKey() {
        return category.getKey();
    }

    @Override
    public String getNameTranslationKey() {
        return "treasure_chest.loot." + getKey();
    }

    @Override
    public T getIntProvider() {
        return intProvider;
    }

    @Override
    public LootCategory getCategory() {
        return category;
    }
}
