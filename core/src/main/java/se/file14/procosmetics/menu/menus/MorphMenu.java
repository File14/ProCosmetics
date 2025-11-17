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
package se.file14.procosmetics.menu.menus;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.CosmeticMenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class MorphMenu extends CosmeticMenuImpl<MorphType> {

    private static final int COOLDOWN = 20;

    public MorphMenu(ProCosmetics plugin, User user, CosmeticCategory<MorphType, ?, ?> category) {
        super(plugin, user, category);
    }

    @Override
    protected void addCustomItems() {
        if (category.getConfig().getBoolean("menu.items.toggle_self_view.enabled")) {
            Player player = getPlayer();
            ItemBuilder itemBuilder = new ItemBuilderImpl(category.getConfig(), "menu.items.toggle_self_view");
            String path = user.hasSelfViewMorph() ? "disable" : "enable";
            itemBuilder.setDisplayName(user.translate("menu.morphs.toggle_self_view." + path + ".name"));
            itemBuilder.setLore(user.translateList("menu.morphs.toggle_self_view." + path + ".desc"));

            if (user.hasSelfViewMorph()) {
                itemBuilder.setGlintOverride(true);
            }

            setItem(itemBuilder.getSlot(), itemBuilder.getItemStack(), event -> {
                if (player.hasCooldown(itemBuilder.getItemStack())) {
                    return;
                }
                player.setCooldown(itemBuilder.getItemStack(), COOLDOWN);

                boolean toggle = !user.hasSelfViewMorph();
                user.setSelfViewMorph(toggle, true);
                plugin.getDatabase().setSelfViewMorphAsync(user, toggle);

                player.playSound(player, Sound.ENTITY_CHICKEN_EGG, 0.5f, 1.0f);
                refresh();
            });
        }
    }
}
