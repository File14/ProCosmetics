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
package se.file14.procosmetics.cosmetic.miniature.type;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class Enderman implements MiniatureBehavior {

    private static final ItemStack HAND_ITEM = new ItemStack(Material.ENDER_PEARL);
    private static final ItemStack CHESTPLATE_ITEM;
    private static final ItemStack LEGGINGS_ITEM;
    private static final ItemStack BOOTS_ITEM;

    static {
        Color color = Color.BLACK;
        CHESTPLATE_ITEM = new ItemBuilderImpl(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).getItemStack();
        LEGGINGS_ITEM = new ItemBuilderImpl(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color).getItemStack();
        BOOTS_ITEM = new ItemBuilderImpl(Material.LEATHER_BOOTS).setLeatherArmorColor(color).getItemStack();
    }

    @Override
    public void onEquip(CosmeticContext<MiniatureType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity) {
        nmsEntity.setMainHand(HAND_ITEM);
        nmsEntity.setChestplate(CHESTPLATE_ITEM);
        nmsEntity.setLeggings(LEGGINGS_ITEM);
        nmsEntity.setBoots(BOOTS_ITEM);
    }

    @Override
    public void onUpdate(CosmeticContext<MiniatureType> context, NMSEntity nmsEntity, int tick) {
        nmsEntity.getPreviousLocation().getWorld().spawnParticle(Particle.PORTAL,
                nmsEntity.getPreviousLocation(),
                2,
                0.3d,
                0.5d,
                0.3d,
                0.0d
        );
    }

    @Override
    public void onUnequip(CosmeticContext<MiniatureType> context) {
    }
}
