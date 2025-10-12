package se.file14.procosmetics.v1_21;

import com.mojang.datafixers.util.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Input;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R6.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.nms.NMSEntityImpl;
import se.file14.procosmetics.nms.entitytype.CachedEntityType;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NMSEntity extends NMSEntityImpl<Packet<? super ClientGamePacketListener>> {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final List<Pose> POSES = List.of(Pose.values());

    private static Constructor<FallingBlockEntity> fallingBlockConstructor;

    private Entity entity;
    private Entity leashHolder;

    public NMSEntity(World world, CachedEntityType cachedEntityType, EntityTracker tracker) {
        super(world, cachedEntityType, tracker);
        entity = ((net.minecraft.world.entity.EntityType<?>) cachedEntityType.getEntityTypeObject()).create(((CraftWorld) world).getHandle(), EntitySpawnReason.COMMAND);
    }

    public NMSEntity(World world, BlockData blockData, EntityTracker tracker) {
        super(world, blockData, tracker);

        try {
            if (fallingBlockConstructor == null) {
                fallingBlockConstructor = FallingBlockEntity.class.getDeclaredConstructor(Level.class, double.class, double.class, double.class, BlockState.class);
                fallingBlockConstructor.setAccessible(true);
            }
            entity = fallingBlockConstructor.newInstance(((CraftWorld) world).getHandle(), 0.0d, 0.0d, 0.0d, ((CraftBlockData) blockData).getState());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public NMSEntity(org.bukkit.entity.Entity realEntity) {
        super(realEntity);
        entity = ((CraftEntity) realEntity).getHandle();
    }

    @Override
    public void sendPacketToPlayers(@Nullable Collection<Player> players, @Nullable Packet<? super ClientGamePacketListener> packet) {
        if (packet == null || players == null) {
            return;
        }
        for (Player player : players) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }

    @Override
    public void sendPacketToViewers(@Nullable Packet<? super ClientGamePacketListener> packet) {
        if (packet == null || entityTracker == null) {
            return;
        }
        for (Player player : entityTracker.getViewers()) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }

    @Override
    public ClientboundAddEntityPacket getSpawnPacket() {
        return new ClientboundAddEntityPacket(
                entity.getId(),
                entity.getUUID(),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                entity.getXRot(),
                entity.getYRot(),
                entity.getType(),
                entity instanceof FallingBlockEntity fallingBlockEntity ? Block.getId(fallingBlockEntity.getBlockState()) : 0,
                entity.getDeltaMovement(),
                entity.getYHeadRot()
        );
    }

    @Override
    protected ClientboundBundlePacket getSpawnBundlePacket() {
        List<Packet<? super ClientGamePacketListener>> buffer = new ArrayList<>();
        addIfNotNull(buffer, getSpawnPacket());
        addIfNotNull(buffer, getEntityMetadataPacket(true));
        addIfNotNull(buffer, getUpdateAttributesPacket());
        addIfNotNull(buffer, getEntityEquipmentPacket());
        if (!entity.getPassengers().isEmpty()) {
            addIfNotNull(buffer, getSetPassengersPacket());
        }
        if (entity.isPassenger()) {
            addIfNotNull(buffer, getSetPassengersPacket(entity.getVehicle()));
        }
        addIfNotNull(buffer, getSetEntityLinkPacket());
        return new ClientboundBundlePacket(buffer);
    }

    @Override
    public ClientboundRemoveEntitiesPacket getDespawnPacket() {
        return new ClientboundRemoveEntitiesPacket(entity.getId());
    }

    @Override
    @Nullable
    protected ClientboundSetEntityDataPacket getEntityMetadataPacket(boolean isInitialView) {
        List<SynchedEntityData.DataValue<?>> metadata = isInitialView ?
                entity.getEntityData().getNonDefaultValues() : entity.getEntityData().packDirty();

        if (metadata != null) {
            return new ClientboundSetEntityDataPacket(entity.getId(), metadata);
        }
        return null;
    }

    @Override
    @Nullable
    protected ClientboundUpdateAttributesPacket getUpdateAttributesPacket() {
        if (entity instanceof LivingEntity livingEntity) {
            Set<AttributeInstance> set = livingEntity.getAttributes().getAttributesToSync();

            if (!set.isEmpty()) {
                return new ClientboundUpdateAttributesPacket(entity.getId(), set);
            }
        }
        return null;
    }

    @Override
    @Nullable
    public ClientboundSetEquipmentPacket getEntityEquipmentPacket() {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();
            EntityEquipment equipment = livingEntity.getEquipment();

            if (equipment != null) {
                ItemStack helmet = equipment.getHelmet();
                if (helmet != null) {
                    equipmentList.add(Pair.of(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(helmet)));
                }
                ItemStack chest = equipment.getChestplate();
                if (chest != null) {
                    equipmentList.add(Pair.of(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(chest)));
                }
                ItemStack legs = equipment.getLeggings();
                if (legs != null) {
                    equipmentList.add(Pair.of(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(legs)));
                }
                ItemStack boots = equipment.getBoots();
                if (boots != null) {
                    equipmentList.add(Pair.of(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(boots)));
                }
                ItemStack mainHand = equipment.getItemInMainHand();
                if (mainHand != null) {
                    equipmentList.add(Pair.of(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(mainHand)));
                }
                ItemStack offHand = equipment.getItemInOffHand();
                if (offHand != null) {
                    equipmentList.add(Pair.of(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(offHand)));
                }
                return new ClientboundSetEquipmentPacket(entity.getId(), equipmentList);
            }
        }
        return null;
    }

    @Override
    public ClientboundEntityEventPacket getEntityEventPacket(byte eventId) {
        return new ClientboundEntityEventPacket(entity, eventId);
    }

    @Override
    public ClientboundAnimatePacket getAnimatePacket(int actionId) {
        return new ClientboundAnimatePacket(entity, actionId);
    }

    @Override
    public ClientboundSetEntityMotionPacket getVelocityPacket() {
        if (entity.getDeltaMovement().lengthSqr() == 0) {
            return null;
        }
        return new ClientboundSetEntityMotionPacket(entity);
    }

    private ClientboundSetPassengersPacket getSetPassengersPacket(Entity entity) {
        return new ClientboundSetPassengersPacket(entity);
    }

    @Override
    public ClientboundSetPassengersPacket getSetPassengersPacket() {
        return getSetPassengersPacket(entity);
    }

    @Override
    public ClientboundSetEntityLinkPacket getSetEntityLinkPacket() {
        if (leashHolder != null) {
            return new ClientboundSetEntityLinkPacket(entity, leashHolder);
        }
        return null;
    }

    @Override
    public ClientboundRotateHeadPacket getRotateHeadPacket() {
        float yaw = entity.getYRot() * 256.0f / 360.0f;
        return new ClientboundRotateHeadPacket(entity, (byte) yaw);
    }

    @Override
    public ClientboundEntityPositionSyncPacket getTeleportEntityPacket() {
        return ClientboundEntityPositionSyncPacket.of(entity);
    }

    @Override
    public void setPositionRotation(Location location) {
        if (previousLocation == null) {
            previousLocation = location.clone();
        } else {
            if (previousLocation.equals(location)) {
                return;
            }
            setPreviousLocation(location);
        }
        entity.setPosRaw(location.getX(), location.getY(), location.getZ());
        entity.setXRot(location.getPitch());
        entity.setYRot(location.getYaw());
        entity.setYHeadRot(location.getYaw());
        entity.setYBodyRot(location.getYaw());
    }

    @Override
    public void setYaw(float yaw) {
        entity.setYRot(yaw);
    }

    @Override
    public void setPitch(float pitch) {
        entity.setXRot(pitch);
    }

    @Override
    public void navigateTo(Location location, double speed) {
        if (entity instanceof Mob mob) {
            mob.getNavigation().moveTo(location.getX(), location.getY(), location.getZ(), speed);
        }
    }

    @Override
    public void move(Vector vector) {
        entity.move(MoverType.SELF, new Vec3(vector.getX(), vector.getY(), vector.getZ()));
    }

    @Override
    public void moveRide(Player player) {
        LivingEntity entityLivingPlayer = ((CraftPlayer) player).getHandle();
        Input input = player.getCurrentInput();
        float forward = 0.0f;
        float strafe = 0.0f;
        boolean jumping = input.isJump();

        if (input.isForward()) {
            forward += 1.0f;
        }
        if (input.isBackward()) {
            forward -= 1.0f;
        }
        if (input.isLeft()) {
            strafe += 1.0f;
        }
        if (input.isRight()) {
            strafe -= 1.0f;
        }
        if (forward < 0.0f) {
            forward *= 0.50f;
        }
        Vec3 movementVec = new Vec3(strafe, 0.0f, forward);
        if (movementVec.lengthSqr() > 1.0) {
            movementVec = movementVec.normalize();
        }
        setYaw(entityLivingPlayer.getYRot());
        setPitch(entityLivingPlayer.getXRot());

        LivingEntity entityLiving = (LivingEntity) entity;

        entityLiving.setYHeadRot(entityLivingPlayer.getYRot());

        if (entity.onGround() && jumping) {
            Vec3 vec3d1 = entityLiving.getDeltaMovement();
            double jumpPower = 0.25d;
            entityLiving.setDeltaMovement(vec3d1.x * jumpPower, 0.5d, vec3d1.z * jumpPower);
        }
        float speedModifier = entity.onGround() ? getRideSpeed() : 0.3f;
        float speed;
        float swimSpeed;

        if (entityLiving.isInWater()) {
            double locY = entityLiving.getY();
            speed = 0.8f;
            swimSpeed = 0.02f;

            entityLiving.moveRelative(swimSpeed, movementVec);
            entityLiving.move(MoverType.SELF, entityLiving.getDeltaMovement());
            double motX = entityLiving.getDeltaMovement().x * (double) speed;
            double motY = entityLiving.getDeltaMovement().y * 0.800000011920929d;
            double motZ = entityLiving.getDeltaMovement().z * (double) speed;
            motY -= 0.02d;

            if (entityLiving.horizontalCollision && entityLiving.isFree(entityLiving.getDeltaMovement().x, entityLiving.getDeltaMovement().y + 0.6000000238418579d - entityLiving.getY() + locY, entityLiving.getDeltaMovement().z)) {
                motY = 0.30000001192092896d;
            }
            entityLiving.setDeltaMovement(motX, motY, motZ);
        } else {
            double minY = entityLiving.getBoundingBox().minY;
            float friction = 0.51f;

            if (entityLiving.onGround()) {
                friction = entityLiving.level().getBlockState(new BlockPos(Mth.floor(entityLiving.getX()), Mth.floor(minY) - 1, Mth.floor(entityLiving.getZ()))).getBlock().getFriction() * 0.91f;
            }
            speed = speedModifier * 0.21600002f / friction * friction * friction;

            a(speed, movementVec);

            double motX = entityLiving.getDeltaMovement().x;
            double motY = entityLiving.getDeltaMovement().y;
            double motZ = entityLiving.getDeltaMovement().z;

/*            if (entityLiving.isClimbing()) {
                swimSpeed = 0.15f;
                motX = MathHelper.a(motX, -swimSpeed, swimSpeed);
                motZ = MathHelper.a(motZ, -swimSpeed, swimSpeed);
                entityLiving.K = 0.0f;

                if (motY < -0.15d) {
                    motY = -0.15d;
                }
            }*/
            entityLiving.move(MoverType.SELF, new Vec3(motX, motY, motZ));

/*            if (entityLiving.A && entityLiving.isClimbing()) {
                motY = 0.2d;
            }*/
            motY -= 0.08d;
            motY *= 0.9800000190734863d;
            motX *= friction;
            motZ *= friction;

            entityLiving.setDeltaMovement(motX, motY, motZ);
        }
        entityLiving.calculateEntityAnimation(false);
    }

    public void a(float f, Vec3 vec3d) {
        Vec3 vec3d1 = a(vec3d, f, entity.getYRot());
        entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d1));
    }

    private static Vec3 a(Vec3 vec3d, float f, float f1) {
        double d0 = vec3d.length();
        if (d0 < 1.0E-7D) {
            return Vec3.ZERO;
        } else {
            Vec3 vec3d1 = (d0 > 1.0D ? vec3d.normalize() : vec3d).scale(f);
            float f2 = Mth.sin(f1 * 0.017453292F);
            float f3 = Mth.cos(f1 * 0.017453292F);
            return new Vec3(vec3d1.x * (double) f3 - vec3d1.z * (double) f2, vec3d1.y, vec3d1.z * (double) f3 + vec3d1.x * (double) f2);
        }
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        entity.setDeltaMovement(x, y, z);
    }

    @Override
    public void setMainHand(@Nullable ItemStack itemStack) {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null) {
                equipment.setItemInMainHand(itemStack);
            }
        }
    }

    @Override
    public void setOffHand(@Nullable ItemStack itemStack) {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null) {
                equipment.setItemInOffHand(itemStack);
            }
        }
    }

    @Override
    public void setHelmet(@Nullable ItemStack itemStack) {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null) {
                equipment.setHelmet(itemStack);
            }
        }
    }

    @Override
    public void setChestplate(@Nullable ItemStack itemStack) {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null) {
                equipment.setChestplate(itemStack);
            }
        }
    }

    @Override
    public void setLeggings(@Nullable ItemStack itemStack) {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null) {
                equipment.setLeggings(itemStack);
            }
        }
    }

    @Override
    public void setBoots(@Nullable ItemStack itemStack) {
        if (entity.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment != null) {
                equipment.setBoots(itemStack);
            }
        }
    }

    @Override
    public void setHeadPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setHeadPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setBodyPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setBodyPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setLeftArmPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setLeftArmPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setRightArmPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setRightArmPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setLeftLegPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setLeftLegPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setRightLegPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setRightLegPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setCustomName(@Nullable Component component) {
        entity.setCustomNameVisible(true);
        entity.setCustomName(CraftChatMessage.fromStringOrNull(SERIALIZER.serialize(component)));
    }

    @Override
    public void setLeashHolder(@Nullable org.bukkit.entity.Entity entity2) {
        leashHolder = ((CraftEntity) entity2).getHandle();
    }

    @Override
    public void setCreeperPowered(boolean powered) {
        if (entity instanceof Creeper creeper) {
            creeper.setPowered(powered);
        }
    }

    @Override
    public void setCreeperIgnited(boolean ignited) {
        if (entity instanceof Creeper creeper) {
            creeper.setSwellDir(ignited ? 1 : -1);
        }
    }

    @Override
    public void setHurtTicks(int ticks) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.hurtTime = ticks;
        }
    }

    @Override
    public void setPose(org.bukkit.entity.Pose pose) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setPose(POSES.get(pose.ordinal()));
        }
    }

    @Override
    public void setNoClip(boolean noClip) {
        entity.noPhysics = noClip;
    }

    @Override
    public void setEntityItemStack(ItemStack itemStack) {
        if (entity instanceof ItemEntity itemEntity) {
            itemEntity.setItem(CraftItemStack.asNMSCopy(itemStack));
        }
    }

    @Override
    public void setHorseStanding(boolean standing) {
        if (entity instanceof Horse horse) {
            if (standing) {
                horse.setStanding(1);
            } else {
                horse.clearStanding();
            }
        }
    }

    @Override
    public void setGuardianTarget(int id) {
        if (entity instanceof Guardian guardian) {
            guardian.setActiveAttackTarget(id);
        }
    }

    @Override
    public void removePathfinder() {
        try {
            Mob entityInsentient = (Mob) entity;

            entityInsentient.goalSelector.getAvailableGoals().clear();
            entityInsentient.targetSelector.getAvailableGoals().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getNMSEntity() {
        return entity;
    }

    @Override
    public int getId() {
        return entity.getId();
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntity() {
        return entity.getBukkitEntity();
    }
}