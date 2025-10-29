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
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.item.Heads;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class SantaClaus implements PetBehavior {

    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1);

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    @Override
    public void onEquip(CosmeticContext<PetType> context) {
        context.getPlayer().getLocation(location);
    }

    @Override
    public void onSetupEntity(CosmeticContext<PetType> context, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            Color color = Color.RED;

            entityEquipment.setHelmet(Heads.SANTA.getSkull());
            entityEquipment.setChestplate(
                    new ItemBuilderImpl(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).getItemStack());
            entityEquipment.setLeggings(
                    new ItemBuilderImpl(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color).getItemStack());
            entityEquipment.setBoots(new ItemBuilderImpl(Material.LEATHER_BOOTS).setLeatherArmorColor(color).getItemStack());
            entityEquipment.setItemInHand(Heads.RED_PRESENT.getSkull());

            ((Zombie) entity).setBaby(false);
            livingEntity.setCanPickupItems(false);
        }
    }

    @Override
    public void onUpdate(CosmeticContext<PetType> context, Entity entity) {
        for (int i = 0; i < 10; ++i) {
            location.getWorld().spawnParticle(Particle.DUST, entity.getLocation(location).add(
                    MathUtil.randomRange(-1.2d, 1.2d),
                    MathUtil.randomRange(0.0d, 3.5d),
                    MathUtil.randomRange(-1.2d, 1.2d)
            ), 0, 255, 255, 255, 0.1f, DUST_OPTIONS);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<PetType> context) {
    }
}
