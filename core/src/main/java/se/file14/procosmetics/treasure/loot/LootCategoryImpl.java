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

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import se.file14.procosmetics.api.treasure.loot.LootCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;

public class LootCategoryImpl implements LootCategory {

    private final String key;
    private final ItemBuilder itemBuilder;

    public LootCategoryImpl(String key, ItemBuilder itemBuilder) {
        this.key = key;
        this.itemBuilder = itemBuilder;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getNameTranslationKey() {
        return "treasure_chest.category." + key;
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.empty();
    }
}
