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
package se.file14.procosmetics.cosmetic.miniature;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.joml.Matrix4f;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.miniature.Miniature;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.util.FastMathUtil;

public class MiniatureImpl extends CosmeticImpl<MiniatureType, MiniatureBehavior> implements Miniature {

    private static final float DEGREES_180 = FastMathUtil.toRadians(180.0f);
    private static final double MAX_DISTANCE_SQUARED_BEFORE_TELEPORT = 256.0d;
    private static final double DISTANCE_SQUARED_BEFORE_MOVE_TOWARDS_PLAYER = 3.5d;
    private static final double ACCELERATION = 0.15d;

    protected NMSEntity entity;
    protected int tick;
    private double currentSpeedX;
    private double currentSpeedZ;

    private final Location playerLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location entityLocation = new Location(null, 0.0d, 0.0d, 0.0d);

    public MiniatureImpl(ProCosmeticsPlugin plugin, User user, MiniatureType type, MiniatureBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
        spawnEntity();
        runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    @Override
    public void onUpdate() {
        player.getLocation(playerLocation);
        entity.getPreviousLocation(entityLocation);

        if (entityLocation.getWorld() != playerLocation.getWorld()) {
            return;
        }
        double distanceSquared = playerLocation.distanceSquared(entityLocation);

        if (distanceSquared > MAX_DISTANCE_SQUARED_BEFORE_TELEPORT) {
            entity.sendPositionRotationPacket(playerLocation);
            return;
        }
        double yOffset = 0.3d * FastMathUtil.cos(FastMathUtil.toRadians(tick * 6));
        double y = playerLocation.getY() + 0.8d + yOffset;
        entityLocation.setY(y);

        entityLocation.setDirection(playerLocation.add(0.0d, 0.8d, 0.0d)
                .subtract(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ())
                .toVector()
                .normalize());

        if (distanceSquared > DISTANCE_SQUARED_BEFORE_MOVE_TOWARDS_PLAYER) {
            float angle = FastMathUtil.toRadians(entityLocation.getYaw());
            double targetSpeed = distanceSquared * 0.02d;
            double targetX = -FastMathUtil.sin(angle) * targetSpeed;
            double targetZ = FastMathUtil.cos(angle) * targetSpeed;

            // Smooth acceleration/deceleration
            currentSpeedX += (targetX - currentSpeedX) * ACCELERATION;
            currentSpeedZ += (targetZ - currentSpeedZ) * ACCELERATION;
        } else {
            // Smoothly decelerate to zero when close to player
            currentSpeedX *= (1.0d - ACCELERATION);
            currentSpeedZ *= (1.0d - ACCELERATION);
        }
        entityLocation.add(currentSpeedX, 0.0d, currentSpeedZ);
        entity.sendPositionRotationPacket(entityLocation);
        entity.setHeadPose(entityLocation.getPitch(), 0.0f, 0.0f);
        entity.sendEntityMetadataPacket();

        if (++tick >= 360) {
            tick = 0;
        }
        behavior.onUpdate(this, entity, tick);
    }

    @Override
    protected void onUnequip() {
        if (entity != null) {
            entity.getTracker().destroy();
            entity = null;
        }
    }

    private void spawnEntity() {
        entity = plugin.getNMSManager().createEntity(player.getWorld(), cosmeticType.getEntityType());
        entity.setPositionRotation(getSpawnLocation());
        Entity bukkitEntity = entity.getBukkitEntity();

        applyEntityScale(bukkitEntity);

        if (cosmeticType.isBaby() && bukkitEntity instanceof Ageable ageable) {
            ageable.setBaby();
        }

        if (entity.getBukkitEntity() instanceof ArmorStand armorStand) {
            armorStand.setBasePlate(false);
            armorStand.setSmall(cosmeticType.isBaby());
            armorStand.setArms(true);
            entity.setHelmet(cosmeticType.getItemStack());
        } else if (entity.getBukkitEntity() instanceof Display display) {
            configureDisplayEntity(display);
        }
        behavior.setupEntity(this, entity);
        entity.getTracker().startTracking();
    }

    private void applyEntityScale(Entity bukkitEntity) {
        if (bukkitEntity instanceof LivingEntity livingEntity) {
            AttributeInstance attribute = livingEntity.getAttribute(Attribute.SCALE);

            if (attribute != null) {
                attribute.setBaseValue(cosmeticType.getScale());
            }
        }
    }

    private void configureDisplayEntity(Display display) {
        float scale = (float) cosmeticType.getScale();
        Matrix4f transformationMatrix = new Matrix4f();

        if (display instanceof ItemDisplay itemDisplay) {
            itemDisplay.setItemStack(cosmeticType.getItemStack());
            transformationMatrix.identity()
                    .scale(scale)
                    .rotateY(DEGREES_180)
                    .translate(0.0f, 0.5f, 0.0f);
        } else if (display instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(cosmeticType.getItemStack().getType().createBlockData());
            transformationMatrix.identity()
                    .scale(scale)
                    .rotateY(DEGREES_180)
                    .translate(-0.5f, 0.0f, -0.5f);
        }
        display.setTransformationMatrix(transformationMatrix);
        display.setTeleportDuration(2);
    }

    private Location getSpawnLocation() {
        player.getLocation(playerLocation).add(0.0d, 0.5d, 0.0d).add(playerLocation.getDirection().multiply(1.5d));
        playerLocation.setDirection(player.getLocation()
                .subtract(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ())
                .toVector()
                .normalize());
        return playerLocation;
    }

    @Override
    public NMSEntity getNMSEntity() {
        return entity;
    }
}
