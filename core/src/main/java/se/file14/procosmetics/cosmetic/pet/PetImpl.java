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
package se.file14.procosmetics.cosmetic.pet;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.pet.Pet;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.event.CosmeticEntitySpawnEventImpl;
import se.file14.procosmetics.nms.NMSEntityImpl;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

public class PetImpl extends CosmeticImpl<PetType, PetBehavior> implements Pet {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    protected NMSEntityImpl nmsEntity;
    protected Entity entity;
    protected Location location;

    public PetImpl(ProCosmeticsPlugin plugin, User user, PetType type, PetBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
        spawn();
        runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    protected void onUpdate() {
        if (user.isMoving()) {
            nmsEntity.follow(player);
        }

        if (cosmeticType.getTossItem() != null) {
            dropDespawningItem(cosmeticType.getTossItem());
        }
        behavior.onUpdate(this, entity);
    }

    @Override
    protected void onUnequip() {
        despawn();
    }

    @Override
    public void spawn() {
        spawn(player.getLocation());
    }

    @Override
    public void spawn(Location location) {
        this.location = location.clone();

        despawn();

        entity = location.getWorld().spawn(location, cosmeticType.getEntityType().getEntityClass(), entity -> {
            Component nameTag = user.translate(
                    "cosmetic.pets.name_tag",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("cosmetic", cosmeticType.getName(user))
            );
            entity.setCustomName(SERIALIZER.serialize(nameTag));
            entity.setCustomNameVisible(!nameTag.equals(Component.empty()));
            entity.setSilent(true);

            if (entity instanceof LivingEntity livingEntity) {
                AttributeInstance attribute = livingEntity.getAttribute(Attribute.SCALE);

                if (attribute != null) {
                    attribute.setBaseValue(cosmeticType.getScale());
                }
            }

            if (cosmeticType.isBaby()) {
                if (entity instanceof Ageable ageable) {
                    ageable.setBaby();
                    ageable.setAgeLock(true);
                    ageable.setBreed(false);
                }
            }
            MetadataUtil.setCustomEntity(entity);
            behavior.onSetupEntity(this, entity);
        });
        nmsEntity = plugin.getNMSManager().entityToNMSEntity(entity);

        setupEntity();

        Sound spawnSound = cosmeticType.getSpawnSound();
        if (spawnSound != null) {
            entity.getWorld().playSound(location, spawnSound, 0.5f, 1.0f);
        }
        plugin.getNMSManager().entityToNMSEntity(entity).removePathfinder();

        CosmeticEntitySpawnEventImpl event = new CosmeticEntitySpawnEventImpl(plugin, user, player, entity);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    private void despawn() {
        if (nmsEntity != null) {
            nmsEntity = null;
        }
        if (entity != null) {
            entity.remove();
            entity = null;
        }
    }

    protected void dropDespawningItem(ItemStack itemStack) {
        NMSEntityImpl nmsEntity = plugin.getNMSManager().createEntity(entity.getWorld(), EntityType.ITEM);
        nmsEntity.setEntityItemStack(itemStack);
        nmsEntity.setVelocity(
                MathUtil.randomRange(-0.5d, 0.5d),
                MathUtil.randomRange(-0.1d, 0.3d),
                MathUtil.randomRange(-0.5d, 0.5d)
        );
        nmsEntity.setPositionRotation(entity.getLocation(location).add(0.0d, 0.2d, 0.0d));
        nmsEntity.getTracker().startTracking();
        nmsEntity.getTracker().destroyAfter(70);
    }

    protected void setupEntity() {
    }
}
