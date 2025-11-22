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
package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExplosiveSheep implements GadgetBehavior {

    private final static List<DyeColor> DYE_COLORS = List.of(DyeColor.values());
    private static final int SHEEP_AMOUNT = 10;

    private Sheep sheep;
    private final Set<Sheep> babies = new HashSet<>();
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        if (sheep != null || !babies.isEmpty()) {
            return InteractionResult.FAILED;
        }
        Player player = context.getPlayer();
        location = player.getLocation();

        sheep = location.getWorld().spawn(location, Sheep.class, entity -> {
            Vector vector = player.getEyeLocation().getDirection();
            vector.setY(vector.getY() + 0.5d);
            entity.setVelocity(vector);

            MetadataUtil.setCustomEntity(entity);
        });

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(), () -> {
            if (sheep == null) {
                return;
            }
            sheep.getLocation(location);

            sheep.getWorld().spawnParticle(Particle.EXPLOSION, location, 0);
            sheep.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.4f, 1.0f);

            despawnSheep();

            for (int i = 0; i < SHEEP_AMOUNT; i++) {
                location.setYaw(location.getYaw() + 45.0f);

                babies.add(location.getWorld().spawn(location, Sheep.class, entity -> {
                    entity.setBaby();
                    entity.setColor(DYE_COLORS.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(DYE_COLORS.size())));
                    entity.setVelocity(new Vector(
                            MathUtil.randomRange(-0.5d, 0.5d),
                            MathUtil.randomRange(0.8d, 1.5d),
                            MathUtil.randomRange(-0.5d, 0.5d)
                    ));
                    MetadataUtil.setCustomEntity(entity);
                }));
            }
        }, Long.max(0L, context.getType().getDurationInTicks() - 80L));

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (sheep == null || !sheep.isValid()) {
            return;
        }
        sheep.setColor(sheep.getColor() == DyeColor.WHITE ? DyeColor.RED : DyeColor.WHITE);
        sheep.getWorld().playSound(sheep.getLocation(location), Sound.UI_BUTTON_CLICK, 0.5f, 2.0f);
        location.getWorld().spawnParticle(Particle.LARGE_SMOKE, location.add(0.0d, 0.5d, 0.0d), 0);
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        despawnSheep();
        removeBabies();
    }

    private void despawnSheep() {
        if (sheep != null) {
            sheep.remove();
            sheep = null;
        }
    }

    private void removeBabies() {
        for (Entity entity : babies) {
            location.getWorld().spawnParticle(Particle.CLOUD, entity.getLocation(location).add(0.0d, 0.3d, 0.0d), 0);
            entity.remove();
        }
        babies.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}
