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
package se.file14.procosmetics.cosmetic.pet.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;
import se.file14.procosmetics.util.item.Heads;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class Elf implements PetBehavior {

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    @Override
    public void onEquip(CosmeticContext<PetType> context) {
    }

    @Override
    public void onSetupEntity(CosmeticContext<PetType> context, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            Color color = Color.GREEN;

            entityEquipment.setHelmet(Heads.ELF.getSkull());
            entityEquipment.setChestplate(
                    new ItemBuilderImpl(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).getItemStack());
            entityEquipment.setLeggings(
                    new ItemBuilderImpl(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color).getItemStack());
            entityEquipment.setBoots(new ItemBuilderImpl(Material.LEATHER_BOOTS).setLeatherArmorColor(color).getItemStack());
            entityEquipment.setItemInHand(Heads.TURQUOISE_PRESENT.getSkull());

            livingEntity.setCanPickupItems(false);
        }
    }

    @Override
    public void onUpdate(CosmeticContext<PetType> context, Entity entity) {
        entity.getLocation(location);
        location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 3, 0.5d, 0.8d, 0.5d, 0.1d);
    }

    @Override
    public void onUnequip(CosmeticContext<PetType> context) {
    }
}
