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

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.RGBFade;
import se.file14.procosmetics.util.material.Materials;

public class DiscoBall implements GadgetBehavior {

    private static final double HEIGHT_OFFSET = 4.0d;
    private static final int POINTS = 20;
    private static final double RANGE = 5.0d;
    private static final float ROTATION_PER_TICK = 5.0f;

    private NMSEntity nmsEntity;
    private int tick;
    private Location location;
    private final RGBFade rgbFade = new RGBFade();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, Action action, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        location = player.getLocation();

        nmsEntity = context.getPlugin().getNMSManager().createEntity(player.getWorld(), EntityType.ARMOR_STAND);
        if (nmsEntity.getBukkitEntity() instanceof ArmorStand armorStand) {
            armorStand.setInvisible(true);
            armorStand.setArms(false);
        }
        nmsEntity.setPositionRotation(location.add(0.0d, HEIGHT_OFFSET, 0.0d));
        nmsEntity.getTracker().startTracking();

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationTicks()
        );
        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (nmsEntity == null) {
            return;
        }
        nmsEntity.setHeadPose(0.0f, ROTATION_PER_TICK * tick, 0.0f);
        nmsEntity.sendEntityMetadataPacket();

        if (tick % 4 == 0) {
            nmsEntity.setHelmet(Materials.getRandomStainedGlassItem());
            nmsEntity.sendEntityEquipmentPacket();
        }
        nmsEntity.getPreviousLocation(location).add(0.0d, 1.6d, 0.0d);

        Location randomLocation = location.clone().add(
                MathUtil.randomRange(-RANGE, RANGE),
                MathUtil.randomRange(-RANGE, RANGE),
                MathUtil.randomRange(-RANGE, RANGE)
        );
        location.getWorld().spawnParticle(Particle.FIREWORK, randomLocation, 0, 0.0f, 0.0f, 0.0f, 1.0f);

        location.getWorld().spawnParticle(Particle.NOTE, randomLocation, 0, MathUtil.randomRange(1.0d, 25.0d) / 24.0d, 0.0d, 0.0d, 0.0d);
        Vector vector = randomLocation.subtract(location).toVector().normalize().multiply(-0.4d);
        Location loc = location.clone();

        for (int i = 0; i < POINTS; i++) {
            rgbFade.nextRGB();

            loc.add(vector);
            location.getWorld().spawnParticle(Particle.DUST, loc, 0, rgbFade.getR(), rgbFade.getG(), rgbFade.getB(), 0.0d,
                    new Particle.DustOptions(Color.fromRGB(rgbFade.getR(), rgbFade.getG(), rgbFade.getB()), 1)
            );
        }
        tick++;

        if (tick > 360) {
            tick = 0;
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        if (nmsEntity != null) {
            nmsEntity.getTracker().destroy();
            nmsEntity = null;
        }
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        location.add(0.0d, HEIGHT_OFFSET, 0.0d);
        Material material = location.getBlock().getType();
        location.subtract(0.0d, HEIGHT_OFFSET, 0.0d);

        return material.isAir();
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}
